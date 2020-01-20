package com.viroyal.doormagnet.devicemng.socket;

import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.viroyal.doormagnet.devicemng.mapper.DeviceMessageMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceResponseMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceStatusMapper;
import com.viroyal.doormagnet.devicemng.model.DeviceMessage;
import com.viroyal.doormagnet.devicemng.model.DeviceResponse;
import com.viroyal.doormagnet.devicemng.model.DeviceStatus;
import com.viroyal.doormagnet.util.TextUtils;

import io.netty.channel.Channel;

@Service
public class MessageDispatcher {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DeviceStatusMapper mDeviceStatusMapper;

	@Autowired
	private DeviceResponseMapper deviceresponsemapper;
	
	@Autowired
	private DeviceMessageMapper devicemessagemapper;
	
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
		DeviceResponse response=new DeviceResponse();
		response.setImei(message.getImei());
		response.setControlhexstr(message.getControlhexstr());
		response.setErrorcode((byte) Integer.parseInt(message.getContenthexstr().substring(34, 36), 16));
		Thread.sleep(1000);
		response.setTime(new Date());
		
    	logger.info("onDevMessage21   设备回复信息存入数据库insert index:"+response.toString());        
    	logger.info("onDevMessage21   设备回复信息存入数据库 时间:"+response.getTime().toString());        

		DeviceResponse response1 = deviceresponsemapper.findLastresponse(response.getImei(),response.getControlhexstr());
		if(response!=null) {
			logger.info("服务器发送onDevMessage21完毕，response=="+response.getImei());
		}
		deviceresponsemapper.insert(response);
		
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
		DeviceServer.sendMsg(toDeviceMessage.toString(), toDeviceMessage.getChannel());
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

}
