package com.viroyal.doormagnet.usermng.aop;



import com.viroyal.doormagnet.usermng.pojo.BaseResponse;
import com.viroyal.doormagnet.usermng.util.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*
 * Author: Created by qinyl.
 * Date:   2017/1/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
@Aspect
@Component
@Configuration
@Order(2)
public class AspectLogger {
    private static final Logger logger = LoggerFactory.getLogger(AspectLogger.class);

    @Around("execution(* com.viroyal.doormagnet.usermng.controller.*Controller.*(..))")
    public Object loggerAround(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        int argsLen = args.length;
        String reqJson = " [usermanager request]: no param";

        if (argsLen > 0) {
            StringBuffer reqJsonBuf = new StringBuffer(" [usermanager request]: ");
            for (int i = 0; i < argsLen; i++) {
                reqJsonBuf.append(JsonUtil.writeObject(args[i]));
                if (i < argsLen - 1) reqJsonBuf.append(", ");
            }
            reqJson = reqJsonBuf.toString();
        }

        try {
            logger.info(getName(point) + reqJson);
            Object rsp = point.proceed();
            logger.info(getName(point) + " [usermanager response]: " + (rsp != null ? JsonUtil.writeObject(rsp) : " no return"));

            return rsp;
        } catch (Exception e) {
            logger.error(getName(point) + " [usermanager exception by]: " + reqJson, e);
            return BaseResponse.SYSTEM_BUSY;
        }
    }

    private String getName(JoinPoint point) {
        return point.getTarget().getClass().getSimpleName() + "." + point.getSignature().getName();
    }
}
