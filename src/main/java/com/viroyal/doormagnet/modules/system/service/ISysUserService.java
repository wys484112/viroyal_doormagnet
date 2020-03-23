package com.viroyal.doormagnet.modules.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.viroyal.doormagnet.common.api.vo.Result;
import com.viroyal.doormagnet.common.system.vo.SysUserCacheInfo;
import com.viroyal.doormagnet.modules.system.model.SysUser;




/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService {

	/**
	 * 重置密码
	 *
	 * @param username
	 * @param oldpassword
	 * @param newpassword
	 * @param confirmpassword
	 * @return
	 */
	public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword);

	/**
	 * 修改密码
	 *
	 * @param sysUser
	 * @return
	 */
	public Result<?> changePassword(SysUser sysUser);

	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	public boolean deleteUser(String userId);

	/**
	 * 批量删除用户
	 * @param userIds
	 * @return
	 */
	public boolean deleteBatchUsers(String userIds);
	
	public SysUser getUserByName(String username);
	
	/**
	 * 添加用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void addUserWithRole(SysUser user,String roles);
	
	
	/**
	 * 修改用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void editUserWithRole(SysUser user,String roles);

	/**
	 * 获取用户的授权角色
	 * @param username
	 * @return
	 */
	public List<String> getRole(String username);
	
	/**
	  * 查询用户信息包括 部门信息
	 * @param username
	 * @return
	 */
	public SysUserCacheInfo getCacheUser(String username);

	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param username 用户名
	 * @return 角色集合
	 */
	Set<String> getUserRolesSet(String username);

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	Set<String> getUserPermissionsSet(String username);
	
	
	/**
	 * 根据手机号获取用户名和密码
	 */
	public SysUser getUserByPhone(String phone);


	/**
	 * 根据邮箱获取用户
	 */
	public SysUser getUserByEmail(String email);



	
	/**
	   * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffective(SysUser sysUser);

}
