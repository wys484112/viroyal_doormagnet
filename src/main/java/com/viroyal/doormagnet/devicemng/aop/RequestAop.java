package com.viroyal.doormagnet.devicemng.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viroyal.doormagnet.common.util.JsonUtil;
import com.viroyal.doormagnet.common.util.RandomUtil;
import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@SuppressWarnings(value = "unused")
@Aspect
@Component
public class RequestAop {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("execution(* com.viroyal.doormagnet.devicemng.controller.*Controller.*(..))")
    public Object printAccessDetail(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        int argsLen = args.length;
        String reqJson = " Request: No Parameters.";

        if (argsLen > 0) {
            StringBuilder sb = new StringBuilder(" Request: ");
            for (int i = 0; i < argsLen; i++) {
                if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse) {
                    continue;
                }

                sb.append(JsonUtil.writeObject(args[i]));
                if (i < argsLen - 1) {
                    sb.append(", ");
                }
            }
            reqJson = sb.toString();
        }

        MDC.put(RandomUtil.MDC_KEY, RandomUtil.getMDCValue());
        try {
            logger.info(getName(point) + reqJson);
            Object rsp = point.proceed();
            logger.info(getName(point) + " Response: " + (rsp != null ? JsonUtil.writeObject(rsp) : "No Return"));

            return rsp;
        } catch (TokenInvalidException e) {
            logger.error(getName(point) + " " + e.getMessage());
            return BaseResponse.INVALID_TOKEN;
        }
        catch (Exception e) {
            logger.error(getName(point) + " Exception: " + reqJson, e);
            return BaseResponse.SYSTEM_BUSY;//如果返回类型不是BaseResponse会报错
        } finally {
            MDC.remove(RandomUtil.MDC_KEY);
        }
    }

    private String getName(JoinPoint point) {
        return point.getTarget().getClass().getSimpleName() + "." + point.getSignature().getName();
    }
}
