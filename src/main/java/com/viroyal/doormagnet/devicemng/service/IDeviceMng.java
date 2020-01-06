package com.viroyal.doormagnet.devicemng.service;

import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;
import com.viroyal.doormagnet.devicemng.pojo.BindReqParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IDeviceMng {
    BaseResponse bind(String token, BindReqParam param) throws TokenInvalidException;

    BaseResponse unbind(String token, int devId) throws TokenInvalidException;

    BaseResponse getDeviceList(String token) throws TokenInvalidException;

    BaseResponse getDeviceSetting(String token, int devId) throws TokenInvalidException;

    BaseResponse getDeviceAlert(String token, int devId, int nextId) throws TokenInvalidException;

    BaseResponse setDeviceSetting(String token, int devId, DeviceSetting param) throws TokenInvalidException;
}
