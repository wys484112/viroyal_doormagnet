package com.viroyal.doormagnet.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private ObjectMapper mJsonMapper;

    private static JsonUtil mInstance;

    private JsonUtil() {
        mJsonMapper = new ObjectMapper();
        mJsonMapper.setSerializationInclusion(Include.NON_NULL);
        mJsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mJsonMapper.setPropertyNamingStrategy(SNAKE_CASE);
    }

    private static JsonUtil getInstance() {
        if (mInstance == null) {
            mInstance = new JsonUtil();
        }
        return mInstance;
    }

    /**
     * 反序列化
     * 
     * @param devId
     *            用于错误时在log中输出
     * @param value
     * @param valueType
     * @return
     */
    public static <T> T readObject(int devId, String value, Class<T> valueType) {
        JsonUtil util = getInstance();
        try {
            T obj = util.mJsonMapper.readValue(value, valueType);
            return obj;
        } catch (Exception e) {
            util.logger.error("bad json message from dev:" + devId + " content is:" + value);
            util.logger.error("json parser error:" + e.getMessage());

            return null;
        }
    }

    /**
     * 反序列化
     * 
     * @param value
     * @param valueType
     * @return
     */
    public static <T> T readObject(String value, Class<T> valueType) {
        JsonUtil util = getInstance();
        try {
            T obj = util.mJsonMapper.readValue(value, valueType);
            return obj;
        } catch (Exception e) {
            util.logger.error("bad json message :" + value);
            util.logger.error("json parser error:" + e.getMessage());

            return null;
        }
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
