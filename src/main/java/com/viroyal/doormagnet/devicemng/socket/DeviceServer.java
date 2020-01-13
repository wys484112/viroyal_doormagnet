/**
 *
 */
package com.viroyal.doormagnet.devicemng.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 目前公司定义的设备端协议的服务器实现类： 1 tcp长连接； 2 数据通过json格式传输
 *
 * 最早开发的时候，是用的ChildWatchServer这个类，但目前看来，协议和儿童手表
 * 没有关系，可以采用一种通用的实现，但是ChildWatch的心跳是服务端主动发起的，
 * 现在要改为由设备端主动发起，所以还是保留ChildWatchServer。
 *
 * @author LiGang
 *
 */
@Service
public class DeviceServer implements IDeviceServer {

    private static final Logger logger = LoggerFactory.getLogger(DeviceServer.class);

    @Value("${listen_port}")
    private int port;

    @Value("${idle_time}")
    private int readIdleTime;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Channel serverChannel;

    @Autowired
    private DeviceHandler mHandler;

    private ByteArrayEncoder mByteEncoder;

    public DeviceServer() {
        mByteEncoder = new ByteArrayEncoder();
    }

    @Override
    public void startup() throws Exception {
        bossGroup = new NioEventLoopGroup(); // (1)
        workerGroup = new NioEventLoopGroup();
        logger.info("enter");

        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(readIdleTime, 0, 0));                            
							// Decoders
//							ch.pipeline().addLast("frameDecoder",
//									new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
							ch.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());
							// Encoder
//							ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
							ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());
							
                            ch.pipeline().addLast("decoder", mHandler);
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128) // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)
            logger.info("bind server socket ok at " + this.port);
            serverChannel = f.channel();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to
            // gracefully
            // shut down your server.
            // f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("create server socket exception", e);
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        } /*
           * finally { workerGroup.shutdownGracefully(); bossGroup.shutdownGracefully(); }
           */
    }

    @Override
    public void shutdown() {
        logger.info("serverChannel=" + serverChannel);
        /*
         * if (serverChannel != null) { try { //serverChannel.closeFuture().sync();
         * serverChannel.closeFuture();
         * //serverChannel.close().addListener(ChannelFutureListener.CLOSE); }
         * catch(Exception e) { logger.fatal("close socket failed", e); } }
         */
        if (workerGroup != null) {
            try {
                workerGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                logger.error("exception", e);
            }
        }

        if (bossGroup != null) {
            try {
                bossGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                logger.error("exception", e);
            }
        }

        logger.info("shutdown complete");
    }
}
