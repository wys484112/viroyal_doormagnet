package com.viroyal.doormagnet.devicemng.service.impl;

import com.viroyal.doormagnet.devicemng.entity.Device;
import com.viroyal.doormagnet.devicemng.entity.DeviceOpLog;
import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import com.viroyal.doormagnet.devicemng.entity.UserEntity;
import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.mapper.DeviceStatusMapper;
import com.viroyal.doormagnet.devicemng.model.DeviceStatus;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceSwitch;
import com.viroyal.doormagnet.devicemng.pojo.*;
import com.viroyal.doormagnet.devicemng.service.IDeviceMng;
import com.viroyal.doormagnet.devicemng.socket.IDeviceServer;
import com.viroyal.doormagnet.util.ErrorCode;
import com.viroyal.doormagnet.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class DeviceMngImpl implements IDeviceMng {
    private static final Logger logger = LoggerFactory.getLogger(DeviceMngImpl.class);
    private static final int PAGE_SIZE = 20; //数据分页大小

    
    @Autowired
    private IDeviceServer mIDeviceServer;

    
	@Autowired
	private DeviceStatusMapper mDeviceStatusMapper;
    /**
     * 设备绑定
     *
     * @param token 用户token
     * @param param 绑定请求参数
     * @return BaseResponse
     * @throws TokenInvalidException exception
     */
    @Override
    public BaseResponse bind(String token, BindReqParam param) throws TokenInvalidException {
//        UserEntity userEntity = checkTokenEx(token);
//        // 检查设备是否存在
//        Device device = mDeviceMapper.findBySn(param.sn);
//        if (device == null) {
//            return BaseResponse.getInvalidParamResponse("无效的序列号");
//        }
//        //设备激活了吗？
//        if (device.status != Device.STATUS_ACTIVATED) {
//            logger.info("device not register, status=" + device.status);
//            return new BaseResponse(ErrorCode.DEVICE_NOT_ACTIVATED, "该设备未注册");
//        }
//        //是否绑定过了?
//        int devId = device.id;
//        /*
//        int bindCount = mDeviceMapper.getBindCount(userId, devId);
//        if (bindCount > 0) {
//            return new BaseResponse(ErrorCode.DEVICE_ALREADY_BINDED, "您已经绑定过此设备");
//        }
//        */
//        //保存绑定关系
//        try {
//            String devName = param.name;
//            if (TextUtils.isEmpty(devName)) {
//                devName = device.name;
//            }
//            mDeviceMapper.insertBindInfo(userEntity.id, devId, devName);
//        } catch (DuplicateKeyException e) {
//            return new BaseResponse(ErrorCode.DEVICE_ALREADY_BINDED, "您已经绑定过此设备");
//        }
//        //写操作log
//        DeviceOpLog logEntity = new DeviceOpLog();
//        logEntity.devId = device.id;
//        logEntity.action = DeviceOpLog.ACTION_BINDED;
//        logEntity.actionTime = new Timestamp(System.currentTimeMillis());
//        logEntity.msg = "用户" + userEntity.phone + "_" + userEntity.id + "绑定设备";
//        mDeviceOpLogMapper.insertOne(logEntity);

        return BaseResponse.SUCCESS;
    }

    /**
     * 解除绑定关系
     * 1 删除绑定表记录
     * @param token user token
     * @param devId device id
     * @return response
     * @throws TokenInvalidException
     */
    @Override
    public BaseResponse unbind(String token, int devId) throws TokenInvalidException {
//        UserEntity userEntity = checkTokenEx(token);
//
//        int count = mDeviceMapper.deleteBindInfo(userEntity.id, devId);
//        //写操作log
//        DeviceOpLog logEntity = new DeviceOpLog();
//        logEntity.devId = devId;
//        logEntity.action = DeviceOpLog.ACTION_UN_BINDED;
//        logEntity.actionTime = new Timestamp(System.currentTimeMillis());
//        logEntity.msg = "用户" + userEntity.phone + "_" + userEntity.id + "解绑设备";
//        mDeviceOpLogMapper.insertOne(logEntity);
//
//        if (count == 0) {
//            return new BaseResponse(ErrorCode.DEVICE_NOT_ACTIVATED, "您没有绑定该设备");
//        } else {
//            return BaseResponse.SUCCESS;
//        }
        return BaseResponse.SUCCESS;

    }

    /**
     * 获取用户名下的设备列表
     * @param token 用户token
     * @return BaseResponse
     * @throws TokenInvalidException exception
     */
    @Override
    public BaseResponse getDeviceList(String token) throws TokenInvalidException {
//        long userId = checkToken(token);

//        List<BindListRsp> list = mDeviceMapper.getBindList(userId);

        return new DataListResponse(mIDeviceServer.getDeviceActiveList());        
    }

    /**
     * 获取用户名下的设备列表
     * @param token 用户token
     * @return BaseResponse
     * @throws TokenInvalidException exception
     */
    @Override
    public BaseResponse getDeviceListActive(String token) throws TokenInvalidException {
//        long userId = checkToken(token);

//        List<BindListRsp> list = mDeviceMapper.getBindList(userId);

        return new DataListResponse(mIDeviceServer.getDeviceActiveList());        
    }
    /**
     * 获取设备的设置值
     * @param token 用户token
     * @param devId 设备id
     * @return BaseResponse
     * @throws TokenInvalidException exception
     */
    @Override
    public BaseResponse getDeviceSetting(String token, int devId) throws TokenInvalidException {
//        long userId = checkToken(token);
//
//        DeviceSettingRsp rsp = mDeviceMapper.getDeviceSetting(userId, devId);
//        if (rsp == null) {
//            return new BaseResponse(ErrorCode.DEVICE_NOT_BINDED, "您没有绑定该设备");
//        }
//
//        return new ExtraResponse(rsp);
        return BaseResponse.SUCCESS;

    }

    /**
     * 获取设备的告警记录
     *
     * 从最新的开始返回，分页加载
     *
     * @param token 用户token
     * @param devId 设备id
     * @return 列表
     * @throws TokenInvalidException exception
     */
    @Override
    public BaseResponse getDeviceAlert(String token, int devId, int nextId) throws TokenInvalidException {
//        long userId = checkToken(token);
//
//        //在t_device_op_log中查询action为1,2, 6的记录
//        if (nextId < 0) {
//            nextId = 0;
//        }
//        int offset = nextId * PAGE_SIZE;
//        int limit  = PAGE_SIZE + 1;
//        List<DeviceAlert> list;
//        if (devId == 0) {
//            list = mDeviceMapper.getDeviceAlert(userId, limit, offset);
//        } else {
//            list = mDeviceMapper.getDeviceAlertByDeviceId(userId, devId, limit, offset);
//        }
//
//        if (list != null && list.size() == limit) {
//            list.remove(limit -1);
//            nextId++;
//        } else {
//            nextId = -1;
//        }
//
//        return new DataListResponse(list, nextId);
        return BaseResponse.SUCCESS;

    }

    @Override
    public BaseResponse setDeviceSetting(String token, int devId, DeviceSetting param) throws TokenInvalidException {
//        long userId = checkToken(token);
//        //有绑定关系吗？
//        int bindCount = mDeviceMapper.getBindCount(userId, devId);
//        if (bindCount == 0) {
//            return new BaseResponse(ErrorCode.DEVICE_NOT_BINDED, "您没有绑定该设备");
//        }
//        param.devId = devId;
//        param.userId = userId;
//        mDeviceMapper.updateDeviceSetting(param);
//        //修改t_bind_info里面的字段
//        if (param.name != null || param.appPush != null || param.pushSlient != null
//                || param.slientStart != null || param.slientEnd != null ) {
//            mDeviceMapper.updateBindSetting(userId, devId, param);
//        }

        return BaseResponse.SUCCESS;
    }

    /**
     * 根据token获取用户信息
     * @param token user token
     * @return user id
     */
    private long checkToken(String token) throws TokenInvalidException {
//        Long userId = mDeviceMapper.getUserIdByToken(token);
//        if (userId == null) {
//            throw new TokenInvalidException(token);
//        }
//        return userId;
    	return 0L;
    }

    /**
     * 根据token获取用户信息
     * @param token user token
     * @return UserEntity 用户信息，目前只是手机号
     */
    private UserEntity checkTokenEx(String token) throws TokenInvalidException {
//        UserEntity user = mDeviceMapper.findUserByToken(token);
//        if (user == null) {
//            throw new TokenInvalidException(token);
//        }
//        return user;
    	return null;
    }

	@Override
	public BaseResponse setDeviceSettingSwitch(String token, String devId, ServiceSettingsDeviceSwitch param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse setDeviceSettingBrightness(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse setDeviceSettingReportInterval(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse setDeviceSettingStrategy(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse setDeviceSettingTime(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse setDeviceSettingReboot(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse getDeviceSettingCellId(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse getDeviceSettingSoftVersion(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse getDeviceSettingHardVersion(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse setDeviceSettingMountAndAngularThreshold(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse getDeviceSettingPowerConsumption(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse setDeviceSettingPowerConsumption(String token, String devId, DeviceSetting param)
			throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse setDeviceStatus(String token, String devId, DeviceStatus param) throws TokenInvalidException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse getDeviceStatusList(String token, String imei) throws TokenInvalidException {
		// TODO Auto-generated method stub      
        return new DataListResponse(mDeviceStatusMapper.selectByImei(imei));        
	}
	
}
