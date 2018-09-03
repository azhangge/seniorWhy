create table message
(
   id                   char(36) not null comment '标识',
   jump_type            varchar(100) comment '消息跳转页面',
   jump_param           varchar(500),
   link_id              char(36) comment '消息中心消息id',
   create_time          timestamp NULL default CURRENT_TIMESTAMP,
   create_user_id       bigint(20) default NULL,
   update_time          timestamp NULL default NULL,
   update_user_id       bigint(20) default NULL,
   custom_type varchar(50) DEFAULT NULL,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;