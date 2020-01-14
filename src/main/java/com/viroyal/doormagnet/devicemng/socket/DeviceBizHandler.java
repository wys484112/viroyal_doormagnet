
package com.viroyal.doormagnet.devicemng.socket;

import java.nio.ByteBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.viroyal.doormagnet.devicemng.entity.*;
import com.viroyal.doormagnet.devicemng.mapper.DeviceMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceOpLogMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceSettingMapper;
import com.viroyal.doormagnet.devicemng.mapper.DeviceStatusMapper;
import com.viroyal.doormagnet.devicemng.pojo.BindUser;
import com.viroyal.doormagnet.util.RandomUtil;
import com.viroyal.doormagnet.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.viroyal.doormagnet.util.TextUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import javax.annotation.PostConstruct;

/**
 *
 */
@Component
public class DeviceBizHandler {}
