package com.viroyal.doormagnet.common.mybatisplus.service.impl;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.viroyal.doormagnet.common.mybatisplus.mapper.BaseMapper;
import com.viroyal.doormagnet.common.mybatisplus.page.IPage;
import com.viroyal.doormagnet.common.mybatisplus.service.IService;

@SuppressWarnings("unchecked")
public class ServiceImpl<M extends BaseMapper<T>, T> implements IService<T> {

	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	protected M baseMapper;

	@Override
	public M getBaseMapper() {
		return baseMapper;
	}

	@Override
	public IPage<T> page(IPage<T> page) {
		// TODO Auto-generated method stub
		return baseMapper.selectPage(page);
	}
}
