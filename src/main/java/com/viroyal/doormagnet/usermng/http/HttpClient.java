package com.viroyal.doormagnet.usermng.http;

import com.viroyal.doormagnet.usermng.pojo.MessageBean;
import com.viroyal.doormagnet.usermng.pojo.MessageResponse;
import com.viroyal.doormagnet.usermng.pojo.MessageSendParam;
import com.viroyal.doormagnet.usermng.pojo.MessageVerifyParam;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;


/**
 * @author: Created by qinyl.
 * @date: 2017/6/19.
 * @comments:
 */
public interface HttpClient {
    @POST("/sms/auth/send")
    MessageResponse sendMessage(@Header(value = "Authorization") String appName,
                                @Header(value = "app_key") String appKey,
                                @Header(value = "app_secret") String appSecret,
                                @Body MessageSendParam param);

    @POST("/sms/auth/verify")
    MessageResponse verifyMessage(@Header(value = "Authorization") String appName,
                                  @Header(value = "app_key") String appKey,
                                  @Header(value = "app_secret") String appSecret,
                                  @Body MessageVerifyParam param);

    class Builder {
        public static HttpClient messageHttpClient() {
            return new RestAdapterConfig().messageRestAdapter().create(HttpClient.class);
        }
    }

    class RestAdapterConfig {
        public RestAdapter messageRestAdapter() {
            return new RestAdapter.Builder().setEndpoint("http://139.196.174.56:20480").build();
        }
    }
}
