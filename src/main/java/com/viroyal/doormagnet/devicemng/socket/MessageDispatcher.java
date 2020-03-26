package com.viroyal.doormagnet.devicemng.socket;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.viroyal.doormagnet.common.util.ErrorCode;
import com.viroyal.doormagnet.common.util.MyConstant;
import com.viroyal.doormagnet.common.util.TextUtils;
import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.mapper.DeviceMessageMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceReadTimeMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceReportAngleAbnormalMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceReportCellIdMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceReportCurrentAbnormalMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceReportHardVersionMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceReportHeartBeatMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceReportLightAbnormalMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceReportPowerConsumptionAbnormalMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceReportSoftVersionMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceResponseMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceStatusMapper;
import com.viroyal.doormagnet.devicemng.mapper.ServiceSettingsDeviceBrightnessMapper;
import com.viroyal.doormagnet.devicemng.mapper.ServiceSettingsDeviceInstallationstateAnglethreadholdMapper;
import com.viroyal.doormagnet.devicemng.mapper.ServiceSettingsDeviceLightingStrategyMapper;
import com.viroyal.doormagnet.devicemng.mapper.ServiceSettingsDevicePowerConsumptionMapper;
import com.viroyal.doormagnet.devicemng.mapper.ServiceSettingsDeviceReportIntervalMapper;
import com.viroyal.doormagnet.devicemng.mapper.ServiceSettingsDeviceSwitchMapper;
import com.viroyal.doormagnet.devicemng.mapper.ServiceSettingsDeviceTimeMapper;
import com.viroyal.doormagnet.devicemng.model.*;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

@Service
public class MessageDispatcher {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DeviceStatusMapper mDeviceStatusMapper;

	@Autowired
	private DeviceMessageMapper deviceMessageMapper;

	@Autowired
	private DeviceResponseMapper deviceResponseMapper;

	@Autowired
	private ServiceSettingsDeviceSwitchMapper serviceSettingsDeviceSwitchMapper;

	@Autowired
	private ServiceSettingsDeviceBrightnessMapper serviceSettingsDeviceBrightnessMapper;

	@Autowired
	private ServiceSettingsDeviceReportIntervalMapper serviceSettingsDeviceReportIntervalMapper;

	@Autowired
	private ServiceSettingsDeviceLightingStrategyMapper serviceSettingsDeviceLightingStrategyMapper;

	@Autowired
	private ServiceSettingsDeviceTimeMapper serviceSettingsDeviceTimeMapper;

	@Autowired
	private ServiceSettingsDeviceInstallationstateAnglethreadholdMapper serviceSettingsDeviceInstallationstateAnglethreadholdMapper;

	@Autowired
	private ServiceSettingsDevicePowerConsumptionMapper serviceSettingsDevicePowerConsumptionMapper;

	@Autowired
	private DeviceReadTimeMapper deviceReadTimeMapper;

	@Autowired
	private DeviceReportCellIdMapper deviceReportCellIdMapper;

	@Autowired
	private DeviceReportSoftVersionMapper deviceReportSoftVersionMapper;

	@Autowired
	private DeviceReportHardVersionMapper deviceReportHardVersionMapper;

	@Autowired
	private DeviceReportLightAbnormalMapper deviceReportLightAbnormalMapper;

	@Autowired
	private DeviceReportCurrentAbnormalMapper deviceReportCurrentAbnormalMapper;

	@Autowired
	private DeviceReportAngleAbnormalMapper deviceReportAngleAbnormalMapper;

	@Autowired
	private DeviceReportPowerConsumptionAbnormalMapper deviceReportPowerConsumptionAbnormalMapper;

	@Autowired
	private DeviceReportHeartBeatMapper deviceReportHeartBeatMapper;

	final Object object = new Object();

