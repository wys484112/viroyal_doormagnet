package com.viroyal.doormagnet.devicemng.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.viroyal.doormagnet.devicemng.mapper.DeviceStatusMapper;
import com.viroyal.doormagnet.devicemng.model.DeviceStatus;

@Service
public class MessageDispatcher {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DeviceStatusMapper mDeviceStatusMapper;
	
	@Async
	public void handleMessage(DeviceMessage message) {
		onDevData(message);
	}

	private void onDevData(DeviceMessage message) {
    	logger.info("dispatch message imei:"+message.getImei()+"   message flag:"+message.getFlagHexStr()+"   message control flag:"+message.getControlHexStr());
		
    	switch (message.getFlagHexStr()) {
		//设备主动上报信息处理
		case "00":
			switch (message.getControlHexStr()) {
			case "01":
				
				break;

			default:
				break;
			}
			break;
		//设备回复信息处理			
		case "01":
			break;			
		default:
			break;
		}
    }
	
	private void onDevMessage01(DeviceMessage message) {
		DeviceStatus status=new DeviceStatus();
		status.setImei(message.getImei());
		String contentHexStr=message.getContentHexStr();
		
		
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
		status.setTime("20200117");

		
		
	}


}
