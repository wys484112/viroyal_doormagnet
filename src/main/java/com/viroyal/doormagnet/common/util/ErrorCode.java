package com.viroyal.doormagnet.common.util;

public interface ErrorCode {
    int SUCCESS = 1000;
    int INVALID_PARAM = 1001;
    int SYSTEM_BUSY = 1002;
    int INVALID_TOKEN = 1003;
    int SERVICE_SEND_ERROR = 1004;
    int DEVICE_RESPONSE_ERROR = 1005;

    
    int DEVICE_NOT_ACTIVATED = 2000;
    int DEVICE_ALREADY_BINDED = 2001;
    int DEVICE_NOT_BINDED = 2002;
}
