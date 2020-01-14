package com.viroyal.doormagnet.devicemng.socket;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import com.viroyal.doormagnet.util.RandomUtil;

@Service
public class MessageDispatcher {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Executor mMessageExecutor;

    public MessageDispatcher() {
        mMessageExecutor = new ThreadPoolExecutor(20 /* corePoolSize */, 40 /* maximumPoolSize */,
                100 /* keepAliveSecond */, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public void stop() {
    }

    /**
     * channel上产生的事件，放到任务队列中执行
     * 
     * @param message
     * @param traceId
     */
    public void dispatch(DeviceMessageBase message, String traceId) {
        MessageWorker messageWorker = new MessageWorker(message, traceId);
        mMessageExecutor.execute(messageWorker);
    }

    /**
     * 消息队列处理线程实现
     *
     * @author ligang
     *
     */
    private final class MessageWorker implements Runnable {
        private DeviceMessageBase message;
        private String traceId;

        private MessageWorker(DeviceMessageBase message, String traceId) {
            this.message = message;
            this.traceId = traceId;
        }

        @Override
        public void run() {
            try {
                MDC.put(RandomUtil.MDC_KEY, traceId);
                handleMessage();
            } finally {
                MDC.remove(RandomUtil.MDC_KEY);
            }
        }

        /**
         * 处理消息队列
         */
        private void handleMessage() {}
    }
}
