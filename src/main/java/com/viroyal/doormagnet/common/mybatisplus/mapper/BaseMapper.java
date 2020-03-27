package com.viroyal.doormagnet.common.mybatisplus.mapper;

import com.viroyal.doormagnet.common.mybatisplus.page.IPage;

public interface BaseMapper<T> extends Mapper<T> {
	IPage<T> selectPage(IPage<T> page);

}
