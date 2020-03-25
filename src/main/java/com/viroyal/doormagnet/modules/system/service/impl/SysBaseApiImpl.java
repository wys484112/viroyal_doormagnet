package com.viroyal.doormagnet.modules.system.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.viroyal.doormagnet.DoorMagnetApplication;
import com.viroyal.doormagnet.common.constant.CacheConstant;
import com.viroyal.doormagnet.common.constant.CommonConstant;
import com.viroyal.doormagnet.common.constant.DataBaseConstant;
import com.viroyal.doormagnet.common.exception.JeecgBootException;
import com.viroyal.doormagnet.common.system.api.ISysBaseAPI;
import com.viroyal.doormagnet.common.system.vo.ComboModel;
import com.viroyal.doormagnet.common.system.vo.DynamicDataSourceModel;
import com.viroyal.doormagnet.common.system.vo.LoginUser;
import com.viroyal.doormagnet.common.util.IPUtils;
import com.viroyal.doormagnet.common.util.SpringContextUtils;
import com.viroyal.doormagnet.common.util.SysAnnmentTypeEnum;
import com.viroyal.doormagnet.common.util.oConvertUtils;
import com.viroyal.doormagnet.common.util.oss.OssBootUtil;
import com.viroyal.doormagnet.modules.message.websocket.WebSocket;
import com.viroyal.doormagnet.modules.system.mapper.SysAnnouncementMapper;
import com.viroyal.doormagnet.modules.system.mapper.SysAnnouncementSendMapper;
import com.viroyal.doormagnet.modules.system.mapper.SysLogMapper;
import com.viroyal.doormagnet.modules.system.mapper.SysRoleMapper;
import com.viroyal.doormagnet.modules.system.mapper.SysUserMapper;
import com.viroyal.doormagnet.modules.system.mapper.SysUserRoleMapper;
import com.viroyal.doormagnet.modules.system.model.SysAnnouncement;
import com.viroyal.doormagnet.modules.system.model.SysAnnouncementSend;
import com.viroyal.doormagnet.modules.system.model.SysAnnouncementWithBLOBs;
import com.viroyal.doormagnet.modules.system.model.SysLog;
import com.viroyal.doormagnet.modules.system.model.SysRole;
import com.viroyal.doormagnet.modules.system.model.SysUser;
import com.viroyal.doormagnet.modules.system.util.MinioUtil;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author: scott
 * @Date:2019-4-20 
 * @Version:V1.0
 */
@Service
public class SysBaseApiImpl implements ISysBaseAPI {
    private static final Logger Logger = LoggerFactory.getLogger(SysBaseApiImpl.class);	
	/** 当前系统数据库类型 */
	public static String DB_TYPE = "";
	@Resource
	private SysLogMapper sysLogMapper;
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	private SysAnnouncementMapper sysAnnouncementMapper;
	@Autowired
	private SysAnnouncementSendMapper sysAnnouncementSendMapper;	
	@Resource
    private WebSocket webSocket;
	@Resource
	private SysRoleMapper roleMapper;



	@Override
	public void addLog(String LogContent, Integer logType, Integer operatetype) {
		SysLog sysLog = new SysLog();
		//注解上的描述,操作日志内容
		sysLog.setLogContent(LogContent);
		sysLog.setLogType(logType);
		sysLog.setOperateType(operatetype);

		//请求的方法名
		//请求的参数

		try {
			//获取request
			HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
			//设置IP地址
			sysLog.setIp(IPUtils.getIpAddr(request));
		} catch (Exception e) {
			sysLog.setIp("127.0.0.1");
		}

		//获取登录用户信息
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		if(sysUser!=null){
			sysLog.setUserid(sysUser.getUsername());
			sysLog.setUsername(sysUser.getRealname());

		}
		sysLog.setCreateTime(new Date());
		//保存系统日志
		sysLogMapper.insert(sysLog);
	}

