
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




