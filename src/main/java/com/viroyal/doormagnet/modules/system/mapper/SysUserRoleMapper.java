package com.viroyal.doormagnet.modules.system.mapper;

import java.util.List;

import com.viroyal.doormagnet.modules.system.model.SysUserRole;

public interface SysUserRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int deleteByPrimaryKey(String id);

    
    int deleteByUserId(String userId);

	List<String> getRoleByUserName(String username);

	List<String> getRoleIdByUserName(String username);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int insert(SysUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int insertSelective(SysUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    SysUserRole selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int updateByPrimaryKeySelective(SysUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int updateByPrimaryKey(SysUserRole record);
}