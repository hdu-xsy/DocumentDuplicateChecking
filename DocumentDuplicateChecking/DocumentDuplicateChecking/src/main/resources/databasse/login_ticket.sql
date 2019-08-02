CREATE TABLE `login_ticket`
(
    `id`      INT         NOT NULL AUTO_INCREMENT,
    `user_id` INT         NOT NULL,
    `ticket`  VARCHAR(45) NOT NULL,
    `expired` DATETIME    NOT NULL,
    `status`  INT         NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `ticket_UNIQUE` (`ticket` ASC)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;