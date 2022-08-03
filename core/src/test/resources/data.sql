INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_1', 'description_1', '1', '30', '2022-04-18T23:49:00.000', '2022-04-25T23:00:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_2_find', 'find_description_2', '2', '30', '2022-04-18T03:49:00.000', '2022-04-18T07:35:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_3', 'description_3', '3', '180', '2022-04-18T07:25:00.000', '2022-04-20T09:25:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_find_4', 'description_4', '4', '180', '2022-04-18T12:49:00.000', '2022-04-19T12:49:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_5', 'find_description_5', '5', '365', '2022-03-18T13:00:00.000', '2022-04-18T23:50:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_6', 'find_description_6', '6', '365', '2022-02-18T13:00:00.000', '2022-02-18T23:50:00.000');
INSERT INTO `gift_certificates` (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('certificate_7_to_delete', 'description_7', '7', '165', '2022-03-14T11:00:00.000', '2022-05-11T14:20:00.000');

INSERT INTO `tags` (`name`) VALUES ('tag_1');
INSERT INTO `tags` (`name`) VALUES ('tag_2_find');
INSERT INTO `tags` (`name`) VALUES ('tag_3_empty');
INSERT INTO `tags` (`name`) VALUES ('tag_4');
INSERT INTO `tags` (`name`) VALUES ('tag_5');
INSERT INTO `tags` (`name`) VALUES ('tag_5_again');


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
INSERT INTO `gift_certificate_has_tag` (`gift_certificate_id`, `tag_id`)
VALUES ('7', '1');

INSERT INTO `users` (`login`, `name`, `password`) VALUES ('login_1', 'user_1', 'password_1');
INSERT INTO `users` (`login`, `name`, `password`) VALUES ('login_2', 'user_2', 'password_2');
INSERT INTO `users` (`login`, `name`, `password`) VALUES ('login_3_count', 'user_3', 'password_3');
INSERT INTO `users` (`login`, `name`, `password`) VALUES ('login_4_count', 'user_4', 'password_4');
INSERT INTO `users` (`login`, `name`, `password`) VALUES ('login_5', 'user_5', 'password_5');

INSERT INTO `orders` (`cost`, `purchase_date`, `user_id`) VALUES ('10', '2022-02-17T12:50:00.000', '1');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('1', '1');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('1', '5');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('1', '4');

INSERT INTO `orders` (`cost`, `purchase_date`, `user_id`) VALUES ('10', '2022-01-12T11:50:00.000', '2');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('2', '5');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('2', '5');

INSERT INTO `orders` (`cost`, `purchase_date`, `user_id`) VALUES ('7', '2022-02-12T10:50:00.000', '2');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('3', '2');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('3', '2');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('3', '3');

INSERT INTO `orders` (`cost`, `purchase_date`, `user_id`) VALUES ('3', '2022-02-02T10:50:00.000', '3');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('4', '1');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('4', '1');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('4', '1');

INSERT INTO `orders` (`cost`, `purchase_date`, `user_id`) VALUES ('6', '2022-05-02T06:50:00.000', '4');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('5', '6');

INSERT INTO `orders` (`cost`, `purchase_date`, `user_id`) VALUES ('6', '2022-05-02T06:50:00.000', '5');
INSERT INTO `order_has_gift_certificate` (`order_id`, `gift_certificate_id`) VALUES ('6', '6');