	@Async
	public void handleMessage(Channel ch, byte[] msg) {
		try {
			logger.info("handleMessage thread==" + Thread.currentThread().getName());

			DeviceMessage message = decodeMessage(ch, (byte[]) msg);
			if (message != null) {
				DeviceServer.ALLCHANNELS_GROUP.add(message);
				onDevData(message);
			}else {
				throw new Exception("message format illegal"); 
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("error, handleMessage channel=" + ch + ", error=" + e.getMessage());
		}

	}

	// 6F010001 01 383637373235303330303935353738 0064 03E8 0003E8 0003E8 50 32 03E8
	// 38 04 04 05 00 01 00 64 01 03 04B0 04B0 04B0 04B0 0D0A0D0A

	private void onDevData(DeviceMessage message) throws Exception {
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		logger.info("dispatch message imei:" + message.getImei() + "   message flag:" + message.getFlaghexstr()
				+ "   message control flag:" + message.getControlhexstr());
		switch (message.getFlaghexstr()) {
		// 设备主动上报信息处理
		case "00":
			switch (message.getControlhexstr()) {
			case "01":// 3.1设备定时上报数据
				onDevMessage01(message);
				test(message);
				break;
			case "02":// 3.2设备请求读时间
				onDevMessage02(message);
				break;
			case "03":// 3.3设备上报CELLID
				onDevMessage03(message);
				break;
			case "04":// 3.4设备上报软件版本号
				onDevMessage04(message);
				break;
			case "05":// 3.5设备上报固件版本号
				onDevMessage05(message);

				break;
			case "06":// 3.6设备开关灯异常报警
				onDevMessage06(message);

				break;
			case "07":// 3.7灯具大电流报警
				onDevMessage07(message);

				break;
			case "08":// 3.8倾斜器报警
				onDevMessage08(message);

				break;
			case "09":// 3.9 上报耗电量
				onDevMessage09(message);

				break;
			case "0a":// 3.10 保持连接的心跳
				onDevMessage0a(message);

				break;

			default:
				break;
			}
			break;
		// 设备回复信息处理
		case "01":
			switch (message.getControlhexstr()) {
			case "21":
				onDevMessageResponse(message);
				break;

			default:
				onDevMessageResponse(message);
				break;
			}

			break;
		default:
			break;
		}
	}

	private void test(DeviceMessage message) {
		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(message.getImei()));
		toDeviceMessage.setImei(message.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("11");
		toDeviceMessage.setContentlengthhexstr("0005");
		toDeviceMessage.setContenthexstr("0101013131");
		toDeviceMessage.setResponsecontrolhexstr("21");
		toDeviceMessage.setTime(new Date());

		deviceMessageMapper.insertOrUpdate(toDeviceMessage);
	}

	private void onDevMessageResponse(DeviceMessage message) throws Exception {
		DeviceResponse response = new DeviceResponse();
		response.setImei(message.getImei());
		response.setControlhexstr(message.getControlhexstr());
		response.setErrorcode((byte) Integer.parseInt(message.getContenthexstr().substring(34, 36), 16));
		Thread.sleep(1000);
		response.setTime(new Date());

		deviceResponseMapper.insert(response);
		logger.info(
				"onDevMessageResponse，response imei==" + response.getImei() + "  control" + message.getControlhexstr());
		int aa = deviceMessageMapper.deleteByImeiAndControl(message.getImei(),
				MyConstant.controlResponseControlHEXMap.get(message.getControlhexstr()));
		logger.info("onDevMessageResponse，deleteByImeiAndControl aa==" + aa);

	}

	// 6F01000101002F383637373235303330303935353738006403E80003E80003E8503203E83804040500010064010304B004B004B004B00D0A0D0A
	private void onDevMessage01(DeviceMessage message) throws Exception {
		DeviceStatus status = new DeviceStatus();
		status.setImei(message.getImei());
		String contentHexStr = message.getContenthexstr();

		String voltage = contentHexStr.substring(30, 34);// 2
		String current = contentHexStr.substring(34, 38);// 2
		String activepower = contentHexStr.substring(38, 44);// 3
		String reactivepower = contentHexStr.substring(44, 50);// 3
		String powerfactor = contentHexStr.substring(50, 52);// 1
		String temperature = contentHexStr.substring(52, 54);// 1
		String powerconsumptionintegerpart = contentHexStr.substring(54, 58);// 2
		String powerconsumptiondecimalpart = contentHexStr.substring(58, 60);// 1
		String brightnesscontrolone = contentHexStr.substring(60, 62);// 1
		String brightnesscontroltwo = contentHexStr.substring(62, 64);// 1
		String brightnesscontrolthree = contentHexStr.substring(64, 66);// 1
		String switchcontrolone = contentHexStr.substring(66, 68);// 1
		String switchcontroltwo = contentHexStr.substring(68, 70);// 1
		String switchcontrolthree = contentHexStr.substring(70, 72);// 1
		String signalstrengthabsolutevalue = contentHexStr.substring(72, 74);// 1
		String dimmingmode = contentHexStr.substring(74, 76);// 1
		String abnormalflag = contentHexStr.substring(76, 78);// 1
		String angleone = contentHexStr.substring(78, 82);// 2
		String angletwo = contentHexStr.substring(82, 86);// 2
		String angleoriginalone = contentHexStr.substring(86, 90);// 2
		String angleoriginaltwo = contentHexStr.substring(90, 94);// 2

		status.setCurrent(Integer.parseInt(current, 16));
		status.setVoltage(Integer.parseInt(voltage, 16));
		status.setActivepower(Integer.parseInt(activepower, 16));
		status.setReactivepower(Integer.parseInt(reactivepower, 16));
		status.setPowerfactor(Integer.parseInt(powerfactor, 16));
		status.setTemperature(Integer.parseInt(temperature, 16));
		status.setPowerconsumptionintegerpart(Integer.parseInt(powerconsumptionintegerpart, 16));
		status.setPowerconsumptiondecimalpart(Integer.parseInt(powerconsumptiondecimalpart, 16));
		status.setBrightnesscontrolone(Integer.parseInt(brightnesscontrolone, 16));
		status.setBrightnesscontroltwo(Integer.parseInt(brightnesscontroltwo, 16));
		status.setBrightnesscontrolthree(Integer.parseInt(brightnesscontrolthree, 16));
		status.setSwitchcontrolone(Integer.parseInt(switchcontrolone, 16));
		status.setSwitchcontroltwo(Integer.parseInt(switchcontroltwo, 16));
		status.setSwitchcontrolthree(Integer.parseInt(switchcontrolthree, 16));
		status.setSignalstrengthabsolutevalue(Integer.parseInt(signalstrengthabsolutevalue, 16));
		status.setDimmingmode(Integer.parseInt(dimmingmode, 16));
		status.setAbnormalflag(Integer.parseInt(abnormalflag, 16));
		status.setAngleone(Integer.parseInt(angleone, 16));
		status.setAngletwo(Integer.parseInt(angletwo, 16));
		status.setAngleoriginalone(Integer.parseInt(angleoriginalone, 16));
		status.setAngleoriginaltwo(Integer.parseInt(angleoriginaltwo, 16));
		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		status.setTime(new Date());

		int index = mDeviceStatusMapper.insertSelective(status);
		logger.info("onDevMessage01 insertSelective index:" + index);

		// 判断状态中的电流电源 等数据是否正常，对设备进行回复
		onServiceResponse01(message, true);
	}

	private void onServiceResponse01(DeviceMessage message, Boolean isRight) throws Exception {
		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(message.getChannel());
		toDeviceMessage.setFlaghexstr("01");
		toDeviceMessage.setControlhexstr(message.getControlhexstr());
		toDeviceMessage.setContentlengthhexstr("0001");
		toDeviceMessage.setContenthexstr(isRight ? "00" : "01");
		sendMsg(toDeviceMessage.toString(), toDeviceMessage.getChannel());
	}

	private void onDevMessage02(DeviceMessage message) throws Exception {
		DeviceReadTime temp = new DeviceReadTime();
		temp.setImei(message.getImei());

		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		temp.setTime(new Date());

		int index = deviceReadTimeMapper.insertSelective(temp);
		logger.info("insertSelective index:" + index);

		// 判断数据是否正常，对设备进行回复
		onServiceResponse02(message, true);
	}

	private void onServiceResponse02(DeviceMessage message, Boolean isRight) throws Exception {

		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改

		ServiceSettingsDeviceTime temp = new ServiceSettingsDeviceTime();
		temp.setImei(message.getImei());
		temp.setYear(c.get(Calendar.YEAR));
		temp.setMonth(c.get(Calendar.MONTH));
		temp.setDay(c.get(Calendar.DATE));
		temp.setHour(c.get(Calendar.HOUR));
		temp.setMinute(c.get(Calendar.MINUTE));
		temp.setSecond(c.get(Calendar.SECOND));

		setDeviceSettingTime(null, null, temp);
	}

	private void onDevMessage03(DeviceMessage message) throws Exception {
		DeviceReportCellId temp = new DeviceReportCellId();
		temp.setImei(message.getImei());
		String contentHexStr = message.getContenthexstr();

		String cellId = contentHexStr.substring(30, 34);// 2

		temp.setCellid(Integer.parseInt(cellId, 16));
		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		temp.setTime(new Date());

		int index = deviceReportCellIdMapper.insertSelective(temp);
		logger.info("onDevMessage01 insertSelective index:" + index);

		// 判断状态中数据是否正常，对设备进行回复
		onServiceResponse01(message, true);
	}

	private void onDevMessage04(DeviceMessage message) throws Exception {
		DeviceReportSoftVersion temp = new DeviceReportSoftVersion();
		temp.setImei(message.getImei());
		String contentHexStr = message.getContenthexstr();

		String softversionHexStr = contentHexStr.substring(30);//

		temp.setSoftversion(TextUtils.hexStr2AscIIStr(softversionHexStr));
		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		temp.setTime(new Date());

		int index = deviceReportSoftVersionMapper.insertSelective(temp);
		logger.info("onDevMessage01 insertSelective index:" + index);

		// 判断状态中数据是否正常，对设备进行回复
		onServiceResponse01(message, true);
	}

	private void onDevMessage05(DeviceMessage message) throws Exception {
		DeviceReportHardVersion temp = new DeviceReportHardVersion();
		temp.setImei(message.getImei());
		String contentHexStr = message.getContenthexstr();

		String hardversionHexStr = contentHexStr.substring(30);//

		temp.setHardversion(TextUtils.hexStr2AscIIStr(hardversionHexStr));
		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		temp.setTime(new Date());

		int index = deviceReportHardVersionMapper.insertSelective(temp);
		logger.info("onDevMessage01 insertSelective index:" + index);

		// 判断状态中数据是否正常，对设备进行回复
		onServiceResponse01(message, true);
	}

	private void onDevMessage06(DeviceMessage message) throws Exception {
		DeviceReportLightAbnormal temp = new DeviceReportLightAbnormal();
		temp.setImei(message.getImei());
		String contentHexStr = message.getContenthexstr();

		String statusHexStr = contentHexStr.substring(30);//

		temp.setLightstatus((byte) Integer.parseInt(statusHexStr, 16));
		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		temp.setTime(new Date());

		int index = deviceReportLightAbnormalMapper.insertSelective(temp);
		logger.info("onDevMessage01 insertSelective index:" + index);

		// 判断状态中数据是否正常，对设备进行回复
		onServiceResponse01(message, true);
	}

	private void onDevMessage07(DeviceMessage message) throws Exception {
		DeviceReportCurrentAbnormal temp = new DeviceReportCurrentAbnormal();
		temp.setImei(message.getImei());
		String contentHexStr = message.getContenthexstr();

		String statusHexStr = contentHexStr.substring(30);//

		temp.setCurrentstatus((byte) Integer.parseInt(statusHexStr, 16));
		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		temp.setTime(new Date());

		int index = deviceReportCurrentAbnormalMapper.insertSelective(temp);
		logger.info("onDevMessage01 insertSelective index:" + index);

		// 判断状态中数据是否正常，对设备进行回复
		onServiceResponse01(message, true);
	}

	private void onDevMessage08(DeviceMessage message) throws Exception {
		DeviceReportAngleAbnormal temp = new DeviceReportAngleAbnormal();
		temp.setImei(message.getImei());
		String contentHexStr = message.getContenthexstr();

		temp.setAbnormalflag((byte) Integer.parseInt(contentHexStr.substring(30, 32), 16));
		temp.setAngleone(Integer.parseInt(contentHexStr.substring(32, 36), 16) / 10);
		temp.setAngletwo(Integer.parseInt(contentHexStr.substring(36, 40), 16) / 10);
		temp.setAngleoriginalone(Integer.parseInt(contentHexStr.substring(40, 44), 16) / 10);
		temp.setAngleoriginaltwo(Integer.parseInt(contentHexStr.substring(44, 48), 16) / 10);
		temp.setAnglethreadhold(Integer.parseInt(contentHexStr.substring(48, 52), 16) / 10);

		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		temp.setTime(new Date());

		int index = deviceReportAngleAbnormalMapper.insertSelective(temp);
		logger.info("onDevMessage01 insertSelective index:" + index);

		// 判断状态中数据是否正常，对设备进行回复
		onServiceResponse01(message, true);
	}

	private void onDevMessage09(DeviceMessage message) throws Exception {
		DeviceReportPowerConsumptionAbnormal temp = new DeviceReportPowerConsumptionAbnormal();
		temp.setImei(message.getImei());
		String contentHexStr = message.getContenthexstr();

		temp.setPowerconsumptionintegerpart(Integer.parseInt(contentHexStr.substring(30, 34), 16));
		temp.setPowerconsumptiondecimalpart(Integer.parseInt(contentHexStr.substring(34, 38), 16));

		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		temp.setTime(new Date());

		int index = deviceReportPowerConsumptionAbnormalMapper.insertSelective(temp);
		logger.info("onDevMessage01 insertSelective index:" + index);

		// 判断状态中数据是否正常，对设备进行回复
		onServiceResponse01(message, true);
	}

	private void onDevMessage0a(DeviceMessage message) throws Exception {
		DeviceReportHeartBeat temp = new DeviceReportHeartBeat();
		temp.setImei(message.getImei());
		String contentHexStr = message.getContenthexstr();

		// Date d=new Date();
		// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		temp.setTime(new Date());

		int index = deviceReportHeartBeatMapper.insertSelective(temp);
		logger.info("onDevMessage01 insertSelective index:" + index);

		// 判断状态中数据是否正常，对设备进行回复
		onServiceResponse01(message, true);
	}

	public DeviceMessage decodeMessage(Channel ch, byte[] msg) {
		DeviceMessage base = new DeviceMessage();
		String message = new String(msg).trim().toUpperCase(Locale.US);
		logger.info("decodeMessagerevrevererererre=====:" + message.toString());

		
		message=TextUtils.replaceBlank(message);
	    int index=message.indexOf("6F01");
	    int index2=message.indexOf("0D0A0D0A");	
	    if(index!=-1&&index2!=-1&&index2>index) {
		    message=message.substring(index, index2+8);
	    }else {
	    	return null;
	    }

		base.setChannel(ch);

		base.setHeadhexstr(message.substring(0, 4));
		logger.info("base.getHeadHexStr()=====:" + base.getHeadhexstr());

		base.setFlaghexstr(message.substring(4, 6));
		logger.info("base.getFlagHexStr()=====:" + base.getFlaghexstr());

		base.setControlhexstr(message.substring(6, 8));
		logger.info("base.getHeadHexStr()=====:" + base.getControlhexstr());

		base.setVersionhexstr(message.substring(8, 10));
		logger.info("base.getVersionHexStr()=====:" + base.getVersionhexstr());

		base.setContentlengthhexstr(message.substring(10, 14));
		logger.info("base.getContentLengthHexStr()=====:" + base.getContentlengthhexstr());

		Integer dataLength = Integer.valueOf(base.getContentlengthhexstr(), 16);
		logger.info("dataLength=====:" + dataLength);

		base.setContenthexstr(message.substring(14, dataLength * 2 + 14));
		logger.info("base.getControlHexStr()=====:" + base.getControlhexstr());

		base.setEndshexstr(message.substring(dataLength * 2 + 14, message.length()));
		logger.info("base.getEndsHexStr()=====:" + base.getEndshexstr());

		String imeiHexStr = base.getContenthexstr().substring(0, 30);
		logger.info("imeiHexStr=====:" + imeiHexStr);

		base.setImei(TextUtils.hexStr2AscIIStr(imeiHexStr));

		logger.info("base.getImei()=====:" + base.getImei());

		return base;
	}

	public void sendMsg(String textHexStr, Channel channel) {
		// Thread.sleep(2 * 1000);
		ByteBuf buf = channel.alloc().buffer();
		Charset charset = Charset.forName("UTF-8");
		buf.writeCharSequence(textHexStr, charset);
		channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				if (future.isSuccess()) {
					logger.info("sendMsg 发送成功   channel:" + channel + "  textHexStr:" + textHexStr);
				} else {
					logger.info("sendMsg 发送失败   channel:" + channel + "  textHexStr:" + textHexStr);

				}

			}
		});
	}

	@Async
	public void sendMsg(DeviceMessage toDeviceMessage) throws Exception {
		logger.info("sendToDevice thread==" + Thread.currentThread().getName());
		if (toDeviceMessage.getChannel() == null) {
			logger.info("服务器发送信息，设备不在线，将信息存入数据库，后续再发送");
			deviceMessageMapper.insertOrUpdate(toDeviceMessage);
			return;
		}
		sendMsg(toDeviceMessage.getHexStr(), toDeviceMessage.getChannel());
	}

	@Async
	public void messagesScheduledToSend() {
		long aa = System.currentTimeMillis();
		logger.info("activedevices count==" + DeviceServer.ALLCHANNELS_GROUP.size());
		List<String> activeimeis = DeviceServer.ALLCHANNELS_GROUP.getImeisArray();
		Iterator<String> iteratorImeis = activeimeis.iterator();

		List<DeviceMessage> messages = new ArrayList<DeviceMessage>();
		while (iteratorImeis.hasNext()) {
			String imei = iteratorImeis.next();
			messages.addAll(deviceMessageMapper.selectByImei(imei));
			if (messages.size() > 500) {
				break;
			}
		}
		messages.forEach(message -> {
			message.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(message.getImei()));
			message.setTime(new Date());
			try {
				sendMsg(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		//
		// List<DeviceMessage> messages = deviceMessageMapper.queryByLimit(0, 10);
		// Iterator<DeviceMessage> iterator = messages.iterator();
		// while (iterator.hasNext()) {
		// DeviceMessage message = iterator.next();
		// if (DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(message.getImei()) !=
		// null) {
		// message.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(message.getImei()));
		// message.setTime(new Date());
		// sendMsgAndReceiveResponse(message);
		// }
		//
		// }
		logger.info("sendToDevice thread==" + Thread.currentThread().getName() + "  时间："
				+ (System.currentTimeMillis() - aa));

	}

	@Async
	public BaseResponse sendMsgAndReceiveResponse(DeviceMessage toDeviceMessage) {
		logger.info("sendToDevice thread==" + Thread.currentThread().getName());
		long aa = System.currentTimeMillis();

		logger.info("服务器发送信息");

		try {
			sendMsg(toDeviceMessage);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.info("服务器发送信息失败，将信息存入数据库，后续再发送");
			deviceMessageMapper.insertOrUpdate(toDeviceMessage);
			return new BaseResponse(ErrorCode.SERVICE_SEND_ERROR, "服务器发送信息失败，将信息存入数据库，后续发送");

		}

		DeviceResponse response = null;
		response = getDeviceResponse(5000L, toDeviceMessage);
		if (response != null) {
			logger.info("服务器发送信息完毕，toDeviceMessage.getTime()==" + toDeviceMessage.getTime());
			logger.info("服务器发送信息完毕，response.getTime()==" + response.getTime());
		}

		if (response != null && response.getTime().compareTo(toDeviceMessage.getTime()) >= 0) {
			deviceMessageMapper.deleteByImeiAndControl(toDeviceMessage.getImei(), toDeviceMessage.getControlhexstr());
			logger.info("设置成功 发送接受信息时间：" + (System.currentTimeMillis() - aa) + "   当前时间" + System.currentTimeMillis());

			return BaseResponse.SUCCESS;

		} else if (response == null || response.getTime().before(toDeviceMessage.getTime())) {
			deviceMessageMapper.insertOrUpdate(toDeviceMessage);
			logger.info("务器发送信息，设备没有回复，将信息存入数据库，后续再发送 发送接受信息时间：" + (System.currentTimeMillis() - aa));

			return new BaseResponse(ErrorCode.DEVICE_RESPONSE_ERROR, "设备没有回复");
		} else {
			deviceMessageMapper.insertOrUpdate(toDeviceMessage);
			logger.info("务器发送信息，设备反馈信息无效，将信息存入数据库，后续再发送 发送接受信息时间：" + (System.currentTimeMillis() - aa));

			return new BaseResponse(ErrorCode.DEVICE_RESPONSE_ERROR, "设备反馈信息无效");
		}

	}

	public BaseResponse setDeviceSettingSwitch(String token, String devId, ServiceSettingsDeviceSwitch param)
			throws TokenInvalidException {
		ServiceSettingsDeviceSwitch test = param;
		test.setTime(new Date());
		serviceSettingsDeviceSwitchMapper.insertSelective(test);

		logger.info("setDeviceSettingSwitch getImei==" + test.getImei());

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		toDeviceMessage.setImei(test.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("11");
		toDeviceMessage.setContentlengthhexstr("0005");
		toDeviceMessage.setContenthexstr(ServiceSettingsDeviceSwitchToString(test));
		toDeviceMessage.setResponsecontrolhexstr("21");
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
		// TODO Auto-generated method stub
	}

	public String ServiceSettingsDeviceSwitchToString(ServiceSettingsDeviceSwitch test) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getSwitchcontrolone(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getSwitchcontroltwo(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getSwitchcontrolthree(), 1, "0"));
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		return stringBuffer.toString();

	}

	public String ServiceSettingsDeviceBrightnessToString(ServiceSettingsDeviceBrightness test) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getBrightnesscontrolone(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getBrightnesscontroltwo(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getBrightnesscontrolthree(), 1, "0"));
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		return stringBuffer.toString();

	}

	public String int2HexStringFormated(int number, int bytenum, String fill) {
		String st = Integer.toHexString(number).toUpperCase();
		st = String.format("%" + bytenum * 2 + "s", st);
		st = st.replaceAll(" ", fill);
		return st;
	}

	public DeviceResponse getDeviceResponse(Long waittime, DeviceMessage message) {
		synchronized (object) {
			try {
				logger.info("等待设备反馈信息 thread==" + Thread.currentThread().getName());
				object.wait(waittime);
				return deviceResponseMapper.findLastresponse(message.getImei(), message.getResponsecontrolhexstr());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public BaseResponse setDeviceSettingBrightness(String token, String devId, ServiceSettingsDeviceBrightness param) {
		// TODO Auto-generated method stub
		ServiceSettingsDeviceBrightness test = param;
		test.setTime(new Date());
		serviceSettingsDeviceBrightnessMapper.insertSelective(test);

		logger.info("setDeviceSettingSwitch getImei==" + test.getImei());

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		toDeviceMessage.setImei(test.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("12");
		toDeviceMessage.setContentlengthhexstr("0005");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getBrightnesscontrolone(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getBrightnesscontroltwo(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getBrightnesscontrolthree(), 1, "0"));
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse setDeviceSettingReportInterval(String token, String devId,
			ServiceSettingsDeviceReportInterval param) {
		// TODO Auto-generated method stub
		ServiceSettingsDeviceReportInterval test = param;
		test.setTime(new Date());
		serviceSettingsDeviceReportIntervalMapper.insertSelective(test);

		logger.info("setDeviceSettingSwitch getImei==" + test.getImei());

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		toDeviceMessage.setImei(test.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("13");
		toDeviceMessage.setContentlengthhexstr("0004");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getReportinterval(), 2, "0"));
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse setDeviceSettingStrategy(String token, String devId,
			ServiceSettingsDeviceLightingStrategy param) {
		// TODO Auto-generated method stub
		ServiceSettingsDeviceLightingStrategy test = param;
		test.setTime(new Date());
		serviceSettingsDeviceLightingStrategyMapper.insertSelective(test);

		logger.info("setDeviceSettingSwitch getImei==" + test.getImei());

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		toDeviceMessage.setImei(test.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("14");
		toDeviceMessage.setContentlengthhexstr(int2HexStringFormated(test.getTimenum() * 5 + 5 + 2, 2, "0"));

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getLightnum(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getStrategynum(), 2, "0"));
		stringBuffer.append(int2HexStringFormated(test.getStrategyperiod(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getTimenum(), 1, "0"));

		stringBuffer.append(test.getTimehex());
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse setDeviceSettingTime(String token, String devId, ServiceSettingsDeviceTime param) {
		// TODO Auto-generated method stub
		ServiceSettingsDeviceTime test = param;
		test.setTime(new Date());
		serviceSettingsDeviceTimeMapper.insertSelective(test);

		logger.info("setDeviceSettingSwitch getImei==" + test.getImei());

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		toDeviceMessage.setImei(test.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("15");
		toDeviceMessage.setContentlengthhexstr("0009");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getYear(), 2, "0"));
		stringBuffer.append(int2HexStringFormated(test.getMonth(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getDay(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getHour(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getMinute(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getSecond(), 1, "0"));

		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse setDeviceSettingReboot(String token, String devId) {
		// TODO Auto-generated method stub
		logger.info(" getImei==" + devId);

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		toDeviceMessage.setImei(devId);
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("16");
		toDeviceMessage.setContentlengthhexstr("0002");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse getDeviceSettingCellId(String token, String devId) {
		// TODO Auto-generated method stub
		logger.info(" getImei==" + devId);

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		toDeviceMessage.setImei(devId);
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("17");
		toDeviceMessage.setContentlengthhexstr("0002");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse getDeviceSettingSoftVersion(String token, String devId) {
		// TODO Auto-generated method stub
		logger.info(" getImei==" + devId);

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		toDeviceMessage.setImei(devId);
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("18");
		toDeviceMessage.setContentlengthhexstr("0002");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse getDeviceSettingHardVersion(String token, String devId) {
		// TODO Auto-generated method stub
		logger.info(" getImei==" + devId);

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		toDeviceMessage.setImei(devId);
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("19");
		toDeviceMessage.setContentlengthhexstr("0002");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse setDeviceSettingInstallationstateAnglethreadhold(String token, String devId,
			ServiceSettingsDeviceInstallationstateAnglethreadhold param) {
		// TODO Auto-generated method stub
		ServiceSettingsDeviceInstallationstateAnglethreadhold test = param;
		test.setTime(new Date());
		serviceSettingsDeviceInstallationstateAnglethreadholdMapper.insertSelective(test);

		logger.info("setDeviceSettingSwitch getImei==" + test.getImei());

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		toDeviceMessage.setImei(test.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("1a");
		toDeviceMessage.setContentlengthhexstr("0005");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getInstallationstable(), 1, "0"));
		stringBuffer.append(int2HexStringFormated(test.getAnglethreadhold() * 10, 2, "0"));

		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse getDeviceSettingPowerConsumption(String token, String devId) {
		// TODO Auto-generated method stub
		logger.info(" getImei==" + devId);

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(devId));

		toDeviceMessage.setImei(devId);
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("1b");
		toDeviceMessage.setContentlengthhexstr("0002");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

	public BaseResponse setDeviceSettingPowerConsumption(String token, String devId,
			ServiceSettingsDevicePowerConsumption param) {
		// TODO Auto-generated method stub
		ServiceSettingsDevicePowerConsumption test = param;
		test.setTime(new Date());
		serviceSettingsDevicePowerConsumptionMapper.insertSelective(test);

		logger.info("setDeviceSettingSwitch getImei==" + test.getImei());

		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="
				+ DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		toDeviceMessage.setImei(test.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("1c");
		toDeviceMessage.setContentlengthhexstr("0005");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getPowerconsumptionintegerpart(), 2, "0"));
		stringBuffer.append(int2HexStringFormated(test.getPowerconsumptiondecimalpart(), 1, "0"));

		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));
		toDeviceMessage.setContenthexstr(stringBuffer.toString());

		toDeviceMessage.setResponsecontrolhexstr(
				MyConstant.controlResponseControlHEXMap.get(toDeviceMessage.getControlhexstr()));
		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread==" + Thread.currentThread().getName());

		return sendMsgAndReceiveResponse(toDeviceMessage);
	}

}
