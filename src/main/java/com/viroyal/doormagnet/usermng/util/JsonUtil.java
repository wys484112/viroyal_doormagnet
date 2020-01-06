package com.viroyal.doormagnet.usermng.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings(value = "unused")
public class JsonUtil {
    private static JsonUtil mInstance;
    private final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private ObjectMapper mJsonMapper;

    private JsonUtil() {
        mJsonMapper = new ObjectMapper();
        mJsonMapper.setSerializationInclusion(Include.NON_NULL);
    }

    private static JsonUtil getInstance() {
        if (mInstance == null) {
            mInstance = new JsonUtil();
        }
        return mInstance;
    }

    public static <T> T readObject(String value, Class<T> valueType) {
        JsonUtil util = getInstance();
        try {
            T obj = util.mJsonMapper.readValue(value, valueType);
            return obj;
        } catch (Exception e) {
            util.logger.error("bad json message :" + value);
            util.logger.error("json parser error:" + e.getMessage());
        }
        return null;
    }

    public static String writeObject(Object obj) {
        JsonUtil util = getInstance();
        try {
            return util.mJsonMapper.writeValueAsString(obj);
        } catch (Exception e) {
            util.logger.error("json write error:" + e.getMessage());
        }
        return null;
    }

}
