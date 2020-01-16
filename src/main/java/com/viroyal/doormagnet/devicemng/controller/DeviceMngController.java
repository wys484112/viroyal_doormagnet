package com.viroyal.doormagnet.devicemng.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.viroyal.doormagnet.devicemng.entity.Device;
import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.model.DeviceStatus;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;
import com.viroyal.doormagnet.devicemng.pojo.BindReqParam;
import com.viroyal.doormagnet.devicemng.pojo.DataListResponse;
import com.viroyal.doormagnet.devicemng.service.IDeviceMng;
import com.viroyal.doormagnet.devicemng.socket.DeviceServer;
import com.viroyal.doormagnet.devicemng.socket.IDeviceServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device/")
public class DeviceMngController {
    private static final Logger logger = LoggerFactory.getLogger(DeviceMngController.class);

    @Autowired
    private IDeviceMng mDeviceMng;

    @Autowired
    private JPushClient mJPushClient;
    

    
    @RequestMapping(value = "/push_test", method = RequestMethod.GET)
    public String pushTest(@RequestParam("user_id") String userId, @RequestParam("alert") String alert) {
        String tag = "user_" + userId;
        //alert = "您所使用的设备（" + "abc" + "）产生了警报，请及时登录APP查看详细信息";

        final PushPayload payload = buildPushObject(tag, 8513, alert);
        sendJPush(payload);
        return "push ok";
    }

    private PushPayload buildPushObject(String tag, int devId, String alert) {
        Map<String, String> extras = new HashMap<String, String>();
        extras.put("dev_id", "" + devId);
        extras.put("extra_2", "val2");

        PushPayload.Builder builder = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.tag(tag))
                .setNotification(Notification.newBuilder()
                        .setAlert(alert)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .addExtras(extras).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtras(extras).build())
                        .build());
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
        }
    }

    @PostMapping("v1/bind")
    public BaseResponse bind(@RequestHeader("token") String token, @RequestBody BindReqParam param) throws TokenInvalidException {
        return mDeviceMng.bind(token, param);
    }

    @PostMapping("v1/{id}/unbind")
    public BaseResponse unbind(@RequestHeader("token") String token, @PathVariable("id") int devId) throws TokenInvalidException {
        return mDeviceMng.unbind(token, devId);
    }

    @GetMapping("v1/list")
    public BaseResponse getDeviceList(@RequestHeader("token") String token) throws TokenInvalidException {
        return mDeviceMng.getDeviceList(token);
    }
    
	@GetMapping("v1/{id}/devicestatus")
	public BaseResponse setDeviceStatus(@RequestHeader("token") String token, @PathVariable("id") String devId)
			throws TokenInvalidException {
		return mDeviceMng.getDeviceStatus(token, devId);
	}
    
    
    @PutMapping("v1/{id}/settings")
    public BaseResponse setDeviceSettingSwitch(@RequestHeader("token") String token, @PathVariable("id") int devId,
                                       @RequestBody DeviceSetting param) throws TokenInvalidException {
        return mDeviceMng.setDeviceSetting(token, devId, param);
    }
    

    @GetMapping("v1/{id}/settings")
    public BaseResponse getDeviceSetting(@RequestHeader("token") String token, @PathVariable("id") int devId) throws TokenInvalidException {
        return mDeviceMng.getDeviceSetting(token, devId);
    }

    @GetMapping("v1/{id}/alert")
    public BaseResponse getDeviceAlert(@RequestHeader("token") String token, @PathVariable("id") int devId,
                                       @RequestParam("next_id") int nextId) throws TokenInvalidException {
        return mDeviceMng.getDeviceAlert(token, devId, nextId);
    }




    
    
}
