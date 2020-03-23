package com.viroyal.doormagnet.modules.system.mapper;

import com.viroyal.doormagnet.modules.system.model.SysRolePermission;

public interface SysRolePermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_permission
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_permission
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int insert(SysRolePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_permission
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int insertSelective(SysRolePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_permission
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    SysRolePermission selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_permission
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int updateByPrimaryKeySelective(SysRolePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_permission
     *
     * @mbg.generated Mon Mar 23 11:02:42 CST 2020
     */
    int updateByPrimaryKey(SysRolePermission record);
}