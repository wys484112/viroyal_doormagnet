package com.viroyal.doormagnet.devicemng.socket;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.mapper.DeviceMessageMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceResponseMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceStatusMapper;
import com.viroyal.doormagnet.devicemng.mapper.ServiceSettingsDeviceSwitchMapper;
import com.viroyal.doormagnet.devicemng.model.DeviceMessage;
import com.viroyal.doormagnet.devicemng.model.DeviceResponse;
import com.viroyal.doormagnet.devicemng.model.DeviceStatus;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceSwitch;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;
import com.viroyal.doormagnet.util.ErrorCode;
import com.viroyal.doormagnet.util.TextUtils;

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
	private DeviceResponseMapper deviceresponsemapper;
	
	@Autowired
	private DeviceMessageMapper devicemessagemapper;
	
    @Autowired
    private DeviceMessageMapper deviceMessageMapper;
    
    @Autowired
    private DeviceResponseMapper deviceResponseMapper;
    
    @Autowired
    private  ServiceSettingsDeviceSwitchMapper serviceSettingsDeviceSwitchMapper;
    
    final Object object =new Object();

    
	@Async
	public void handleMessage(Channel ch, byte[] msg) {
		try {
			logger.info("handleMessage thread=="+Thread.currentThread().getName());            
			
	        DeviceMessage message = decodeMessage(ch, (byte[]) msg);
	    	DeviceServer.ALLCHANNELS_GROUP.add(message);            	
			onDevData(message);
		} catch (Exception e) {
			// TODO: handle exception
            logger.error("error, handleMessage channel=" + ch + ", error=" + e.getMessage());
		}

	}

	//6F010001 01  383637373235303330303935353738 0064  03E8 0003E8 0003E8 50 32 03E8 38 04 04 05 00 01 00 64 01 03 04B0 04B0 04B0 04B0 0D0A0D0A
	
	private void onDevData(DeviceMessage message) throws Exception {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    	logger.info("dispatch message imei:"+message.getImei()+"   message flag:"+message.getFlaghexstr()+"   message control flag:"+message.getControlhexstr());        
    	switch (message.getFlaghexstr()) {
		//设备主动上报信息处理
		case "00":
			switch (message.getControlhexstr()) {
			case "01":
				onDevMessage01(message);
				test(message);
				break;

			default:
				break;
			}
			break;
		//设备回复信息处理			
		case "01":
			switch (message.getControlhexstr()) {
			case "21":
				onDevMessage21(message);
				break;

			default:
				break;
			}
			
			break;			
		default:
			break;
		}
    }
	
	private void test(DeviceMessage message) {
		DeviceMessage toDeviceMessage=new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(message.getImei()));
		toDeviceMessage.setImei(message.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("11");
		toDeviceMessage.setContentlengthhexstr("0005");
		toDeviceMessage.setContenthexstr("0101013131");
		toDeviceMessage.setResponsecontrolhexstr("21");
		toDeviceMessage.setTime(new Date());
		
		devicemessagemapper.insertOrUpdate(toDeviceMessage);
	}

	private void onDevMessage21(DeviceMessage message) throws Exception {
		DeviceResponse response = new DeviceResponse();
		response.setImei(message.getImei());
		response.setControlhexstr(message.getControlhexstr());
		response.setErrorcode((byte) Integer.parseInt(message.getContenthexstr().substring(34, 36), 16));
		Thread.sleep(1000);
		response.setTime(new Date());

		deviceresponsemapper.insert(response);
		logger.info("服务器发送onDevMessage21完毕，response==" + response.getImei());

	}
	
	
	//6F01000101002F383637373235303330303935353738006403E80003E80003E8503203E83804040500010064010304B004B004B004B00D0A0D0A	
	private void onDevMessage01(DeviceMessage message) throws Exception {
		DeviceStatus status=new DeviceStatus();
		status.setImei(message.getImei());
		String contentHexStr=message.getContenthexstr();
		
		
		String voltage=contentHexStr.substring(30, 34);//2
		String current=contentHexStr.substring(34, 38);//2
		String activepower=contentHexStr.substring(38, 44);//3
		String reactivepower=contentHexStr.substring(44, 50);//3
		String powerfactor=contentHexStr.substring(50, 52);//1
		String temperature=contentHexStr.substring(52, 54);//1
		String powerconsumptionintegerpart=contentHexStr.substring(54, 58);//2
		String powerconsumptiondecimalpart=contentHexStr.substring(58, 60);//1
		String brightnesscontrolone=contentHexStr.substring(60, 62);//1
		String brightnesscontroltwo=contentHexStr.substring(62, 64);//1
		String brightnesscontrolthree=contentHexStr.substring(64, 66);//1
		String switchcontrolone=contentHexStr.substring(66, 68);//1
		String switchcontroltwo=contentHexStr.substring(68, 70);//1
		String switchcontrolthree=contentHexStr.substring(70, 72);//1
		String signalstrengthabsolutevalue=contentHexStr.substring(72, 74);//1
		String dimmingmode=contentHexStr.substring(74, 76);//1
		String abnormalflag=contentHexStr.substring(76, 78);//1
		String angleone=contentHexStr.substring(78, 82);//2
		String angletwo=contentHexStr.substring(82, 86);//2
		String angleoriginalone=contentHexStr.substring(86, 90);//2
		String angleoriginaltwo=contentHexStr.substring(90, 94);//2
		
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
//		Date d=new Date();
//        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  

		status.setTime(new Date());

		int index= mDeviceStatusMapper.insertSelective(status);
    	logger.info("onDevMessage01 insertSelective index:"+index);        

		//判断状态中的电流电源 等数据是否正常，对设备进行回复
    	onServiceResponse01(message,true);		
	}
	private void onServiceResponse01(DeviceMessage message,Boolean isRight) throws Exception {
		DeviceMessage toDeviceMessage = new DeviceMessage();
		toDeviceMessage.setChannel(message.getChannel());
		toDeviceMessage.setFlaghexstr("01");
		toDeviceMessage.setControlhexstr("01");
		toDeviceMessage.setContentlengthhexstr("0001");
		toDeviceMessage.setContenthexstr(isRight?"00":"01");
		sendMsg(toDeviceMessage.toString(), toDeviceMessage.getChannel());
	}

	
	

    public DeviceMessage decodeMessage(Channel ch, byte[] msg) {
    	DeviceMessage base =new DeviceMessage();
    	String message=new String(msg).trim().toUpperCase(Locale.US);
        logger.info("decodeMessagerevrevererererre=====:"+message.toString());

    	base.setChannel(ch);
    	
    	base.setHeadhexstr(message.substring(0, 4));
        logger.info("base.getHeadHexStr()=====:"+base.getHeadhexstr());
    	
    	base.setFlaghexstr(message.substring(4, 6));
        logger.info("base.getFlagHexStr()=====:"+base.getFlaghexstr());
    	
    	base.setControlhexstr(message.substring(6, 8));
        logger.info("base.getHeadHexStr()=====:"+base.getControlhexstr());
    	
    	base.setVersionhexstr(message.substring(8, 10));
        logger.info("base.getVersionHexStr()=====:"+base.getVersionhexstr());

    	base.setContentlengthhexstr(message.substring(10, 14));
        logger.info("base.getContentLengthHexStr()=====:"+base.getContentlengthhexstr());

        Integer dataLength = Integer.valueOf(base.getContentlengthhexstr(),16);
        logger.info("dataLength=====:"+dataLength);

    	base.setContenthexstr(message.substring(14, dataLength*2+14));
        logger.info("base.getControlHexStr()=====:"+base.getControlhexstr());

    	base.setEndshexstr(message.substring(dataLength*2+14, message.length()));
        logger.info("base.getEndsHexStr()=====:"+base.getEndshexstr());

    	String imeiHexStr=base.getContenthexstr().substring(0, 30);
        logger.info("imeiHexStr=====:"+imeiHexStr);
    	
    	base.setImei(TextUtils.hexStr2AscIIStr(imeiHexStr));

        logger.info("base.getImei()=====:"+base.getImei());

    	return base;
    }

    
    public void sendMsg(String textHexStr,Channel channel) {
		// Thread.sleep(2 * 1000);
		ByteBuf buf = channel.alloc().buffer();
		Charset charset = Charset.forName("UTF-8");
		buf.writeCharSequence(textHexStr, charset);
		channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				if(future.isSuccess()) {
			    	logger.info("sendMsg 发送成功   channel:"+channel +"  textHexStr:"+textHexStr);        					
				}else {
			    	logger.info("sendMsg 发送失败   channel:"+channel +"  textHexStr:"+textHexStr);        
					
				}

			}
		});
	}
    
    public void sendMsg(DeviceMessage toDeviceMessage) throws Exception{
 		if(toDeviceMessage.getChannel()==null) {
 			logger.info("服务器发送信息，设备不在线，将信息存入数据库，后续再发送");
 			deviceMessageMapper.insertOrUpdate(toDeviceMessage);
 			return;
 		} 		
 		sendMsg(toDeviceMessage.getHexStr(), toDeviceMessage.getChannel()); 		
    }
    
    @Async
	public void messagesScheduledToSend() {
		long aa = System.currentTimeMillis();
		logger.info("activedevices count=="+DeviceServer.ALLCHANNELS_GROUP.size());
		List<DeviceMessage> messages = deviceMessageMapper.queryByLimit(0, 1000);
		Iterator<DeviceMessage> iterator = messages.iterator();
		while (iterator.hasNext()) {
			DeviceMessage message = iterator.next();
			if (DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(message.getImei()) != null) {
				message.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(message.getImei()));
				message.setTime(new Date());
				sendMsgAndReceiveResponse(message);
			}

		}
		logger.info("thread==" + Thread.currentThread().getName() + "  时间："
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
		try {
			response = getDeviceResponse(5000L, toDeviceMessage).get();
			if (response != null) {
				logger.info("服务器发送信息完毕，toDeviceMessage.getTime()==" + toDeviceMessage.getTime());
				logger.info("服务器发送信息完毕，response.getTime()==" + response.getTime());
			}

			if (response != null && response.getTime().compareTo(toDeviceMessage.getTime()) >= 0) {
				deviceMessageMapper.deleteByImeiAndControl(toDeviceMessage.getImei(),
						toDeviceMessage.getControlhexstr());
				logger.info("设置成功 发送接受信息时间：" + (System.currentTimeMillis() - aa));

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

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		deviceMessageMapper.insertOrUpdate(toDeviceMessage);
		logger.info("服务器发送信息，设备没有回复，将信息存入数据库，后续再发送 发送接受信息时间：" + (System.currentTimeMillis() - aa));
		return new BaseResponse(ErrorCode.DEVICE_RESPONSE_ERROR, "设备没有回复");
	}
    
	public BaseResponse setDeviceSettingSwitch(String token, String devId, ServiceSettingsDeviceSwitch param)
			throws TokenInvalidException {
		ServiceSettingsDeviceSwitch test=param;
		test.setTime(new Date());
		serviceSettingsDeviceSwitchMapper.insertSelective(test);
		
		logger.info("setDeviceSettingSwitch getImei=="+test.getImei());
		
		DeviceMessage toDeviceMessage=new DeviceMessage();
		toDeviceMessage.setChannel(DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		logger.info("setDeviceSettingSwitch getChannelFromImei=="+DeviceServer.ALLCHANNELS_GROUP.getChannelFromImei(test.getImei()));

		
		toDeviceMessage.setImei(test.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("11");
		toDeviceMessage.setContentlengthhexstr("0005");
		toDeviceMessage.setContenthexstr(ServiceSettingsDeviceSwitchToString(test));
		toDeviceMessage.setResponsecontrolhexstr("21");
 		toDeviceMessage.setTime(new Date());
		logger.info("setDeviceSettingSwitch thread=="+Thread.currentThread().getName());
		
		return  sendMsgAndReceiveResponse(toDeviceMessage);
		// TODO Auto-generated method stub
	}
	
	public String   ServiceSettingsDeviceSwitchToString(ServiceSettingsDeviceSwitch test) {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getSwitchcontrolone(),1,"0"));
		stringBuffer.append(int2HexStringFormated(test.getSwitchcontroltwo(),1,"0"));
		stringBuffer.append(int2HexStringFormated(test.getSwitchcontrolthree(),1,"0"));
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));		
		return stringBuffer.toString();
		
	}
	
	public  String  int2HexStringFormated(int number,int bytenum,String fill) {
	      String st = Integer.toHexString(number).toUpperCase();
	      st = String.format("%"+bytenum*2+"s",st);
	      st= st.replaceAll(" ",fill);
	      return st;
	}
	
    @Async
    public ListenableFuture<DeviceResponse> getDeviceResponse(Long waittime,DeviceMessage message) {
        
		synchronized (object) {
			try {
				logger.info("等待设备反馈信息 thread=="+Thread.currentThread().getName());
				object.wait(waittime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		    	
    	return new AsyncResult<DeviceResponse>(deviceResponseMapper.findLastresponse(message.getImei(),
    			message.getResponsecontrolhexstr()));
    }
    
}
