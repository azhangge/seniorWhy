-- 更新sortNumber

-- 删除custom_id_title(course等 )

-- 增加web_url app_url
ALTER TABLE `banner` ADD `web_url` VARCHAR(1000) NULL DEFAULT NULL , ADD `app_url` VARCHAR(1000) NULL DEFAULT NULL ;