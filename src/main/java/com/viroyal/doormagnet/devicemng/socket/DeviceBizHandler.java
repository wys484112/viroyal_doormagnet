
package com.viroyal.doormagnet.devicemng.socket;

import java.nio.ByteBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.viroyal.doormagnet.devicemng.entity.*;
import com.viroyal.doormagnet.devicemng.mapper.DeviceMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceOpLogMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceSettingMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceStatusMapper;
import com.viroyal.doormagnet.devicemng.pojo.BindUser;
import com.viroyal.doormagnet.util.RandomUtil;
import com.viroyal.doormagnet.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.viroyal.doormagnet.util.TextUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import javax.annotation.PostConstruct;

/**
 *
 */
@Component
public class DeviceBizHandler {
    private static final Logger logger = LoggerFactory.getLogger(DeviceBizHandler.class);

    private static final int ALERT_TYPE_DOOR_OPEN = 0;
    private static final int ALERT_TYPE_LOW_POWER = 1;
    private static final int ALERT_TYPE_OFFLINE = 2;
    private static final int ALERT_TYPE_ONLINE = 3;

    @Autowired
    private DeviceMapper mDeviceMapper;

    @Autowired
    private DeviceStatusMapper mDeviceStatusMapper;

    @Autowired
    private DeviceOpLogMapper mDeviceOpLogMapper;

    @Autowired
    private DeviceSettingMapper mDeviceSettingMapper;

    @Autowired
    private JPushClient mJPushClient;

    @Autowired
    private Environment mEnvironment;

    private boolean mIsProductEnv;

    @PostConstruct
    private void postInit() {
        mIsProductEnv = false;
        String[] profiles = mEnvironment.getActiveProfiles();
        if (profiles != null && profiles.length > 0 && "prod".equals(profiles[0])) {
            mIsProductEnv = true;
        }
        logger.info("mIsProductEnv={}", mIsProductEnv);
    }
    public void execute(DeviceMessageBase request) {
        if (request.mWhat == DeviceMessage.MSG_READ_DATA) {
            onChannelRead(request);
        } else if (request.mWhat == DeviceMessage.MSG_SOCKET_CLOSED) {
            // onChannelClose(request.mChannel);
        }
    }

    /**
     * 收到设备数据时的处理 目前可能的情况是
     * <p>
     * 1 设备开机后上报id 2 设备发送的心跳 3 设备执行指令的回应
     *
     * @param messageBase
     */
    protected void onChannelRead(DeviceMessageBase messageBase) {
        DeviceMessage msg = (DeviceMessage) messageBase;
        Channel ch = msg.mChannel;

        ByteBuffer bf = ByteBuffer.wrap(msg.mPayload);

        int cmd = bf.get() & 0xff;

        if (cmd == 0x21) { // 设备注册
            onDevReg(bf, ch, msg.mPayload);
        } else if (cmd == 0x81) { // 设备上报数据
            onDevData(bf, ch);
        } else {
            logger.error("invalid cmd = {}", Integer.toHexString(cmd));
        }
    }

    private String readSn(ByteBuffer buf) {
        byte[] bytes = new byte[6];
        buf.get(bytes);
        String sn = TextUtils.byte2HexStrNoSpace(bytes);
        sn = sn.toUpperCase();
        return sn;
    }

    private void sendToDevice(Channel ch, int cmd, byte[] reqPayload) {
        // 0xAA length(1) cmd(1) SN(6) payload(n) checksum(1)
        int len = 1 + 1 + 6;
        ByteBuf buf = ch.alloc().buffer();
        buf.writeByte(0xAA);
        buf.writeByte(len);
        buf.writeByte(cmd);
        buf.writeBytes(reqPayload, 1, 6);

        byte checksum = Utils.calcChecksum(buf, 1, buf.readableBytes());
        buf.writeByte(checksum);

        logger.info(TextUtils.byte2HexStr(buf) + " ch=" + ch);
        ch.writeAndFlush(buf);
    }

