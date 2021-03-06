package com.viroyal.doormagnet.modules.system.mapper;

import java.util.List;

import com.viroyal.doormagnet.common.mybatisplus.mapper.BaseMapper;
import com.viroyal.doormagnet.modules.system.model.SysUser;

public interface SysUserMapper  extends BaseMapper<SysUser> {
	
	/**
	  * 通过用户账号查询用户信息
	 * @param username
	 * @return
	 */
	SysUser getUserByName(String username);
	
	
	int deleteByPrimaryKeys(List list);


	SysUser getUserByPhone(String phone);


	SysUser getUserByEmail(String email);
	
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int insert(SysUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int insertSelective(SysUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    SysUser selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int updateByPrimaryKeySelective(SysUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int updateByPrimaryKeyWithBLOBs(SysUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int updateByPrimaryKey(SysUser record);


}