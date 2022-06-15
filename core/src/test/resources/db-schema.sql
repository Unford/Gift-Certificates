SET MODE MYSQL;

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
            ON DELETE CASCADE
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_gift_certificate_has_tag_tag1`
        FOREIGN KEY (`tag_id`)
            REFERENCES `tags` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX `fk_gift_certificate_has_tag_tag1_idx` ON `gift_certificate_has_tag` (`tag_id` ASC) ;

CREATE INDEX `fk_gift_certificate_has_tag_gift_certificate_idx` ON `gift_certificate_has_tag` (`gift_certificate_id` ASC) ;