    private void sendToDevice(Channel ch, int cmd, ByteBuf payload) {
        // 0xAA length(1) cmd(1) payload(n) checksum(1)
        int len = 1 + 1 + payload.readableBytes();
        ByteBuf buf = ch.alloc().buffer();
        buf.writeByte(0xAA);
        buf.writeByte(len);
        buf.writeByte(cmd);
        buf.writeBytes(payload);

        byte checksum = Utils.calcChecksum(buf, 1, buf.readableBytes());
        buf.writeByte(checksum);

        logger.info(TextUtils.byte2HexStr(buf) + " ch=" + ch);
        ch.writeAndFlush(buf);
    }

    private Device getDeviceBySn(String sn) {
        return mDeviceMapper.findBySn(sn);
    }

    /**
     * 收到设备注册消息的处理
     */
    private void onDevReg(ByteBuffer bf, Channel ch, byte[] reqPayload) {
        String sn = readSn(bf);
        logger.info("device reg {}", sn);

        Device entity = getDeviceBySn(sn);
        if (entity == null) {
            logger.warn("invalid sn {}, insert one", sn);
            //插入一条新纪录
            entity = new Device();
            entity.sn = sn;
            try {
                mDeviceMapper.insertDevice(entity);
            } catch (DuplicateKeyException e) {
                //不处理，继续执行
            }
            entity = getDeviceBySn(sn);
        }
        //发送响应给设备
        sendToDevice(ch, 0x31, reqPayload);

        if (entity.status != Device.STATUS_NOT_ACTIVATED) {
            logger.warn("device already register status=" + entity.status);
            //处理在线状态变更
            onDevOnlineChanged(entity.id, 1, entity.online, true);
            return;
        }
        //设置状态为激活
        mDeviceMapper.register(Device.STATUS_ACTIVATED, sn);
        //增加log
        DeviceOpLog logEntity = new DeviceOpLog();
        logEntity.action = DeviceOpLog.ACTION_REGISTER;
        logEntity.msg = "设备注册(" + sn + ")";

        logEntity.devId = entity.id;
        logEntity.actionTime = new Timestamp(System.currentTimeMillis());
        mDeviceOpLogMapper.insertOne(logEntity);

        //处理在线状态变更
        onDevOnlineChanged(entity.id, 1, entity.online, true);

        return;
    }

