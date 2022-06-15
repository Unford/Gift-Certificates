INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_1', 'description_1', '10.43', '30', '2022-04-18T23:49:00.000', '2022-04-25T23:00:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_2_find', 'find_description_2', '12.4', '30', '2022-04-18T03:49:00.000', '2022-04-18T07:35:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_3', 'description_3', '14.1', '180', '2022-04-18T07:25:00.000', '2022-04-20T09:25:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_find_4', 'description_4', '100.00', '180', '2022-04-18T12:49:00.000', '2022-04-19T12:49:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_5', 'find_description_5', '15.2', '365', '2022-03-18T13:00:00.000', '2022-04-18T23:50:00.000');


INSERT INTO `tags` (`name`)
VALUES ('tag_1');
INSERT INTO `tags` (`name`)
VALUES ('tag_2_find');
INSERT INTO `tags` (`name`)
VALUES ('tag_3_empty');
INSERT INTO `tags` (`name`)
VALUES ('tag_4');
INSERT INTO `tags` (`name`)
VALUES ('tag_5');

INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('1', '1');
INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('1', '5');
INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('2', '2');
INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('2', '4');
INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('3', '5');
INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('4', '2');
INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('4', '1');
INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('4', '5');
INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('5', '5');

