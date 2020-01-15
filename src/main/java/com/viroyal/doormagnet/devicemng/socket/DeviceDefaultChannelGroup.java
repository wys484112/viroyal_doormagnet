package com.viroyal.doormagnet.devicemng.socket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.PlatformDependent;

public class DeviceDefaultChannelGroup extends DefaultChannelGroup{
    private static final Logger logger = LoggerFactory.getLogger(DeviceDefaultChannelGroup.class);

	//imei对应的DeviceMessage    
    private final ConcurrentMap<String, Channel> deviceIMEIChannelIdMap = PlatformDependent.newConcurrentHashMap();
    
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



	
	public boolean add(DeviceMessage message) {
		// TODO Auto-generated method stub
		boolean added = false;
        logger.info("addaddaddadd  enter");

		if (add(message.getChannel())) {
	        logger.info("addaddaddadd  DeviceMessage "+message.getImei());

			added = deviceIMEIChannelIdMap.putIfAbsent(message.getImei(), message.getChannel()) == null;
			if(added) {
				message.getChannel().closeFuture().addListener(remover);

			}
		}
		return added;
	}


	public boolean removeDevice(Object channel) {
		Channel c = null;
		// TODO Auto-generated method stub
		{
			Iterator<Entry<String, Channel>> entries = deviceIMEIChannelIdMap.entrySet().iterator();

			while (entries.hasNext()) {

				Entry<String, Channel> entry = entries.next();
				String imei=entry.getKey();
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

				// 注意这里操作了集合,下面的的遍历不会再打印0
				if (((Channel) channel).id().equals(entry.getValue().id())) {
					c = deviceIMEIChannelIdMap.remove(entry.getKey());
					logger.info("removedevice imei====="+imei);
				}
			}

		}
		if (c == null) {
			return false;
		}
        c.closeFuture().removeListener(remover);

		return true;
	}
	
	public DefaultChannelGroup getChannelGroupFromImeis(ArrayList<String> imeis) {
		DefaultChannelGroup groups = new DefaultChannelGroup("Groups", GlobalEventExecutor.INSTANCE);
		Iterator<String> list = imeis.iterator();
		while (list.hasNext()) {
			String imei = list.next();
			if (deviceIMEIChannelIdMap.containsKey(imei)) {
				groups.add(deviceIMEIChannelIdMap.get(imei));
			}
		}

		return groups;
	}
	
	public Channel getChannelFromImei(String imei) {
		if (deviceIMEIChannelIdMap.containsKey(imei)) {
			return deviceIMEIChannelIdMap.get(imei);
		}
		return null;

	}
	
    public List<String> getImeisArray() {
        List<String> aa=new ArrayList<>();
		Iterator<Entry<String, Channel>> entries = deviceIMEIChannelIdMap.entrySet().iterator();
		while (entries.hasNext()) {

			Entry<String, Channel> entry = entries.next();
			String imei=entry.getKey();
			aa.add(imei);
		}
        return aa;
    }
    
	
}