	@Override
	@Cacheable(cacheNames=CacheConstant.SYS_USERS_CACHE, key="#username")
	public LoginUser getUserByName(String username) {
		if(oConvertUtils.isEmpty(username)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		SysUser sysUser = userMapper.getUserByName(username);
		if(sysUser==null) {
			return null;
		}
		BeanUtils.copyProperties(sysUser, loginUser);
		return loginUser;
	}
	
	@Override
	public LoginUser getUserById(String id) {
		if(oConvertUtils.isEmpty(id)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		SysUser sysUser = userMapper.selectByPrimaryKey(id);
		if(sysUser==null) {
			return null;
		}
		BeanUtils.copyProperties(sysUser, loginUser);
		return loginUser;
	}

	@Override
	public List<String> getRolesByUsername(String username) {
		return sysUserRoleMapper.getRoleByUserName(username);
	}



	@Override
	public String getDatabaseType() throws SQLException {
		DataSource dataSource = SpringContextUtils.getApplicationContext().getBean(DataSource.class);
		return getDatabaseTypeByDataSource(dataSource);
	}



	@Override
	public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent) {
		this.sendSysAnnouncement(fromUser, toUser, title, msgContent, CommonConstant.MSG_CATEGORY_2);
	}

	@Override
	public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory) {
		SysAnnouncementWithBLOBs announcement = new SysAnnouncementWithBLOBs();
		announcement.setTitile(title);
		announcement.setMsgContent(msgContent);
		announcement.setSender(fromUser);
		announcement.setPriority(CommonConstant.PRIORITY_M);
		announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(new Date());
		announcement.setMsgCategory(setMsgCategory);
		announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
		sysAnnouncementMapper.insert(announcement);
		// 2.插入用户通告阅读标记表记录
		String userId = toUser;
		String[] userIds = userId.split(",");
		String anntId = announcement.getId();
		for(int i=0;i<userIds.length;i++) {
			if(oConvertUtils.isNotEmpty(userIds[i])) {
				SysUser sysUser = userMapper.getUserByName(userIds[i]);
				if(sysUser==null) {
					continue;
				}
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(sysUser.getId());
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				sysAnnouncementSendMapper.insert(announcementSend);
				JSONObject obj = new JSONObject();
		    	obj.put("cmd", "user");
		    	obj.put("userId", sysUser.getId());
				obj.put("msgId", announcement.getId());
				obj.put("msgTxt", announcement.getTitile());
		    	webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
			}
		}

	}

