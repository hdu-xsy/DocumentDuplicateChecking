CREATE TABLE `sys_user`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `email`        varchar(255)        DEFAULT NULL,
    `is_disable`   bit(1)              DEFAULT NULL,
    `mobile_no`    varchar(255)        DEFAULT NULL,
    `password`     varchar(255)        DEFAULT NULL,
    `salt`         varchar(255)        DEFAULT NULL,
    `username`     varchar(255)        DEFAULT NULL,
    `mobile_phone` varchar(20)         DEFAULT NULL COMMENT '常用手机号',
    `user_type`    int(11)             DEFAULT NULL COMMENT '0-运营后台用户 1-非运营后台用户 2超级管理员 3管理人员',
    `nickname`     varchar(255)        DEFAULT NULL,
    `created_at`   timestamp  NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `updated_at`   timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;