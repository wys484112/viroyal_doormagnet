package com.viroyal.doormagnet.devicemng.socket;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.dialect.Ingres10Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viroyal.doormagnet.common.util.RandomUtil;
import com.viroyal.doormagnet.common.util.TextUtils;
import com.viroyal.doormagnet.devicemng.model.DeviceMessage;

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
    
//    private AtomicInteger mChannelCount = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            MDC.put(RandomUtil.MDC_KEY, RandomUtil.getMDCValue());
			logger.info("channelRead thread=="+Thread.currentThread().getName());            
            logger.info("channel=" + ctx.channel() + ", msg=" + TextUtils.byte2Str((byte[]) msg));            
            mDispactcher.handleMessage(ctx.channel(), (byte[]) msg);
            logger.info("dispatch message");
            {
                logger.info("channels size===="+DeviceServer.ALLCHANNELS_GROUP.size());
            }

        } catch (Exception e) {
            logger.error("error, channelRead channel=" + ctx.channel() + ", error=" + e.getMessage());
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
//        mChannelCount.incrementAndGet();        
        logger.info("channel=" + ctx.channel() +", id="
                + ctx.channel().id().asShortText());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel=" + ctx.channel());
//        mChannelCount.decrementAndGet();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                logger.info("read_idle channel=" + ctx.channel());
//                ctx.close();
            }
        } else if (evt instanceof SslHandshakeCompletionEvent) {
            logger.info("ssl handshake done");
        }
    }

    @Override
    public boolean isSharable() {
        return true;
    }

    protected DeviceMessage decodeMessage(Channel ch, byte[] msg) {
    	DeviceMessage base =new DeviceMessage();
    	String message=new String(msg).trim().toUpperCase(Locale.US);
        logger.info("decodeMessagerevrevererererre=====:"+message.toString());

    	base.setChannel(ch);
    	
    	base.setHeadhexstr(message.substring(0, 4));
        logger.info("base.getHeadHexStr()=====:"+base.getHeadhexstr());
    	
    	base.setFlaghexstr(message.substring(4, 6));
        logger.info("base.getFlagHexStr()=====:"+base.getFlaghexstr());
    	
    	base.setControlhexstr(message.substring(6, 8));
        logger.info("base.getHeadHexStr()=====:"+base.getControlhexstr());
    	
    	base.setVersionhexstr(message.substring(8, 10));
        logger.info("base.getVersionHexStr()=====:"+base.getVersionhexstr());

    	base.setContentlengthhexstr(message.substring(10, 14));
        logger.info("base.getContentLengthHexStr()=====:"+base.getContentlengthhexstr());

        Integer dataLength = Integer.valueOf(base.getContentlengthhexstr(),16);
        logger.info("dataLength=====:"+dataLength);

    	base.setContenthexstr(message.substring(14, dataLength*2+14));
        logger.info("base.getControlHexStr()=====:"+base.getControlhexstr());

    	base.setEndshexstr(message.substring(dataLength*2+14, message.length()));
        logger.info("base.getEndsHexStr()=====:"+base.getEndshexstr());

    	String imeiHexStr=base.getContenthexstr().substring(0, 30);
        logger.info("imeiHexStr=====:"+imeiHexStr);
    	
    	base.setImei(TextUtils.hexStr2AscIIStr(imeiHexStr));

        logger.info("base.getImei()=====:"+base.getImei());

    	return base;
    }

    protected DeviceBizHandler getBizHandler() {
        return mBizHandler;
    }

}
