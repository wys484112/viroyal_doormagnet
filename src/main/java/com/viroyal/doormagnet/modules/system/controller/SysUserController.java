package com.viroyal.doormagnet.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.viroyal.doormagnet.common.api.vo.Result;
import com.viroyal.doormagnet.common.constant.CommonConstant;
import com.viroyal.doormagnet.common.mybatisplus.page.IPage;
import com.viroyal.doormagnet.common.mybatisplus.page.Page;
import com.viroyal.doormagnet.common.system.api.ISysBaseAPI;
import com.viroyal.doormagnet.common.util.PasswordUtil;
import com.viroyal.doormagnet.common.util.RedisUtil;
import com.viroyal.doormagnet.common.util.oConvertUtils;
import com.viroyal.doormagnet.modules.system.model.SysUser;
import com.viroyal.doormagnet.modules.system.service.ISysUserService;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags="用户管理")
public class SysUserController {
	private Logger logger = LoggerFactory.getLogger(SysUserController.class);	
	@Autowired
	private ISysBaseAPI sysBaseAPI;

	@Autowired
	private ISysUserService sysUserService;

	@Autowired
	private RedisUtil redisUtil;
	
	@ApiOperation("获取用户列表")	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysUser>> queryPageList(SysUser user,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
		Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
		IPage<SysUser> pageList = sysUserService.page(page);

		result.setSuccess(true);
		result.setResult(pageList);
		logger.info(pageList.toString());
		return result;
	}
	
	@ApiOperation("用户注册")	
	@PostMapping("/register")
	public Result<JSONObject> userRegister(@RequestBody JSONObject jsonObject, SysUser user) {
		Result<JSONObject> result = new Result<JSONObject>();
		String phone = jsonObject.getString("phone");
		String smscode = jsonObject.getString("smscode");
		Object code = redisUtil.get(phone);
		String username = jsonObject.getString("username");
		logger.info("registerregisterregister::"+username);

		//未设置用户名，则用手机号作为用户名
		if(oConvertUtils.isEmpty(username)){
            username = phone;
        }
        //未设置密码，则随机生成一个密码
		String password = jsonObject.getString("password");
		if(oConvertUtils.isEmpty(password)){
            password = RandomUtil.randomString(8);
        }
		String email = jsonObject.getString("email");
		
		if(oConvertUtils.isNotEmpty(username)){
			SysUser sysUser1 = sysUserService.getUserByName(username);
			if (sysUser1 != null) {
				result.setMessage("用户名已注册");
				result.setSuccess(false);
				return result;
			}		
		}

		SysUser sysUser2 = sysUserService.getUserByPhone(phone);
		if (sysUser2 != null) {
			result.setMessage("该手机号已注册");
			result.setSuccess(false);
			return result;
		}

		if(oConvertUtils.isNotEmpty(email)){
            SysUser sysUser3 = sysUserService.getUserByEmail(email);
            if (sysUser3 != null) {
                result.setMessage("邮箱已被注册");
                result.setSuccess(false);
                return result;
            }
        }

//		if (!smscode.equals(code)) {
//			result.setMessage("手机验证码错误");
//			result.setSuccess(false);
//			return result;
//		}

		try {
			user.setCreateTime(new Date());// 设置创建时间
			String salt = oConvertUtils.randomGen(8);
			String passwordEncode = PasswordUtil.encrypt(username, password, salt);
			user.setSalt(salt);
			user.setUsername(username);
			user.setRealname(username);
			user.setPassword(passwordEncode);
			user.setEmail(email);
			user.setPhone(phone);
			user.setStatus(true);
			user.setDelFlag(CommonConstant.DEL_FLAG_0);
			user.setActivitiSync(CommonConstant.ACT_SYNC_1);
			logger.info("registerregisterregister::"+user);			
			sysUserService.addUserWithRole(user,"ee8626f80f7c2619917b6236f3a7f02b");//默认临时角色 test
			result.success("注册成功");
		} catch (Exception e) {
			result.error500("注册失败");
		}
		return result;
	}
	

}