    /**
     * 设备上报数据的处理
     *
     * @param bf 表示上报数据的buffer
     * @return
     */
    private void onDevData(ByteBuffer bf, Channel channel) {
        String sn = readSn(bf);
        //序号(2B) 操作(1B) 时间(6B) 上传间隔(1B) 电量(1B) 设防状态(1B) 设防开始(2B) 设防结束(2B)
        Device device = getDeviceBySn(sn);
        if (device == null) {
            logger.warn("invalid sn {}", sn);
            return;
        }
        logger.info("device data {}, dev_id={}", sn, device.id);
//        if (device.status != Device.STATUS_BINDED) {
//            logger.warn("device not binded, status=" + device.status);
//            return;
//        }

        //先发送回应给设备，避免等得太久
        short sequence = bf.getShort();
        DeviceStatus lastDeviceStatus = mDeviceStatusMapper.findLastSequence(device.id);
        int lastSequence = -1;
        if (lastDeviceStatus != null) {
            lastSequence = lastDeviceStatus.lastSequence;
        }

        DeviceSetting settingEntity = mDeviceSettingMapper.findOne(device.id);
        if (settingEntity != null) {
            sendDevDataRsp(settingEntity, sequence, channel);
        } else {
            logger.error("can't find setting for dev=" + device.id);
        }

        //是否重复上报
        if (lastSequence == sequence) {
            logger.info("duplicate device message, seq=" + sequence);
            return;
        }

        //有没有绑定，没有的话忽略数据
        int bindCount = mDeviceMapper.getBindCountByDevId(device.id);
        if (bindCount == 0) {
            logger.info("device not binded, dev_id = " + device.id);
            return;
        }

        //保存设备状态数据
        int action = bf.get() & 0xFF;
        if (action < 1 || action > 6) {
            logger.warn("invalid action=" + action);
            return;
        }

        DeviceStatus statusEntity = new DeviceStatus();
        statusEntity.devId = device.id;
        statusEntity.lastSequence = sequence;
        if (lastDeviceStatus != null) {
            statusEntity.alertTime = lastDeviceStatus.alertTime;
        }

        int[] actionTime = new int[6];
        for (int i = 0; i < 6; i++) {
            actionTime[i] = bcdByte2Int(bf.get());
        }
        Calendar calAction = Calendar.getInstance();
        calAction.set(2000 + actionTime[0], actionTime[1] - 1, actionTime[2], actionTime[3], actionTime[4], actionTime[5]);
        calAction.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("action={}, action_time={}", action, formatter.format(calAction.getTime()));

        //2018.03.06 检查action时间是否有效，最多是当前时间加1小时，最小是1个月前
        Calendar calActionMax = Calendar.getInstance();
        calActionMax.add(Calendar.HOUR_OF_DAY, 1);
        Calendar calActionMin = Calendar.getInstance();
        calActionMin.add(Calendar.DAY_OF_MONTH, -30);
        boolean isActionTimeValid = true;
        if (calAction.after(calActionMax) || calAction.before(calActionMin)) {
            logger.error("action time is invalid");
            isActionTimeValid = false;
        }

        //上报的是开关状态还是充电状态
        int updateType = 0; // 1 更新开关时间，2更新充电时间 0 只更新基本状态
        boolean shouldAlert = false;
        if (isActionTimeValid && (action == 1 || action == 2)) {
            updateType = 1;
            statusEntity.openStatus = action;
            statusEntity.openStatusTime = new Timestamp(calAction.getTimeInMillis());
            logger.info("device open status changed, dev_id={}, action={}", device.id, action);
            if (action == 1) {
                //检查要不要告警
                if (settingEntity.security == 1) {
                    if (settingEntity.allDay == 1) {
                        shouldAlert = true;
                    } else {
                        /*
                        String timeStr = String.format("%02d:%02d:%02d", calAction.get(Calendar.HOUR_OF_DAY), calAction.get(Calendar.MINUTE),
                                calAction.get(Calendar.SECOND));
                        Time alertTime = Time.valueOf(timeStr);
                        logger.info("check alert: action={} start={} end={}", alertTime.getTime(), settingEntity.timeStart.getTime(),
                                settingEntity.timeEnd.getTime());
                        if (alertTime.getTime() >= settingEntity.timeStart.getTime()
                                && alertTime.getTime() <= settingEntity.timeEnd.getTime()) {
                            shouldAlert = true;
                        }
                        */
                        Calendar alertStart = Calendar.getInstance();
                        Calendar alertEnd = Calendar.getInstance();
                        alertStart.setTimeInMillis(settingEntity.timeStart.getTime());
                        alertEnd.setTimeInMillis(settingEntity.timeEnd.getTime());
                        alertStart.set(Calendar.YEAR, calAction.get(Calendar.YEAR));
                        alertStart.set(Calendar.MONTH, calAction.get(Calendar.MONTH));
                        alertStart.set(Calendar.DAY_OF_MONTH, calAction.get(Calendar.DAY_OF_MONTH));

                        alertEnd.set(Calendar.YEAR, calAction.get(Calendar.YEAR));
                        alertEnd.set(Calendar.MONTH, calAction.get(Calendar.MONTH));
                        alertEnd.set(Calendar.DAY_OF_MONTH, calAction.get(Calendar.DAY_OF_MONTH));

                        logger.info("action time={}, alert_start={}, alert_end={}", formatter.format(calAction.getTime()),
                                formatter.format(alertStart.getTime()),
                                formatter.format(alertEnd.getTime()));
                        if (alertEnd.getTimeInMillis() < alertStart.getTimeInMillis()) {
                            //结束时间是第二天
                            alertEnd.add(Calendar.DAY_OF_MONTH, 1);
                            logger.info("change alert_end={}", formatter.format(alertEnd.getTime()));
                        }
                        if (calAction.getTimeInMillis() >= alertStart.getTimeInMillis()
                                && calAction.getTimeInMillis() <= alertEnd.getTimeInMillis()) {
                            logger.info("action time in alert setting");
                            shouldAlert = true;
                        }
                    }
                }
                if (shouldAlert) {
                    logger.info("should send alert");
                    statusEntity.alertTime = new Timestamp(calAction.getTimeInMillis());
                }
            }
        } else if (isActionTimeValid && (action == 3 || action == 4 || action == 5)) {
            updateType = 2;
            statusEntity.chargeStatus = action;
            statusEntity.chargeStatusTime = new Timestamp(calAction.getTimeInMillis());
            logger.info("device charge status changed, dev_id={}", device.id);
        }
        statusEntity.reportInterval = bf.get() & 0xFF;
        statusEntity.voltage = bf.get() & 0xFF;
        //计算百分比电量
        statusEntity.power = getPowerPercent(statusEntity.voltage );

        statusEntity.seurity = bf.get() & 0xFF;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, bcdByte2Int(bf.get()));
        cal.set(Calendar.MINUTE, bcdByte2Int(bf.get()));
        statusEntity.timeStart = new Time(cal.getTimeInMillis());
        cal.set(Calendar.HOUR_OF_DAY, bcdByte2Int(bf.get()));
        cal.set(Calendar.MINUTE, bcdByte2Int(bf.get()));
        statusEntity.timeEnd = new Time(cal.getTimeInMillis());
        int updateCount;
        if (updateType == 1) {
            updateCount = mDeviceStatusMapper.saveOpenStatusChanged(statusEntity);
            if (updateCount == 0) {
                mDeviceStatusMapper.insertOpenStatusChanged(statusEntity);
            }
        } else if (updateType == 2) {
            updateCount = mDeviceStatusMapper.saveChargeStatusChanged(statusEntity);
            if (updateCount == 0) {
                mDeviceStatusMapper.insertChargeStatusChanged(statusEntity);
            }
        } else {
            updateCount = mDeviceStatusMapper.saveBasicStatusChanged(statusEntity);
            if (updateCount == 0) {
                mDeviceStatusMapper.insertBasicStatusChanged(statusEntity);
            }
        }

