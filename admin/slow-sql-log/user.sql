-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(31) NOT NULL DEFAULT '' COMMENT '姓名',
  `password` varchar(63) NOT NULL DEFAULT '' COMMENT '密码',
  `create_time` date NOT NULL DEFAULT '0000-00-00' COMMENT '创建时间',
  `update_time` date NOT NULL DEFAULT '0000-00-00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
