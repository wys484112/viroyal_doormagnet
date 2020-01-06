package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.entity.BindInfoEntity;
import com.viroyal.doormagnet.devicemng.entity.Device;
import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import com.viroyal.doormagnet.devicemng.entity.UserEntity;
import com.viroyal.doormagnet.devicemng.pojo.BindListRsp;
import com.viroyal.doormagnet.devicemng.pojo.BindUser;
import com.viroyal.doormagnet.devicemng.pojo.DeviceAlert;
import com.viroyal.doormagnet.devicemng.pojo.DeviceSettingRsp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeviceMapper {
    @Select("select * from t_device where sn=#{0}")
    Device findBySn(String sn);

    @Insert("insert into t_device(sn, name) values(#{sn}, 'DM100')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertDevice(Device entity);

    @Update("update t_device set status=#{status}, reg_time=now() where sn=#{sn}")
    int register(@Param("status") int status, @Param("sn") String sn);

    @Update("update t_device set status=#{status} where sn=#{sn}")
    int updateStatus(@Param("status") int status, @Param("sn") String sn);

    //修改在线状态
    @Update("update t_device set online=#{online}, online_time=now() where id=#{devId}")
    int updateOnline(@Param("devId") int devId, @Param("online") int online);

    @Select("select id from ac_user where token=#{0}")
    Long getUserIdByToken(String token);

    @Select("select count(*) from t_bind_info where user_id=#{userId} and dev_id=#{devId}")
    int getBindCount(@Param("userId") long userId, @Param("devId") int devId);

    @Select("select count(*) from t_bind_info where dev_id=#{devId}")
    int getBindCountByDevId(@Param("devId") int devId);

    @Insert("insert into t_bind_info(user_id, dev_id, name) values(#{userId}, #{devId}, #{name})")
    int insertBindInfo(@Param("userId") long userId, @Param("devId") int devId, @Param("name") String name);

    @Select("SELECT d.id, b.name, d.sn, s.alert_time, ds.security FROM t_bind_info b JOIN t_device d ON b.dev_id=d.id " +
            " left join t_device_status s on b.dev_id=s.dev_id join t_device_setting ds on b.dev_id=ds.dev_id where b.user_id=#{0}")
    List<BindListRsp> getBindList(long userId);

    //查询绑定某个设备的用户
    @Select("SELECT b.user_id, b.name FROM t_bind_info b " +
            " where b.dev_id=#{0}")
    List<BindInfoEntity> getBindInfoList(int devId);

    //查询绑定设备的用户的信息，主要是Push id
    @Select("SELECT b.name, b.user_id, user.im_token as push_id, b.app_push  FROM t_bind_info b, ac_user user  " +
            " where b.dev_id=#{0} and b.user_id=user.id")
    List<BindUser> getBindUserList(int devId);

    @Delete("delete from t_bind_info where user_id=#{userId} and dev_id=#{devId}")
    int deleteBindInfo(@Param("userId") long userId, @Param("devId") int devId);

    @Select("SELECT st.*, stus.voltage, stus.power, bind.name, bind.app_push, bind.push_slient," +
            "bind.slient_start, bind.slient_end FROM t_device_setting st , t_bind_info bind LEFT JOIN t_device_status stus " +
            "ON  stus.dev_id=bind.dev_id WHERE st.dev_id=bind.dev_id AND bind.user_id=#{userId} AND bind.dev_id=#{devId}")
    DeviceSettingRsp getDeviceSetting(@Param("userId") long userId, @Param("devId") int devId);

    //查询设备的告警记录
    @Select("SELECT l.id, l.dev_id, l.action, l.action_time FROM t_device_op_log l, t_bind_info b " +
            "WHERE l.dev_id=b.dev_id AND l.action in (1,2, 6, 7, 8) and b.user_id=#{userId} AND b.dev_id=#{devId} " +
            " ORDER BY l.action_time DESC limit #{limit} offset #{offset}")
    List<DeviceAlert> getDeviceAlertByDeviceId(@Param("userId") long userId, @Param("devId") int devId, @Param("limit") int limit, @Param("offset") int offset);

    //查询所有绑定设备的告警记录
    @Select("SELECT l.id, l.dev_id, l.action, l.action_time FROM t_device_op_log l, t_bind_info b " +
            "WHERE l.dev_id=b.dev_id AND l.action in (1,2, 6, 7, 8) and b.user_id=#{userId}  " +
            " ORDER BY l.action_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<DeviceAlert> getDeviceAlert(@Param("userId") long userId, @Param("limit") int limit, @Param("offset") int offset);

    //修改设备设置
    @UpdateProvider(type=DeviceSqlProvider.class,method="updateDeviceSetting")
    int updateDeviceSetting(final DeviceSetting entity);

    //获取用户信息
    @Select("select id,phone from ac_user where token=#{0}")
    UserEntity findUserByToken(String token);

    //修改设备绑定名称
    @UpdateProvider(type=DeviceSqlProvider.class,method="updateBindSetting")
    int updateBindSetting(@Param("userId") long userId, @Param("devId") int devId, @Param("entity") final DeviceSetting entity);
}
