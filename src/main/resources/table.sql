
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;