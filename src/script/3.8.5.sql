--
-- 表的结构 `kv_info`
--

CREATE TABLE IF NOT EXISTS `kv_info` (
  `kv_key` varchar(100) NOT NULL,
  `kv_value` varchar(500) DEFAULT NULL,
  `group_key` varchar(100) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `kv_info`
ADD PRIMARY KEY (`kv_key`);