        if (!isActionTimeValid) {
            onDevOnlineChanged(device.id, 1, device.online, true);
            return;
        }
        //写log表
        DeviceOpLog logEntity = new DeviceOpLog();
        switch (action) {
            case 1:
                logEntity.action = DeviceOpLog.ACTION_OPEN;
                logEntity.msg = "门磁打开";
                break;
            case 2:
                logEntity.action = DeviceOpLog.ACTION_CLOSE;
                logEntity.msg = "门磁关闭";
                break;
            case 3:
                logEntity.action = DeviceOpLog.ACTION_CHARGE_STARAT;
                logEntity.msg = "开始充电";
                break;
            case 4:
            case 5:
                logEntity.action = DeviceOpLog.ACTION_CHARGE_FINISH;
                logEntity.msg = "充电结束";
                break;

            default: //6 状态上报
                logEntity.action = DeviceOpLog.ACTION_STATUS_REPORT;
                logEntity.msg = "定时状态上报";
                break;
        }
        logEntity.devId = device.id;
        logEntity.actionTime = new Timestamp(calAction.getTimeInMillis());
        mDeviceOpLogMapper.insertOne(logEntity);

        boolean pushSent = false;
        //门磁打开告警处理
        if (shouldAlert) {
            sendPush(device.id, ALERT_TYPE_DOOR_OPEN);
            pushSent = true;
        }
        //低电量告警处理,目前定的是低于3.6v告警,对应上报值是180
        if (statusEntity.voltage <= 180 && (lastDeviceStatus == null || lastDeviceStatus.voltage > 180)) {
            sendPush(device.id, ALERT_TYPE_LOW_POWER);
            pushSent = true;
            //写log表
            logEntity = new DeviceOpLog();
            logEntity.action = DeviceOpLog.ACTION_LOW_POWER;
            logEntity.msg = "设备电量低";
            logEntity.devId = device.id;
            logEntity.actionTime = new Timestamp(calAction.getTimeInMillis());
            mDeviceOpLogMapper.insertOne(logEntity);
        }
        onDevOnlineChanged(device.id, 1, device.online, !pushSent);

