package com.viroyal.doormagnet.about.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.viroyal.doormagnet.about.entity.AdvEntity;

/**
 * 
 * @author walker
 *
 */
@Mapper
public interface AdvMapper {

    // 获取广告数据
    @Select("SELECT a.id,a.cycle_mode,a.duration,a.media_type,a.media_url,a.target_url,a.start_at,a.end_at FROM t_app_ads a ORDER BY a.id;")
    List<AdvEntity> getAdvData();

}
