package com.viroyal.doormagnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings(value = "unused")
@Component
public class ApplicationFilter implements Filter {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationFilter.class);

    private final static String MASTERKEY = "master_key";

    @Value("${spring.master_key}")
    private String masterKey;

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        // TODO Auto-generated method stub
        logger.info("Account Module Filter");

        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        String url = request.getRequestURI();
//        if (!"/".equals(url)) {
//            if (!masterKey.equals(request.getHeader("master_key")) /*|| request.getHeader("school_id") == null*/) {
//                try {
//                    returnJson(response, "{\"error_code\":1003,\"error_msg\":\"请升级新版本\"}");
//                    return;
//                } catch (Exception e) {
//                    logger.error("filter return master error exception: " + e);
//                }
//            }
//        }

        arg2.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            logger.error("response error", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
