ALTER TABLE `kv_info`
  ADD `isolation_strategy` INT NOT NULL COMMENT '隔离策略：0(项目隔离),1(全站共享)' AFTER `kv_key`,
  ADD `isolation_param` VARCHAR(36) NOT NULL COMMENT '隔离参数' AFTER `isolation_strategy` ,
  ADD _id int not null auto_increment comment '标识' FIRST,
  ADD id  CHAR(36) NOT NULL comment '序号' AFTER `_id`,
  DROP PRIMARY KEY,
  ADD PRIMARY KEY(
     `_id`),
  ADD UNIQUE( `kv_key`, `isolation_strategy`, `isolation_param`)
  ;

update kv_info set isolation_strategy=1,isolation_param=0,id=UUID();

CREATE TABLE `recommend_point` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `reward_user_id` bigint(20) DEFAULT NULL,
  `reward_user_name` varchar(50) DEFAULT NULL,
  `operate_user_name` varchar(50) DEFAULT NULL,
  `points` int(10) DEFAULT NULL,
  `experiences` int(10) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `reward_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ,
  `reward_description` varchar(1024) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;


