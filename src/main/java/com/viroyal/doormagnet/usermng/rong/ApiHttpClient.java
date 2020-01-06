package com.viroyal.doormagnet.usermng.rong;

import com.viroyal.doormagnet.usermng.rong.models.FormatType;
import com.viroyal.doormagnet.usermng.rong.models.SdkHttpResult;
import com.viroyal.doormagnet.usermng.rong.util.HttpUtil;

import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class ApiHttpClient {

    private static final String RONGCLOUDURI = "http://api.cn.ronghub.com";

    private static final String UTF8 = "UTF-8";

    // 获取token
    public static SdkHttpResult getToken(String appKey, String appSecret, String userId, String userName,
                                         String portraitUri, FormatType format) throws Exception {

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey, appSecret,
                RONGCLOUDURI + "/user/getToken." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        sb.append("&name=").append(URLEncoder.encode(userName == null ? "" : userName, UTF8));
        sb.append("&portraitUri=").append(URLEncoder.encode(portraitUri == null ? "" : portraitUri, UTF8));
        HttpUtil.setBodyParameter(sb, conn);

        return HttpUtil.returnResult(conn);
    }
}
