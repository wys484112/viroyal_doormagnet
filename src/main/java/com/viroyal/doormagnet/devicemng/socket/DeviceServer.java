/**
 *
 */
package com.viroyal.doormagnet.devicemng.socket;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.mapper.DeviceMessageMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceResponseMapper;
import com.viroyal.doormagnet.devicemng.mapper.ServiceSettingsDeviceSwitchMapper;
import com.viroyal.doormagnet.devicemng.model.DeviceMessage;
import com.viroyal.doormagnet.devicemng.model.DeviceResponse;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceSwitch;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;
import com.viroyal.doormagnet.util.ErrorCode;
import com.viroyal.doormagnet.util.TextUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

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

    @Autowired
    private  ServiceSettingsDeviceSwitchMapper serviceSettingsDeviceSwitchMapper;
    
    @Autowired
    private DeviceResponseMapper deviceResponseMapper;
    
    @Autowired
    private DeviceMessageMapper deviceMessageMapper;
    
    private ByteArrayEncoder mByteEncoder;

    public DeviceServer() {
        mByteEncoder = new ByteArrayEncoder();
    }

    final Object object =new Object();
        
    public static DeviceDefaultChannelGroup ALLCHANNELS_GROUP = new DeviceDefaultChannelGroup("ChannelGroups", GlobalEventExecutor.INSTANCE);

    
    
    
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
    
    
    //
    /**
     * 当设备在线时发送相应的信息设置给设备。
     */
    @Override   
    @Scheduled(cron = "5/5 * * * * ?")
    public void sendToDevice() {
        logger.info("sendToDevice imei:"+"888888888888888");
        Channel channel=ALLCHANNELS_GROUP.getChannelFromImei("888888888888888");
        logger.info("sendToDevic aaaaa");

        if(channel!=null) {
            logger.info("sendToDevice channel:"+channel.toString());

        	try {
                logger.info("sendToDevice 1111");

				sendMsg("ttttteeeessssttttt",channel);
                logger.info("sendToDevice 2222");

			} catch (Exception e) {
                logger.info("sendToDevice 3333");

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public static void sendMsg(String textHexStr,Channel channel) throws Exception {
		// Thread.sleep(2 * 1000);
		ByteBuf buf = channel.alloc().buffer();
		Charset charset = Charset.forName("UTF-8");
		buf.writeCharSequence(textHexStr, charset);
		channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				if(future.isSuccess()) {
			    	logger.info("sendMsg 发送成功   channel:"+channel +"  textHexStr:"+textHexStr);        					
				}else {
			    	logger.info("sendMsg 发送失败   channel:"+channel +"  textHexStr:"+textHexStr);        
					
				}

			}
		});
	}
	
	@Override
	public Channel  getChannelFromImei(String imei) {
        return ALLCHANNELS_GROUP.getChannelFromImei(imei);
	}
	
    /**
     * 获取用户名下的在线设备imei列表
     * @param token 用户token
     * @return BaseResponse
     * @throws TokenInvalidException exception
     */
	@Override
    public  List<String> getDeviceActiveList() {

		logger.info("getDeviceActiveList thread=="+Thread.currentThread().getName());
        return ALLCHANNELS_GROUP.getImeisArray();
    }

	@Override
	public BaseResponse setDeviceSettingSwitch(String token, String devId, ServiceSettingsDeviceSwitch param)
			throws TokenInvalidException {
		ServiceSettingsDeviceSwitch test=param;
		test.setTime(new Date());
		serviceSettingsDeviceSwitchMapper.insertSelective(test);
		
		DeviceMessage toDeviceMessage=new DeviceMessage();
		toDeviceMessage.setChannel(getChannelFromImei(test.getImei()));
		toDeviceMessage.setImei(test.getImei());
		toDeviceMessage.setHeadhexstr("6F01");
		toDeviceMessage.setFlaghexstr("00");
		toDeviceMessage.setControlhexstr("11");
		toDeviceMessage.setContentlengthhexstr("0005");
		toDeviceMessage.setContenthexstr(ServiceSettingsDeviceSwitchToString(test));
		logger.info("setDeviceSettingSwitch thread=="+Thread.currentThread().getName());
		
		return  sendMsgAndReceiveResponse(toDeviceMessage,"21");
		// TODO Auto-generated method stub
	}
	
    @Async
    public ListenableFuture<DeviceResponse> getDeviceResponse(Long waittime,String imei,String controlhexstr) {
        
		synchronized (object) {
			try {
				logger.info("等待设备反馈信息 thread=="+Thread.currentThread().getName());
				object.wait(waittime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		    	
    	return new AsyncResult<DeviceResponse>(deviceResponseMapper.findLastresponse(imei,
    			controlhexstr));
    }
    
	public  BaseResponse sendMsgAndReceiveResponse(DeviceMessage toDeviceMessage,String controlhexstr)  {
		toDeviceMessage.setTime(new Date());
		logger.info("服务器发送信息");

		if(toDeviceMessage.getChannel()==null) {
			logger.info("服务器发送信息，设备不在线，将信息存入数据库，后续再发送");
			deviceMessageMapper.insertOrUpdate(toDeviceMessage);
	        return new BaseResponse(ErrorCode.DEVICE_RESPONSE_ERROR, "设备不在线，后续发送");
		}
		

		try {
			sendMsg(toDeviceMessage.getHexStr(), toDeviceMessage.getChannel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("服务器发送信息失败，将信息存入数据库，后续再发送");
			deviceMessageMapper.insertOrUpdate(toDeviceMessage);
	        return (new BaseResponse(ErrorCode.SERVICE_SEND_ERROR, "发送信息失败。将信息存入数据库，后续再发送"));			
		}finally {

		}


		DeviceResponse response = null;
		try {
			response = getDeviceResponse(5000L,toDeviceMessage.getImei(),controlhexstr).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		if(response!=null) {
			logger.info("服务器发送信息完毕，response=="+response.toString());
			logger.info("服务器发送信息完毕，toDeviceMessage.getTime()=="+toDeviceMessage.getTime());
		}
		logger.info("服务器发送信息时间=="+toDeviceMessage.getTime());


		if(response!=null&&response.getTime().after(toDeviceMessage.getTime())) {
			deviceMessageMapper.deleteByImeiAndControl(toDeviceMessage.getImei(), toDeviceMessage.getControlhexstr());
			logger.info("设置成功");
			return BaseResponse.SUCCESS;

		}else if(response==null||response.getTime().before(toDeviceMessage.getTime())){
			logger.info("服务器发送信息，设备没有回复，将信息存入数据库，后续再发送");
			deviceMessageMapper.insertOrUpdate(toDeviceMessage);
			
	        return new BaseResponse(ErrorCode.DEVICE_RESPONSE_ERROR, "设备没有回复");						
		}else {
			logger.info("服务器发送信息，设备反馈信息无效，将信息存入数据库，后续再发送");
			deviceMessageMapper.insertOrUpdate(toDeviceMessage);
	        return new BaseResponse(ErrorCode.DEVICE_RESPONSE_ERROR, "设备反馈信息无效");
		}
			
	}
	public String   ServiceSettingsDeviceSwitchToString(ServiceSettingsDeviceSwitch test) {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append(int2HexStringFormated(test.getSwitchcontrolone(),1,"0"));
		stringBuffer.append(int2HexStringFormated(test.getSwitchcontroltwo(),1,"0"));
		stringBuffer.append(int2HexStringFormated(test.getSwitchcontrolthree(),1,"0"));
		stringBuffer.append(TextUtils.byte2HexStr("11".getBytes()));		
		return stringBuffer.toString();
		
	}
	
	public  String  int2HexStringFormated(int number,int bytenum,String fill) {
	      String st = Integer.toHexString(number).toUpperCase();
	      st = String.format("%"+bytenum*2+"s",st);
	      st= st.replaceAll(" ",fill);
	      return st;
	}
    
}