        return;
    }

    /**
     * 发送设置信息给设备
     *
     * @param settingEntity 设备的设置数据
     * @param sequence      请求序列号，原样返回
     * @param channel       channel
     */
    private void sendDevDataRsp(DeviceSetting settingEntity, short sequence, Channel channel) {
        //序号(2B) 时间(6B) 上传间隔(1B) 设防状态(1B) 设防开始(2B) 设防结束(2B)
        ByteBuf byteBuf = channel.alloc().buffer(20);
        byteBuf.writeShort(sequence);
        //
        Calendar calNow = Calendar.getInstance();
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.YEAR) - 2000));
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.MONTH) + 1));
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.DAY_OF_MONTH)));
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.HOUR_OF_DAY)));
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.MINUTE)));
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.SECOND)));
        //上传间隔
        byteBuf.writeByte(settingEntity.reportInterval);
        //设防状态
        int security = 0xff; //模块不设防
        if (settingEntity.security == 1) {
            if (settingEntity.beep == 1) {
                security = 0x1; //模块设防，在设防时段，打开将触发LED灯和蜂鸣器同时报警；
            } else {
                security = 0x2; //模块设防，在设防时段，打开将只触发LED灯报警，蜂鸣器不动作；
            }
        }
        byteBuf.writeByte(security);
        //设防开始
        if (settingEntity.allDay == 1) {
            calNow.set(Calendar.HOUR_OF_DAY, 0);
            calNow.set(Calendar.MINUTE, 0);
        } else {
            calNow.setTimeInMillis(settingEntity.timeStart.getTime());
        }
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.HOUR_OF_DAY)));
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.MINUTE)));
        //设防结束
        if (settingEntity.allDay == 1) {
            calNow.set(Calendar.HOUR_OF_DAY, 23);
            calNow.set(Calendar.MINUTE, 59);
        } else {
            calNow.setTimeInMillis(settingEntity.timeEnd.getTime());
        }
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.HOUR_OF_DAY)));
        byteBuf.writeByte(intToBcdByte(calNow.get(Calendar.MINUTE)));

        sendToDevice(channel, 0x91, byteBuf);
    }

    private int bcdByte2Int(byte bcd) {
        return (bcd >>> 4) * 10 + (bcd & 0x0F);
    }

    private int intToBcdByte(int value) {
        return (value % 10) | ((value / 10) << 4);
    }

    private PushPayload buildPushObject(String tag, int devId, String alert) {
        Map<String, String> extras = new HashMap<String, String>();
        extras.put("dev_id", "" + devId);
        extras.put("extra_2", "val2");

        PushPayload.Builder builder = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(tag))
                .setNotification(Notification.newBuilder()
                        .setAlert(alert)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .addExtras(extras).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtras(extras).build())
                        .build());

        if (mIsProductEnv) {
            builder = builder.setOptions(Options.newBuilder()
                    .setApnsProduction(true).build());
        }
        return builder.build();
    }

    private void sendJPush(final PushPayload payload) {
        try {
            PushResult result = mJPushClient.sendPush(payload);
            logger.info("Got result - " + result);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            logger.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e.getErrorMessage());
//            logger.info("HTTP Status: " + e.getStatus());
//            logger.info("Error Code: " + e.getErrorCode());
//            logger.info("Error Message: " + e.getErrorMessage());
//            logger.info("Msg ID: " + e.getMsgId());
//            logger.error("Sendno: " + payload.getSendno());
        }
    }

    /**
     * 产生推送消息并发给用户
     * @param devId  设备id
     * @param pushType  0-开门告警 1-底电告警
     */
    private void sendPush(int devId, int pushType) {
        //查询关注此设备的用户及其push设置
        List<BindUser> bindInfoList = mDeviceMapper.getBindUserList(devId);
        if (bindInfoList.size() == 0) {
            logger.warn("no bind user to send push");
            return;
        }

        for (BindUser entity : bindInfoList) {
            logger.info("send push to user: {}  push_id={}, app_push={}, push_type={}", entity.userId, entity.pushId, entity.appPush, pushType);
            if (entity.appPush == 0 || TextUtils.isEmpty(entity.pushId)) {
                continue;
            }
            //String tag = "user_" + entity.userId;
            String tag = entity.pushId;
            String alert = null;
            if (pushType == ALERT_TYPE_DOOR_OPEN) {
                alert = "您所使用的设备（" + entity.name + "）产生了警报，请及时登录APP查看详细信息";
            } else if (pushType == ALERT_TYPE_LOW_POWER) {
                alert = "您所使用的设备（" + entity.name + "）电量过低，请及时充电，以免影响正常使用";
            } else if (pushType == ALERT_TYPE_OFFLINE) {
                alert = "您所使用的设备（" + entity.name + "）已断开网络连接，请速检查";
            } else if (pushType == ALERT_TYPE_ONLINE) {
                alert = "您所使用的设备（" + entity.name + "）已连接网络";
            }
            if (alert != null) {
                final PushPayload payload = buildPushObject(tag, devId, alert);
                sendJPush(payload);
            }
        }
    }

    private int getPowerPercent(int voltage) {
        int percentVol = 0;
        voltage *= 2;

        if (voltage > 415) {
            percentVol = 100;
        } else if (voltage > 380) {
            percentVol = 80 + (voltage - 380) * 20 / (415 - 380);
        } else if (voltage > 360) {
            percentVol = 20 + (voltage - 360) * 60 / (380 - 360);
        } else if (voltage > 340) {
            percentVol = (voltage - 340) * 20 / (360 - 340);
        }

        return percentVol;
    }


    /**
     * 定时获取离线设备，发送告警
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    private void checkOfflineDevice() {
        try {
            MDC.put(RandomUtil.MDC_KEY, RandomUtil.getMDCValue());

            logger.info("scheduled...");

            List<Integer> devList = mDeviceStatusMapper.findOfflineDevices();
            for (int  devId: devList) {
                onDevOnlineChanged(devId, 0, 1, true);
            }
        } finally {
            MDC.remove(RandomUtil.MDC_KEY);
        }
    }

    private void onDevOnlineChanged(int devId, int onlineNew, int onlineOld, boolean sendPush) {
        //写表，主要更新一下online时间字段
        mDeviceMapper.updateOnline(devId, onlineNew);
        if (onlineNew == onlineOld) {
            return;
        }
        //要不要发告警
        if (sendPush) {
            //写log表
            DeviceOpLog logEntity = new DeviceOpLog();
            logEntity.action = (onlineNew == 1) ? DeviceOpLog.ACTION_ONLINE : DeviceOpLog.ACTION_OFFLINE;
            logEntity.msg = (onlineNew == 1) ? "设备在线":"设备离线";
            logEntity.devId = devId;
            logEntity.actionTime = new Timestamp(System.currentTimeMillis());
            mDeviceOpLogMapper.insertOne(logEntity);
            //发push
            sendPush(devId, (onlineNew == 1) ? ALERT_TYPE_ONLINE : ALERT_TYPE_OFFLINE);
        }
    }
}
