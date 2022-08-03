SET MODE MYSQL;
-- -----------------------------------------------------
-- Table `gift_service`.`gift_certificates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificates`
(
    `id`               BIGINT         NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(255)   NOT NULL,
    `description`      VARCHAR(255)   NOT NULL,
    `price`            DECIMAL(10, 2) NOT NULL,
    `duration`         INT            NOT NULL,
    `create_date`      TIMESTAMP(3)   NOT NULL,
    `last_update_date` TIMESTAMP(3)   NOT NULL,
    PRIMARY KEY (`id`)
);


-- -----------------------------------------------------
-- Table `gift_service`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tags`
(
    `id`   BIGINT       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX `name_UNIQUE` ON `tags` (`name` ASC);


-- -----------------------------------------------------
-- Table `gift_service`.`gift_certificate_has_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificate_has_tag`
(
    `gift_certificate_id` BIGINT NOT NULL,
    `tag_id`              BIGINT NOT NULL,
    PRIMARY KEY (`gift_certificate_id`, `tag_id`),
    CONSTRAINT `fk_gift_certificate_has_tag_gift_certificate`
        FOREIGN KEY (`gift_certificate_id`)
            REFERENCES `gift_certificates` (`id`)
            ON DELETE CASCADE,
    CONSTRAINT `fk_gift_certificate_has_tag_tag1`
        FOREIGN KEY (`tag_id`)
            REFERENCES  `tags` (`id`)
);

CREATE INDEX `fk_gift_certificate_has_tag_tag1_idx` ON `gift_certificate_has_tag` (`tag_id` ASC);

CREATE INDEX `fk_gift_certificate_has_tag_gift_certificate_idx` ON `gift_certificate_has_tag` (`gift_certificate_id` ASC);


-- -----------------------------------------------------
-- Table `gift_service`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT,
    `login`    VARCHAR(255) NULL DEFAULT NULL,
    `name`     VARCHAR(255) NULL DEFAULT NULL,
    `password` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX `UK_ow0gan20590jrb00upg3va2fn` ON  `users` (`login` ASC);


-- -----------------------------------------------------
-- Table `gift_service`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `orders`
(
    `id`            BIGINT         NOT NULL AUTO_INCREMENT,
    `cost`          DECIMAL(19, 2) NULL DEFAULT NULL,
    `purchase_date` DATETIME(6)    NOT NULL,
    `user_id`       BIGINT         NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih`
        FOREIGN KEY (`user_id`)
            REFERENCES `users` (`id`)
);

CREATE INDEX `FK32ql8ubntj5uh44ph9659tiih` ON `orders` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `gift_service`.`order_has_gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `order_has_gift_certificate`
(
    `order_id`            BIGINT NOT NULL,
    `gift_certificate_id` BIGINT NOT NULL,
    CONSTRAINT `FKfcjpihfgro8qcgenewbypnos5`
        FOREIGN KEY (`order_id`)
            REFERENCES `orders` (`id`),
    CONSTRAINT `FKo93kq7y727m98kalelg81d5f2`
        FOREIGN KEY (`gift_certificate_id`)
            REFERENCES `gift_certificates` (`id`)
);

CREATE INDEX `FKo93kq7y727m98kalelg81d5f2` ON `order_has_gift_certificate` (`gift_certificate_id` ASC);

CREATE INDEX `FKfcjpihfgro8qcgenewbypnos5` ON `order_has_gift_certificate` (`order_id` ASC);


