package com.viroyal.doormagnet.usermng.aop;

import com.viroyal.doormagnet.usermng.util.RandomUtil;
import org.apache.log4j.MDC;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
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
@Order(1)
public class AspectMdc {
    private static final Logger logger = LoggerFactory.getLogger(AspectMdc.class);

    @Before("execution(* com.viroyal.usermanager.controller.*Controller.*(..))")
    public void addMdc(JoinPoint joinPoint) {
        try {
            MDC.put(RandomUtil.MDC_KEY, RandomUtil.getMDCValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After("execution(* com.viroyal.usermanager.controller.*Controller.*(..))")
    public void removeMdc(JoinPoint joinPoint) {
        try {
            MDC.remove(RandomUtil.MDC_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Around("execution(* com.viroyal.usermanager.controller.*Controller.*(..)))")
    public Object timeAround(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long start = System.currentTimeMillis();
        try {
            obj = joinPoint.proceed(args);
        } catch (Throwable e) {
            logger.error("统计某方法执行耗时环绕通知出错", e);
        }

        long end = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String method = signature.getDeclaringTypeName() + "." + signature.getName();
        this.printExecTime(method, start, end);

        return obj;
    }

    private void printExecTime(String method, long start, long end) {
        long diff = end - start;
        //if (diffTime > ONE_MINUTE) {
        logger.info("-----" + method + " 方法执行耗时：" + diff + " ms");
        //}
    }
}
