package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class DeviceSqlProvider {

    public String updateDeviceSetting(final DeviceSetting entity){
        return new SQL(){{
            UPDATE("t_device_setting");

            //条件写法.
            if(entity.security != null){
                SET("security=#{security}");
            }
            if(entity.beep != null){
                SET("beep=#{beep}");
            }
            if(entity.allDay != null){
                SET("all_day=#{allDay}");
            }
            if(entity.timeStart != null){
                SET("time_start=#{timeStart}");
            }
            if(entity.timeEnd != null){
                SET("time_end=#{timeEnd}");
            }
            if(entity.reportInterval != null){
                SET("report_interval=#{reportInterval}");
            }
            /*改到t_bind_info里面
            if(entity.appPush != null){
                SET("app_push=#{appPush}");
            }
            if(entity.pushSlient != null){
                SET("push_slient=#{pushSlient}");
            }
            */

            WHERE("dev_id=#{devId}");
        }}.toString();
    }

    public String updateBindSetting(@Param("userId") long userId, @Param("devId") int devId, @Param("entity") final DeviceSetting entity){
        return new SQL(){{
            UPDATE("t_bind_info");

            //条件写法.
            if(entity.name != null){
                SET("name=#{entity.name}");
            }
            if(entity.appPush != null){
                SET("app_push=#{entity.appPush}");
            }
            if(entity.pushSlient != null){
                SET("push_slient=#{entity.pushSlient}");
            }
            if(entity.slientStart != null){
                SET("slient_start=#{entity.slientStart}");
            }
            if(entity.slientEnd != null){
                SET("slient_end=#{entity.slientEnd}");
            }

            WHERE("dev_id=#{devId} and user_id=#{userId}");
        }}.toString();
    }
}
