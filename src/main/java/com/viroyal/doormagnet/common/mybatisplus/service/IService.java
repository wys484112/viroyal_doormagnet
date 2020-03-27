package com.viroyal.doormagnet.common.mybatisplus.service;

import com.viroyal.doormagnet.common.mybatisplus.mapper.BaseMapper;
import com.viroyal.doormagnet.common.mybatisplus.page.IPage;

public interface IService<T> {

	IPage<T> page(IPage<T> page);
    BaseMapper<T> getBaseMapper();

}
