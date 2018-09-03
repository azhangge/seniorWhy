CREATE TABLE `banner` (
  `id` char(36) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `sort_number` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `create_user_id` bigint(20) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  `custom_id` varchar(50) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `custom_type` varchar(50) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `web_store_object_id` varchar(36) DEFAULT NULL,
  `app_store_object_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `recommend_course` (
  `id` char(36) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `sort_number` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `create_user_id` bigint(20) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  `custom_id` varchar(50) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `custom_type` varchar(50) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `custom_order_by` int(11) DEFAULT NULL,
  `custom_id_title` varchar(50) DEFAULT NULL,
  `custom_id_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `recommend_tag` (
  `id` char(36) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `sort_number` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `create_user_id` bigint(20) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  `custom_id` varchar(50) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `custom_type` varchar(50) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `custom_order_by` int(11) DEFAULT NULL,
  `app_store_object_id` varchar(36) DEFAULT NULL,
  `custom_id_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;