CREATE TABLE activity
(
  _id               INT AUTO_INCREMENT PRIMARY KEY
  COMMENT '标识',
  id                CHAR(36) NOT NULL
  COMMENT '序号',
  create_time       TIMESTAMP NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  create_user_id    BIGINT(20) DEFAULT NULL
  COMMENT '创建人',
  update_time       TIMESTAMP NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 DEFAULT NULL
  COMMENT '修改时间',
  update_user_id    BIGINT(20) DEFAULT NULL
  COMMENT '修改人',
  `custom_id`       VARCHAR(50) DEFAULT NULL
  COMMENT '自定义ID',
  `project_id`      BIGINT(20) DEFAULT NULL
  COMMENT '项目标识',
  `custom_type`     VARCHAR(50) DEFAULT NULL
  COMMENT '自定义类型',
  `title`           VARCHAR(255) DEFAULT NULL
  COMMENT '活动名称',
  `enabled`          TINYINT(4) DEFAULT NULL
  COMMENT '活动是否启用：0-未未启用，1启用',
  start_time        TIMESTAMP NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 DEFAULT NULL
  COMMENT '活动开始时间',
  end_time          TIMESTAMP NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 DEFAULT NULL
  COMMENT '活动结束时间',
  reward_points     INT NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 DEFAULT NULL
  COMMENT '奖励积分',
  reward_experience INT NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 DEFAULT NULL
  COMMENT '奖励经验',
  join_object_type  INT NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 DEFAULT NULL
  COMMENT '参加人员类型（1:人员,2:组织,3:部门）',
  join_object       VARCHAR(2000) NULL DEFAULT NULL
  COMMENT '参加人员',
  join_object_org_id       BIGINT(20) NULL DEFAULT NULL
  COMMENT '参加人员组织id',
  `description`     VARCHAR(500) DEFAULT NULL
  COMMENT '活动名称',
  task_type         TINYINT(4) NULL DEFAULT NULL
  COMMENT '任务类型，1：ERP日常活动，0:PBL活动',
  `task_id`         VARCHAR(100) DEFAULT NULL
  COMMENT '任务id',
  `target_cmp_url`  VARCHAR(300) DEFAULT NULL
  COMMENT '最终跳转的cmp地址',
  `jump_cmp_url`  VARCHAR(300) DEFAULT NULL
  COMMENT '互动跳转的cmp地址',
  activity_type         TINYINT(4) NULL DEFAULT NULL
  COMMENT '活动类型，0:一日三题',
  activity_finish_type         TINYINT(4) NULL DEFAULT NULL
  COMMENT '活动完成标准,1:参加，2:通过'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- 表的结构 `user_info`
--

CREATE TABLE IF NOT EXISTS `user_info` (
  `user_id` bigint(20) NOT NULL COMMENT 'UC账号ID',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `account` varchar(50) DEFAULT NULL COMMENT '登录账号名',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `org_id` bigint(20) DEFAULT NULL COMMENT 'UC组织ID',
  `org_name` varchar(200) DEFAULT NULL COMMENT 'UC组织名称',
  `reg_date` timestamp NULL DEFAULT NULL  COMMENT '注册时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `user_info`
ADD PRIMARY KEY (`user_id`);

--
-- 表的结构 `exam_activity_log`
--

CREATE TABLE IF NOT EXISTS `exam_activity_log` (
  `id` int(11) AUTO_INCREMENT NOT NULL  PRIMARY KEY,
  `message` varchar(1000) DEFAULT NULL COMMENT 'MQ消息内容',
  `status` tinyint(4) DEFAULT NULL COMMENT '处理状态：0失败，1成功',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户标识',
  `activity_id` varchar(50) DEFAULT NULL COMMENT '活动标识',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  `content` varchar(1000) DEFAULT NULL COMMENT '日志内容',
  `task_type` tinyint(4) DEFAULT NULL COMMENT '任务类型'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;






