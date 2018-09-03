ALTER TABLE `recommend_course`
  ADD `recommend_mode` TINYINT NOT  NULL DEFAULT '0' COMMENT '推荐模式:0--标签推荐 1--自定义推荐' ;

CREATE TABLE `recommend_course_detail` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '序号' ,
  `recommend_id` CHAR(36) NOT NULL COMMENT '推荐课程id' ,
  `custom_id` VARCHAR(50) NOT NULL COMMENT '自定义id.代表公开课/培训/测评中心的id.' ,
  `custom_type` VARCHAR(50) NOT NULL COMMENT '自定义类型.代表公开课/培训/测评中心的类型.' ,
  `sort_number` INT NOT NULL COMMENT '推荐的排序' ,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '课程推荐明细';


CREATE TABLE `recommend_faq` (
  `_id` int(20) NOT NULL AUTO_INCREMENT,
  `id` char(36) NOT NULL,
  `question_name` varchar(1000) DEFAULT NULL,
  `question_answer` varchar(4000) DEFAULT NULL,
  `custom_type` varchar(50) DEFAULT NULL,
  `ask_user_id` bigint(20) DEFAULT NULL,
  `answer_user_id` bigint(20) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `sort_number` int(11) DEFAULT NULL,
  `question_type` tinyint(4) DEFAULT NULL,
   `query_text` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`_id`)
) ENGINE=InnoDB;