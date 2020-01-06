package com.viroyal.doormagnet.about.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.viroyal.doormagnet.about.entity.VersionEntity;

/**
 * 
 * @author walker
 *
 */
@Mapper
public interface VersionMapper {
    // 获取版本表里的最新一条数据
    @Select("SELECT v.version, v.version_code, v.apk_url, v.upgrade_log FROM t_app_version v ORDER BY v.version_code DESC LIMIT 1;")
    VersionEntity getLatestVersion();

}