	@Override
	public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory, String busType, String busId) {
		SysAnnouncementWithBLOBs announcement = new SysAnnouncementWithBLOBs();
		announcement.setTitile(title);
		announcement.setMsgContent(msgContent);
		announcement.setSender(fromUser);
		announcement.setPriority(CommonConstant.PRIORITY_M);
		announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(new Date());
		announcement.setMsgCategory(setMsgCategory);
		announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
		announcement.setBusId(busId);
		announcement.setBusType(busType);
		announcement.setOpenType(SysAnnmentTypeEnum.getByType(busType).getOpenType());
		announcement.setOpenPage(SysAnnmentTypeEnum.getByType(busType).getOpenPage());
		sysAnnouncementMapper.insert(announcement);
		// 2.插入用户通告阅读标记表记录
		String userId = toUser;
		String[] userIds = userId.split(",");
		String anntId = announcement.getId();
		for(int i=0;i<userIds.length;i++) {
			if(oConvertUtils.isNotEmpty(userIds[i])) {
				SysUser sysUser = userMapper.getUserByName(userIds[i]);
				if(sysUser==null) {
					continue;
				}
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(sysUser.getId());
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				sysAnnouncementSendMapper.insert(announcementSend);
				JSONObject obj = new JSONObject();
				obj.put("cmd", "user");
				obj.put("userId", sysUser.getId());
				obj.put("msgId", announcement.getId());
				obj.put("msgTxt", announcement.getTitile());
				webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
			}
		}
	}

	@Override
	public void sendSysAnnouncement(String fromUser, String toUser,String title,Map<String, String> map, String templateCode) {
//		List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
//        if(sysSmsTemplates==null||sysSmsTemplates.size()==0){
//            throw new JeecgBootException("消息模板不存在，模板编码："+templateCode);
//        }
//		SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
//		//模板标题
//		title = title==null?sysSmsTemplate.getTemplateName():title;
//		//模板内容
//		String content = sysSmsTemplate.getTemplateContent();
//		if(map!=null) {
//			for (Map.Entry<String, String> entry : map.entrySet()) {
//				String str = "${" + entry.getKey() + "}";
//				title = title.replace(str, entry.getValue());
//				content = content.replace(str, entry.getValue());
//			}
//		}
//
//		SysAnnouncementWithBLOBs announcement = new SysAnnouncementWithBLOBs();
//		announcement.setTitile(title);
//		announcement.setMsgContent(content);
//		announcement.setSender(fromUser);
//		announcement.setPriority(CommonConstant.PRIORITY_M);
//		announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
//		announcement.setSendStatus(CommonConstant.HAS_SEND);
//		announcement.setSendTime(new Date());
//		announcement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);
//		announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
//		sysAnnouncementMapper.insert(announcement);
//		// 2.插入用户通告阅读标记表记录
//		String userId = toUser;
//		String[] userIds = userId.split(",");
//		String anntId = announcement.getId();
//		for(int i=0;i<userIds.length;i++) {
//			if(oConvertUtils.isNotEmpty(userIds[i])) {
//				SysUser sysUser = userMapper.getUserByName(userIds[i]);
//				if(sysUser==null) {
//					continue;
//				}
//				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
//				announcementSend.setAnntId(anntId);
//				announcementSend.setUserId(sysUser.getId());
//				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
//				sysAnnouncementSendMapper.insert(announcementSend);
//				JSONObject obj = new JSONObject();
//				obj.put("cmd", "user");
//				obj.put("userId", sysUser.getId());
//				obj.put("msgId", announcement.getId());
//				obj.put("msgTxt", announcement.getTitile());
//				webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
//			}
//		}
	}

	@Override
	public void sendSysAnnouncement(String fromUser, String toUser, String title, Map<String, String> map, String templateCode, String busType, String busId) {
//		List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
//		if(sysSmsTemplates==null||sysSmsTemplates.size()==0){
//			throw new JeecgBootException("消息模板不存在，模板编码："+templateCode);
//		}
//		SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
//		//模板标题
//		title = title==null?sysSmsTemplate.getTemplateName():title;
//		//模板内容
//		String content = sysSmsTemplate.getTemplateContent();
//		if(map!=null) {
//			for (Map.Entry<String, String> entry : map.entrySet()) {
//				String str = "${" + entry.getKey() + "}";
//				title = title.replace(str, entry.getValue());
//				content = content.replace(str, entry.getValue());
//			}
//		}
//		SysAnnouncementWithBLOBs announcement = new SysAnnouncementWithBLOBs();
//		announcement.setTitile(title);
//		announcement.setMsgContent(content);
//		announcement.setSender(fromUser);
//		announcement.setPriority(CommonConstant.PRIORITY_M);
//		announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
//		announcement.setSendStatus(CommonConstant.HAS_SEND);
//		announcement.setSendTime(new Date());
//		announcement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);
//		announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
//		announcement.setBusId(busId);
//		announcement.setBusType(busType);
//		announcement.setOpenType(SysAnnmentTypeEnum.getByType(busType).getOpenType());
//		announcement.setOpenPage(SysAnnmentTypeEnum.getByType(busType).getOpenPage());
//		sysAnnouncementMapper.insert(announcement);
//		// 2.插入用户通告阅读标记表记录
//		String userId = toUser;
//		String[] userIds = userId.split(",");
//		String anntId = announcement.getId();
//		for(int i=0;i<userIds.length;i++) {
//			if(oConvertUtils.isNotEmpty(userIds[i])) {
//				SysUser sysUser = userMapper.getUserByName(userIds[i]);
//				if(sysUser==null) {
//					continue;
//				}
//				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
//				announcementSend.setAnntId(anntId);
//				announcementSend.setUserId(sysUser.getId());
//				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
//				sysAnnouncementSendMapper.insert(announcementSend);
//				JSONObject obj = new JSONObject();
//				obj.put("cmd", "user");
//				obj.put("userId", sysUser.getId());
//				obj.put("msgId", announcement.getId());
//				obj.put("msgTxt", announcement.getTitile());
//				webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
//			}
//		}
	}

	/**
	 * 获取数据库类型
	 * @param dataSource
	 * @return
	 * @throws SQLException
	 */
	private String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException{
		if("".equals(DB_TYPE)) {
			Connection connection = dataSource.getConnection();
			try {
				DatabaseMetaData md = connection.getMetaData();
				String dbType = md.getDatabaseProductName().toLowerCase();
				if(dbType.indexOf("mysql")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_MYSQL;
				}else if(dbType.indexOf("oracle")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_ORACLE;
				}else if(dbType.indexOf("sqlserver")>=0||dbType.indexOf("sql server")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_SQLSERVER;
				}else if(dbType.indexOf("postgresql")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_POSTGRESQL;
				}else {
					throw new JeecgBootException("数据库类型:["+dbType+"]不识别!");
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}finally {
				connection.close();
			}
		}
		return DB_TYPE;
		
	}





	@Override
	public List<ComboModel> queryAllUser() {
//		List<ComboModel> list = new ArrayList<ComboModel>();
//		List<SysUser> userList = userMapper.selectList(new QueryWrapper<SysUser>().eq("status","1").eq("del_flag","0"));
//		for(SysUser user : userList){
//			ComboModel model = new ComboModel();
//			model.setTitle(user.getRealname());
//			model.setId(user.getId());
//			model.setUsername(user.getUsername());
//			list.add(model);
//		}
//		return list;
		
        return  null;        
	}

    @Override
    public JSONObject queryAllUser(String[] userIds,int pageNo,int pageSize) {
//		JSONObject json = new JSONObject();
//		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status","1").eq("del_flag","0");
//        List<ComboModel> list = new ArrayList<ComboModel>();
//		Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
//		IPage<SysUser> pageList = userMapper.selectPage(page, queryWrapper);
//        for(SysUser user : pageList.getRecords()){
//            ComboModel model = new ComboModel();
//            model.setUsername(user.getUsername());
//            model.setTitle(user.getRealname());
//            model.setId(user.getId());
//            model.setEmail(user.getEmail());
//            if(oConvertUtils.isNotEmpty(userIds)){
//                for(int i = 0; i<userIds.length;i++){
//                    if(userIds[i].equals(user.getId())){
//                        model.setChecked(true);
//                    }
//                }
//            }
//            list.add(model);
//        }
//		json.put("list",list);
//        json.put("total",pageList.getTotal());
//        return json;
        
        return  null;

    }

	@Override
	public List<ComboModel> queryAllRole() {
//		List<ComboModel> list = new ArrayList<ComboModel>();
//		List<SysRole> roleList = roleMapper.selectList(new QueryWrapper<SysRole>());
//		for(SysRole role : roleList){
//			ComboModel model = new ComboModel();
//			model.setTitle(role.getRoleName());
//			model.setId(role.getId());
//			list.add(model);
//		}
//		return list;
		
        return  null;

        
	}

    @Override
    public List<ComboModel> queryAllRole(String[] roleIds) {
//        List<ComboModel> list = new ArrayList<ComboModel>();
//        List<SysRole> roleList = roleMapper.selectList(new QueryWrapper<SysRole>());
//        for(SysRole role : roleList){
//            ComboModel model = new ComboModel();
//            model.setTitle(role.getRoleName());
//            model.setId(role.getId());
//            model.setRoleCode(role.getRoleCode());
//            if(oConvertUtils.isNotEmpty(roleIds)) {
//                for (int i = 0; i < roleIds.length; i++) {
//                    if (roleIds[i].equals(role.getId())) {
//                        model.setChecked(true);
//                    }
//                }
//            }
//            list.add(model);
//        }
//        return list;
        
        return  null;
    }

	@Override
	public List<String> getRoleIdsByUsername(String username) {
		return sysUserRoleMapper.getRoleIdByUserName(username);
	}



	@Override
	public String upload(MultipartFile file,String bizPath,String uploadType) {
		String url = "";
		if(CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)){
			url = MinioUtil.upload(file,bizPath);
		}else{
			url = OssBootUtil.upload(file,bizPath);
		}
		return url;
	}

	@Override
	public List<String> getDepartIdsByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDepartNamesByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}