
/*设备定时上报状态信息*/
CREATE TABLE `t_device_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `voltage` int(11) DEFAULT NULL COMMENT '电压',
  `current` int(11) DEFAULT NULL COMMENT '电流',
  `activePower` int(11) DEFAULT NULL COMMENT '有功功率',
  `reactivePower` int(11) DEFAULT NULL COMMENT '无功功率',
  `powerfactor` int(11) DEFAULT NULL COMMENT '功率因数',
  `temperature` int(11) DEFAULT NULL COMMENT '温度',
  `powerConsumptionIntegerPart` int(11) DEFAULT NULL COMMENT '耗电量整数部分',
  `powerConsumptionDecimalPart` int(11) DEFAULT NULL COMMENT '耗电量小数部分',
  `brightnessControlOne` int(11) DEFAULT NULL COMMENT '控制器1对应灯的亮度',
  `brightnessControlTwo` int(11) DEFAULT NULL COMMENT '控制器2对应灯的亮度',
  `brightnessControlThree` int(11) DEFAULT NULL COMMENT '控制器3对应灯的亮度',
  `switchControlOne` int(11) DEFAULT NULL COMMENT '控制器1对应灯的开关',
  `switchControlTwo` int(11) DEFAULT NULL COMMENT '控制器2对应灯的开关',
  `switchControlThree` int(11) DEFAULT NULL COMMENT '控制器3对应灯的开关',
  `signalStrengthAbsoluteValue` int(11) DEFAULT NULL COMMENT '信号强度的绝对值',
  `dimmingMode` int(11) DEFAULT NULL COMMENT '调光方式',
  `abnormalFlag` int(11) DEFAULT NULL COMMENT '异常标志',
  `angleOne` int(11) DEFAULT NULL COMMENT '倾斜角度1',
  `angleTwo` int(11) DEFAULT NULL COMMENT '倾斜角度2',
  `angleOriginalOne` int(11) DEFAULT NULL COMMENT '设备初始倾斜角度1',
  `angleOriginalTwo` int(11) DEFAULT NULL COMMENT '设备初始倾斜角度2',  
  `time` datetime DEFAULT NULL COMMENT '状态上传时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设备请求读取时间*/
CREATE TABLE `t_device_read_time` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设备上报CELLID*/
CREATE TABLE `t_device_report_cellid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `cellid` int(11) DEFAULT NULL COMMENT 'cellid',  
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*设备上报软件版本号*/
CREATE TABLE `t_device_report_softversion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `softversion` varchar(500) DEFAULT NULL COMMENT '上报软件版本号',  
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设备上报固件版本号*/
CREATE TABLE `t_device_report_hardversion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `hardversion` varchar(500) DEFAULT NULL COMMENT '上报固件版本号',  
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设备上报开关灯异常报警*/
CREATE TABLE `t_device_report_lightabnormal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `lightstatus` TINYINT DEFAULT NULL COMMENT '灯开关状态',  
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设备上报电流异常报警*/
CREATE TABLE `t_device_report_currentabnormal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `currentstatus` TINYINT DEFAULT NULL COMMENT '电流状态',  
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*设备上报倾斜器异常报警*/
CREATE TABLE `t_device_report_angleabnormal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `abnormalflag` TINYINT DEFAULT NULL COMMENT '倾斜器报警标志',  
  `angleOne` int(11) DEFAULT NULL COMMENT '倾斜角度1',
  `angleTwo` int(11) DEFAULT NULL COMMENT '倾斜角度2',
  `angleOriginalOne` int(11) DEFAULT NULL COMMENT '设备初始倾斜角度1',
  `angleOriginalTwo` int(11) DEFAULT NULL COMMENT '设备初始倾斜角度2',  
  `angleThreadhold` int(11) DEFAULT NULL COMMENT '角度阈值',      
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设备上报耗电量*/
CREATE TABLE `t_device_report_powerconsumptionabnormal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `powerConsumptionIntegerPart` int(11) DEFAULT NULL COMMENT '耗电量整数部分',
  `powerConsumptionDecimalPart` int(11) DEFAULT NULL COMMENT '耗电量小数部分', 
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设备保持心跳*/
CREATE TABLE `t_device_report_heartbeat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设备对服务器的回复*/
CREATE TABLE `t_device_response` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `controlHexStr` varchar(10) DEFAULT NULL COMMENT '控制位',  
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',     
  `errorCode` TINYINT DEFAULT NULL COMMENT 'errorCode',        
  `time` datetime DEFAULT NULL COMMENT '信息时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*服务器对设备的回复*/
CREATE TABLE `t_service_response` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `control` int(11) DEFAULT NULL COMMENT '控制位',  
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',     
  `errorCode` TINYINT DEFAULT NULL COMMENT 'errorCode',        
  `time` datetime DEFAULT NULL COMMENT '信息时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*设置控制器1~3路灯开/关*/
CREATE TABLE `t_service_settings_device_switch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `switchControlOne` int(11) DEFAULT NULL COMMENT '控制器1对应灯的开关',  
  `switchControlTwo` int(11) DEFAULT NULL COMMENT '控制器2对应灯的开关',  
  `switchControlThree` int(11) DEFAULT NULL COMMENT '控制器3对应灯的开关',  
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',    
  `time` datetime DEFAULT NULL COMMENT '设置信息发出时间',      
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设置1~3路灯的亮度等级*/
CREATE TABLE `t_service_settings_device_brightness` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `brightnessControlOne` int(11) DEFAULT NULL COMMENT '控制器1对应灯的亮度',  
  `brightnessControlTwo` int(11) DEFAULT NULL COMMENT '控制器2对应灯的亮度',  
  `brightnessControlThree` int(11) DEFAULT NULL COMMENT '控制器3对应灯的亮度',  
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',    
  `time` datetime DEFAULT NULL COMMENT '设置信息发出时间',      
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设置设备定时上报时间间隔*/
CREATE TABLE `t_service_settings_device_reportinterval` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `reportInterval` int(11) DEFAULT NULL COMMENT '设备定时上报时间间隔',  
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',    
  `time` datetime DEFAULT NULL COMMENT '设置信息发出时间',      
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*设置1~3路灯的亮灯策略*/
CREATE TABLE `t_service_settings_device_lightingstrategy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `lightNum` int(11) DEFAULT NULL COMMENT '控制器上灯编号',  
  `strategyNum` int(11) DEFAULT NULL COMMENT '策略编号',  
  `strategyPeriod` int(11) DEFAULT NULL COMMENT '策略周期',  
  `timeNum` int(11) DEFAULT NULL COMMENT '时间段个数',  
  `timehex` text DEFAULT NULL COMMENT '时间段十六进制数组',    
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',    
  `time` datetime DEFAULT NULL COMMENT '设置信息发出时间',      
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*设置时间给设备*/
CREATE TABLE `t_service_settings_device_time` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `year` int(11) DEFAULT NULL COMMENT '年',  
  `month` int(11) DEFAULT NULL COMMENT '月',  
  `day` int(11) DEFAULT NULL COMMENT '日',  
  `hour` int(11) DEFAULT NULL COMMENT '小时',  
  `minute` int(11) DEFAULT NULL COMMENT '分钟', 
  `second` int(11) DEFAULT NULL COMMENT '秒',      
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',    
  `time` datetime DEFAULT NULL COMMENT '设置信息发出时间',      
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*设置安装状态及角度阀值*/
CREATE TABLE `t_service_settings_device_installationstate_anglethreadhold` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `installationStable` int(11) DEFAULT NULL COMMENT '设备是否已经安装稳定',  
  `angleThreadhold` int(11) DEFAULT NULL COMMENT '设备角度阀值',    
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',    
  `time` datetime DEFAULT NULL COMMENT '设置信息发出时间',      
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*设置耗电量*/
CREATE TABLE `t_service_settings_device_powerconsumption` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `powerConsumptionIntegerPart` int(11) DEFAULT NULL COMMENT '耗电量整数部分',
  `powerConsumptionDecimalPart` int(11) DEFAULT NULL COMMENT '耗电量小数部分', 
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',    
  `time` datetime DEFAULT NULL COMMENT '设置信息发出时间',      
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `t_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `region` bigint(20) DEFAULT NULL COMMENT '设备地区编码',  
  `brand` varchar(100) DEFAULT NULL COMMENT '设备品牌',  
  `model` varchar(100) DEFAULT NULL COMMENT '设备型号',
  `param_desc` varchar(500) DEFAULT NULL COMMENT '设备参数描述',        
  `time` datetime DEFAULT NULL COMMENT '设备创建时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_region` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `continent_id` char(2) DEFAULT NULL COMMENT '洲编码',  
  `continent_name` varchar(30) DEFAULT NULL COMMENT '洲名称',
  `country_id` char(3) DEFAULT NULL COMMENT '国家编码',  
  `country_name` varchar(100) DEFAULT NULL COMMENT '国家名称',  
  `province_id` char(3) DEFAULT NULL COMMENT '省编码',  
  `province_name` varchar(100) DEFAULT NULL COMMENT '省名称',  
  `city_id` char(3) DEFAULT NULL COMMENT '市/县编码',  
  `city_name` varchar(100) DEFAULT NULL COMMENT '市/县名称',  
  `town_id` char(3) DEFAULT NULL COMMENT '镇/区编码',  
  `town_name` varchar(100) DEFAULT NULL COMMENT '镇/区名称',  
  `village_id` char(3) DEFAULT NULL COMMENT '村编码',  
  `village_name` varchar(100) DEFAULT NULL COMMENT '村名称',  
  `road_id` char(3) DEFAULT NULL COMMENT '路编码',  
  `road_name` varchar(100) DEFAULT NULL COMMENT '路名称',  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `t_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT 'imei',
  `responsecontrolHexStr` varchar(10) DEFAULT NULL COMMENT '回复控制位',    
  `headHexStr` varchar(10) DEFAULT '6F01' COMMENT '报文头',  
  `flagHexStr` varchar(10) DEFAULT NULL COMMENT '标志位',  
  `controlHexStr` varchar(10) DEFAULT NULL COMMENT '控制位',  
  `versionHexStr` varchar(10) DEFAULT NULL COMMENT '版本位',  
  `contentLengthHexStr` varchar(10) DEFAULT NULL COMMENT '数据区长度',  
  `contentHexStr` varchar(300) DEFAULT NULL COMMENT '数据区内容',  
  `endsHexStr` varchar(16) DEFAULT '0D0A0D0A' COMMENT '结束符',  
  
  `time` datetime DEFAULT NULL COMMENT '信息获取时间',    
  PRIMARY KEY (`id`),
  UNIQUE KEY `imei`(`imei`,`controlHexStr`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构编码',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '性别(1-正常,2-冻结)',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
  `activiti_sync` tinyint(1) NULL DEFAULT NULL COMMENT '同步工作流引擎(1-同步,0-不同步)',
  `work_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号，唯一键',
  `post` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务，关联职务表',
  `telephone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '座机号',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `identity` tinyint(1) NULL DEFAULT NULL COMMENT '身份（1普通成员 2上级）',
  `depart_ids` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '负责部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_user_name`(`username`) USING BTREE,
  UNIQUE INDEX `uniq_sys_user_work_no`(`work_no`) USING BTREE,
  INDEX `index_user_status`(`status`) USING BTREE,
  INDEX `index_user_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_sys_role_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_group_role_per_id`(`role_id`, `permission_id`) USING BTREE,
  INDEX `index_group_role_id`(`role_id`) USING BTREE,
  INDEX `index_group_per_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index2_groupuu_user_id`(`user_id`) USING BTREE,
  INDEX `index2_groupuu_ole_id`(`role_id`) USING BTREE,
  INDEX `index2_groupuu_useridandroleid`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;


