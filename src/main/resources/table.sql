
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
  `time` varchar(50) DEFAULT NULL COMMENT '状态上传时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `t_device_switch_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `switchControlOne` int(11) DEFAULT NULL COMMENT '控制器1对应灯的开关',  
  `switchControlTwo` int(11) DEFAULT NULL COMMENT '控制器2对应灯的开关',  
  `switchControlThree` int(11) DEFAULT NULL COMMENT '控制器3对应灯的开关',  
  `mid` char(2) DEFAULT '11' COMMENT 'Mid',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `t_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `region` char(20) DEFAULT NULL COMMENT '设备地区编码',  
  `time` varchar(50) DEFAULT NULL COMMENT '设备创建时间',    
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_device_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` char(15) DEFAULT NULL COMMENT '设备id',
  `brand` varchar(100) DEFAULT NULL COMMENT '设备品牌',  
  `model` varchar(100) DEFAULT NULL COMMENT '设备型号',
  `param_desc` varchar(500) DEFAULT NULL COMMENT '设备参数描述',      
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




