DROP DATABASE IF EXISTS `vote` ;
CREATE DATABASE IF NOT EXISTS vote default character SET utf8 COLLATE utf8_general_ci;
USE vote;
DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user
(
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(60) NOT NULL UNIQUE ,
  `password` VARCHAR(60) NOT NULL ,
  `nickname` VARCHAR(60) NOT NULL ,
  `create_time` TIMESTAMP NOT NULL DEFAULT '2016-06-14 06:18:18',
  `last_login_time` TIMESTAMP NOT NULL DEFAULT '2016-06-14 06:18:18',
  `last_login_ip` VARCHAR(60) NOT NULL DEFAULT '2016-06-14 06:18:18'
)CHARSET = utf8;

DROP TABLE IF EXISTS vote;
CREATE TABLE IF NOT EXISTS vote
(
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(60) NOT NULL ,
  `description` VARCHAR(80) NOT NULL ,
  `participate_number` INT NOT NULL DEFAULT 0,
  `is_multiple` TINYINT NOT NULL DEFAULT 0 COMMENT '0是单选,1是多选',
  `create_time` TIMESTAMP NOT NULL DEFAULT '2016-06-14 06:18:18',
  `start_time` TIMESTAMP NOT NULL DEFAULT '2016-06-14 06:18:18',
  `end_time` TIMESTAMP NOT NULL DEFAULT '2016-06-14 06:18:18',
  `user_id` BIGINT NOT NULL DEFAULT 1,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES user(`id`)
)CHARSET = utf8;


DROP TABLE IF EXISTS `choice`;
CREATE TABLE IF NOT EXISTS `choice`
(
  `id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL UNIQUE ,
  `number` INT NOT NULL DEFAULT 0,
  `vote_id` BIGINT NOT NULL ,
  CONSTRAINT `fk_vote_id` FOREIGN KEY (`vote_id`) REFERENCES vote(`id`)
)CHARSET = utf8;

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role`
(
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL
)CHARSET = utf8;

DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE IF NOT EXISTS `users_roles`
(
  `user_id` BIGINT NOT NULL ,
  `role_id` INT NOT NULL ,
  PRIMARY KEY (`user_id`,`role_id`),
  CONSTRAINT `fk_user_id_many` FOREIGN KEY (`user_id`) REFERENCES user(`id`),
  CONSTRAINT `fk_role_id_many` FOREIGN KEY (`role_id`) REFERENCES role(`id`)
);

INSERT INTO `user` VALUES (1001,'2014010908013','123456','hexi',now(),now(),'192.168.1.103');

INSERT INTO `vote` VALUES (101,'班级聚餐投票','聚餐投票希望大家踊跃参与！',0,0,now(),now(),now()+10000000,1001),
                          (102,'班委选举投票','班委选举投票,希望大家踊跃参与',0,0,now(),now(),now()+10000000,1001);

INSERT INTO `choice` VALUES (1,'好伦哥',0,101)
  ,(2,'关公冒菜',0,101)
  ,(3,'XXX1',0,102)
  ,(4,'XXX2',0,102);

INSERT INTO `role` VALUES (10000,'creater'),(10001,'admin'),(10002,'user');

INSERT INTO `users_roles` VALUES (1001,10000),(1001,10001),(1001,10002);