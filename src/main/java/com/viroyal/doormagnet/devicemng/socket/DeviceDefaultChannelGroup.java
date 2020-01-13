package com.viroyal.doormagnet.devicemng.socket;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.ServerChannel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.PlatformDependent;

public class DeviceDefaultChannelGroup extends DefaultChannelGroup{
    private static final Logger logger = LoggerFactory.getLogger(DeviceDefaultChannelGroup.class);

	//imei对应的DeviceMessage    
    private final ConcurrentMap<String, DeviceMessageBase> deviceIMEIChannelIdMap = PlatformDependent.newConcurrentHashMap();
    
    private final ChannelFutureListener remover = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
        	removeDevice(future.channel());
        }
    };
	public DeviceDefaultChannelGroup(String name, EventExecutor executor) {
		super(name, executor);
		// TODO Auto-generated constructor stub
	}    
	public DeviceDefaultChannelGroup(EventExecutor executor, boolean stayClosed) {
		super(executor, stayClosed);
		// TODO Auto-generated constructor stub
	}

	public DeviceDefaultChannelGroup(EventExecutor executor) {
		super(executor);
		// TODO Auto-generated constructor stub
	}

	public DeviceDefaultChannelGroup(String name, EventExecutor executor, boolean stayClosed) {
		super(name, executor, stayClosed);
		// TODO Auto-generated constructor stub
	}



	
	public boolean add(DeviceMessageBase message) {
		// TODO Auto-generated method stub
		boolean added = false;
		if (add(message.getmChannel())) {
			added = deviceIMEIChannelIdMap.putIfAbsent(message.getImei(), message) == null;
			if(added) {
				message.getmChannel().closeFuture().addListener(remover);

			}
		}
		return added;
	}


	public boolean removeDevice(Object channel) {
		DeviceMessageBase c = null;
		// TODO Auto-generated method stub
		{
			Iterator<Entry<String, DeviceMessageBase>> entries = deviceIMEIChannelIdMap.entrySet().iterator();

			while (entries.hasNext()) {

				Entry<String, DeviceMessageBase> entry = entries.next();

				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

				// 注意这里操作了集合,下面的的遍历不会再打印0
				if (((Channel) channel).id().equals(entry.getValue().getmChannel().id())) {
					c = deviceIMEIChannelIdMap.remove(entry.getKey());
					logger.info("removedevice====="+c.getImei());
				}
			}

		}
		if (c == null) {
			return false;
		}
        c.getmChannel().closeFuture().removeListener(remover);

		return true;
	}
}
