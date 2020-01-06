package com.viroyal.doormagnet.devicemng.socket;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viroyal.doormagnet.util.RandomUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

@Service
public class DeviceHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DeviceHandler.class);

    @Autowired
    protected MessageDispatcher mDispactcher;

    @Autowired
    protected DeviceBizHandler mBizHandler;

    private AtomicInteger mChannelCount = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            MDC.put(RandomUtil.MDC_KEY, RandomUtil.getMDCValue());
            DeviceMessageBase message = decodeMessage(ctx.channel(), (ByteBuf) msg);
            if (!message.mIsValid) {
                return;
            }

            logger.info("channel=" + ctx.channel() + ", total channel=" + mChannelCount + ", msg=" + message);

            mDispactcher.dispatch(message, MDC.get(RandomUtil.MDC_KEY));
        } catch (Exception e) {
            logger.error("error, channel=" + ctx.channel() + ", error=" + e.getMessage());
        } finally {
            ReferenceCountUtil.release(msg);
            MDC.remove(RandomUtil.MDC_KEY);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("channel=" + ctx.channel() + " cause=" + cause);
        // close it
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        mChannelCount.incrementAndGet();
        logger.info("channel=" + ctx.channel() + ", total channel=" + mChannelCount + ", id="
                + ctx.channel().id().asShortText());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel=" + ctx.channel());
        mChannelCount.decrementAndGet();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                logger.info("read_idle channel=" + ctx.channel());
                ctx.close();
            }
        } else if (evt instanceof SslHandshakeCompletionEvent) {
            logger.info("ssl handshake done");
        }
    }

    @Override
    public boolean isSharable() {
        return true;
    }

    protected DeviceMessageBase decodeMessage(Channel ch, ByteBuf msg) {
        DeviceMessage deviceMessage = new DeviceMessage(ch, DeviceMessage.MSG_READ_DATA, msg, mBizHandler);
        return deviceMessage;
    }

    protected DeviceBizHandler getBizHandler() {
        return mBizHandler;
    }

}
