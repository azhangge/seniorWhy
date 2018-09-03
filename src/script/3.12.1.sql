-- 添加组织类型字段
ALTER TABLE `activity`
ADD COLUMN `join_object_org_type`  tinyint(4) NULL DEFAULT 0 COMMENT '组织类型，0:实体组织 1:虚拟组织' AFTER `join_object_org_id` ;
