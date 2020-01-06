package com.viroyal.doormagnet.usermng.util;


import com.viroyal.doormagnet.usermng.pojo.BaseResponse;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoteUtil {
    private final static Logger logger = LoggerFactory.getLogger(RemoteUtil.class);

    private final static String CHARSET = "UTF-8";

    private final static String MEDIATYPE = "application/json; charset=utf-8";

    private final static String MEDIATYPE2 = "application/x-www-form-urlencoded;charset=utf-8;";

    private final static int CONN_TIMEOUT = 30000;

    private final static int READ_TIMEOUT = 30000;

    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    public static BaseResponse doPost(final boolean isSSL, final String url, final String auth, final String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
        httpHeaders.add("Authorization", auth);
        httpHeaders.setContentType(MediaType.parseMediaType(MEDIATYPE));

        RequestEntity<String> formEntity = new RequestEntity<String>(body, httpHeaders, null, null);
        CloseableHttpClient httpClient = isSSL ? createSSLClientDefault() : HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(httpFactory);
        ResponseEntity<BaseResponse> jsonResponse = restTemplate.postForEntity(url, formEntity, BaseResponse.class);
        BaseResponse result = jsonResponse.getBody();
        logger.info("doPost: restTemplate post: result: " + result);

        return result;
    }

    public static <T> T doPost(Class<T> clazz, final boolean isSSL, final String url, final String auth, final String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
        httpHeaders.add("Authorization", auth);
        httpHeaders.setContentType(MediaType.parseMediaType(MEDIATYPE));

        RequestEntity<String> formEntity = new RequestEntity<String>(body, httpHeaders, null, null);
        CloseableHttpClient httpClient = isSSL ? createSSLClientDefault() : HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(httpFactory);
        ResponseEntity<T> jsonResponse = restTemplate.postForEntity(url, formEntity, clazz);
        T result = jsonResponse.getBody();
        logger.info("doPost: restTemplate post: result: " + result);

        return result;
    }

    public static String doPost2(final boolean isSSL, final String url, final String server, final String auth, final String body) {
        CloseableHttpClient httpClient = isSSL ? createSSLClientDefault() : HttpClients.createDefault();

        String result = null;
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-Type", MEDIATYPE);
            httppost.setHeader("Authorization", auth);

            BasicHttpEntity requestBody = new BasicHttpEntity();
            requestBody.setContent(new ByteArrayInputStream(body.getBytes(CHARSET)));
            requestBody.setContentLength(body.getBytes(CHARSET).length);
            httppost.setEntity(requestBody);

            CloseableHttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse == null) {
                logger.error("doPost2: string result http response null!");
            }

            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity == null) {
                logger.error("doPost2: string result http entity null!");
            }
            result = EntityUtils.toString(httpEntity, CHARSET);
            EntityUtils.consume(httpEntity);
            logger.info(result);
        } catch (ClientProtocolException e) {
            logger.error("doPost2: string DefaultHttpClient execute ClientProtocolException!");
        } catch (IOException e) {
            logger.error("doPost2: string DefaultHttpClient execute IOException!");
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("doPost2: string DefaultHttpClient close IOException!");
            }
        }

        return result;
    }

    public static String doPost2(final boolean isSSL, final String url, final String server, final String auth, final Map<String, String> maps) {
        CloseableHttpClient httpClient = isSSL ? createSSLClientDefault() : HttpClients.createDefault();

        String result = null;
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Accept", "application/json; charset=utf-8");
            httppost.setHeader("Content-Type", MEDIATYPE2);

            if (maps != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : maps.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                httppost.setEntity(new UrlEncodedFormEntity(paramList, CHARSET));
            }

            CloseableHttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse == null) {
                logger.error("doPost2: maps result http response null!");
            }

            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity == null) {
                logger.error("doPost2: maps result http entity null!");
            }
            result = EntityUtils.toString(httpEntity, CHARSET);
            EntityUtils.consume(httpEntity);
            logger.info("doPost2: maps result: " + result);
        } catch (ClientProtocolException e) {
            logger.error("doPost2: maps DefaultHttpClient execute ClientProtocolException!");
        } catch (IOException e) {
            logger.error("doPost2: maps DefaultHttpClient execute IOException!");
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("doPost2: maps DefaultHttpClient close IOException!");
            }
        }

        return result;
    }

    public static String doGet(final String strUrl, final Map<String, Object> maps) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        String result = null;
        try {
            StringBuffer sb = new StringBuffer();

            URL url = new URL(strUrl + "?" + urlencode(maps));
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-agent", USER_AGENT);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(CONN_TIMEOUT);
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.connect();

            InputStream is = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is, CHARSET));
            String strRead = null;
            while ((strRead = bufferedReader.readLine()) != null) {
                sb.append(strRead);
            }
            result = sb.toString();
        } catch (IOException e) {
            logger.error("doGet: maps HttpURLConnection connect IOException!");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.error("doGet: maps BufferedReader close IOException!");
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return result;
    }

    public static String urlencode(Map<String, Object> maps) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> i : maps.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", CHARSET)).append("&");
            } catch (UnsupportedEncodingException e) {
                logger.error("urlencode: URLEncoder encode Exception!");
            }
        }

        return sb.toString();
    }

    private static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            logger.error("createSSLClientDefault: KeyManagementException!");
        } catch (NoSuchAlgorithmException e) {
            logger.error("createSSLClientDefault: NoSuchAlgorithmException!");
        } catch (KeyStoreException e) {
            logger.error("createSSLClientDefault: KeyStoreException!");
        }

        return HttpClients.createDefault();
    }
}
