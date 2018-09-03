CREATE TABLE `tag_category_relation` (
  `id` int(11) AUTO_INCREMENT NOT NULL ,
  `tag_id` CHAR(36) NULL DEFAULT NULL COMMENT '101标签id',
  `nd_code` VARCHAR(30) NULL DEFAULT NULL COMMENT 'NDR分类code',
  `nd_path` VARCHAR(2000) NULL DEFAULT NULL COMMENT 'NDR的pattern_path',
  create_time       TIMESTAMP NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  create_user_id    BIGINT(20) DEFAULT NULL
  COMMENT '创建人',
  update_time       TIMESTAMP NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 DEFAULT NULL
  COMMENT '修改时间',
  update_user_id    BIGINT(20) DEFAULT NULL
  COMMENT '修改人',
  `project_id`      BIGINT(20) DEFAULT NULL
  COMMENT '项目标识',
  `custom_type`     VARCHAR(50) DEFAULT NULL
  COMMENT '自定义类型',
  `status`     TINYINT DEFAULT 1
  COMMENT '状态：0-标签已被删除不可用，1-可用',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '101标签和NDR分类映射关系表';


CREATE TABLE ndr_import_relation (
  `id` INT AUTO_INCREMENT NOT NULL,
  `ndr_id` CHAR(36)  NULL COMMENT 'ndr的教材id',
  `unit_id` INT  NULL COMMENT '基础平台元课程id',
  `course_id` CHAR(36)  NULL COMMENT '资源课程id',
  create_time       TIMESTAMP NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  create_user_id    BIGINT(20) DEFAULT NULL
  COMMENT '创建人',
  update_time       TIMESTAMP NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 DEFAULT NULL
  COMMENT '修改时间',
  update_user_id    BIGINT(20) DEFAULT NULL
  COMMENT '修改人',
  `project_id`      BIGINT(20) DEFAULT NULL
  COMMENT '项目标识',
  `custom_type`     VARCHAR(50) DEFAULT NULL
  COMMENT '自定义类型',
  `custom_id`       VARCHAR(50) DEFAULT NULL
  COMMENT '自定义ID',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = 'ndr教材导入映射表';