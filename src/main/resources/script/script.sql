#用户信息表
CREATE TABLE `user_info` (
  `user_id`  bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(16)         NOT NULL,
  `sign`     varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '',
  `avatar`   varchar(200) DEFAULT 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1',
  `status`   varchar(15) CHARACTER SET utf8
  COLLATE utf8_bin               NOT NULL DEFAULT 'hide',
  `password` varchar(45)         NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1000028
  DEFAULT CHARSET = utf8;
INSERT INTO nest.user_info (user_id, username, sign, avatar, status, password) VALUES (1000001, '熊乾坤', '你要去哪里', 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1', 'online', '6504110130');
INSERT INTO nest.user_info (user_id, username, sign, avatar, status, password) VALUES (1000002, '杨小毛', '勿失莫忘', 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1', 'hide', '123456');
INSERT INTO nest.user_info (user_id, username, sign, avatar, status, password) VALUES (1000003, '曹睿', '静听寂寞嘞', 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1', 'hide', '123456');
INSERT INTO nest.user_info (user_id, username, sign, avatar, status, password) VALUES (1000004, '张威', '哈哈哈', 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1', 'online', '123456');

#创建触发器
DROP TRIGGER IF EXISTS `nest`.`user_info_AFTER_INSERT`;
DELIMITER $$
USE `nest`$$
CREATE DEFINER = CURRENT_USER TRIGGER `nest`.`user_info_AFTER_INSERT` AFTER INSERT ON `user_info` FOR EACH ROW
BEGIN
SELECT max(user_id) INTO @user_id from user_info;

    INSERT INTO packet_info (packet_name) VALUES ('我的好友');
    SET @id1 = LAST_INSERT_ID();

    INSERT INTO packet_info (packet_name) VALUES ('我的家人');
    SET @id2 = LAST_INSERT_ID();

    INSERT INTO packet_info (packet_name) VALUES ('我的亲人');
    SET @id3 = LAST_INSERT_ID();

    INSERT INTO packet_info (packet_name) VALUES ('我的朋友');
    SET @id4 = LAST_INSERT_ID();

    INSERT INTO packet_info (packet_name) VALUES ('我的老师');
    SET @id5 = LAST_INSERT_ID();

    INSERT INTO packet_info (packet_name) VALUES ('我的同学');
    SET @id6 = LAST_INSERT_ID();

    INSERT INTO packet_info (packet_name) VALUES ('我的室友');
    SET @id7 = LAST_INSERT_ID();

    INSERT INTO user_packet_info (packet_id, user_id)
    VALUES (@id1, @user_id),
           (@id2, @user_id),
           (@id3, @user_id),
           (@id4, @user_id),
           (@id5, @user_id),
           (@id6, @user_id),
           (@id7, @user_id);
END$$
DELIMITER ;

#用户拥有的组表
CREATE TABLE `user_packet_info` (
  `packet_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`packet_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO nest.user_packet_info (packet_id, user_id) VALUES (1, 1000001);
INSERT INTO nest.user_packet_info (packet_id, user_id) VALUES (2, 1000001);
INSERT INTO nest.user_packet_info (packet_id, user_id) VALUES (3, 1000002);
INSERT INTO nest.user_packet_info (packet_id, user_id) VALUES (4, 1000002);
INSERT INTO nest.user_packet_info (packet_id, user_id) VALUES (5, 1000003);
INSERT INTO nest.user_packet_info (packet_id, user_id) VALUES (6, 1000003);
INSERT INTO nest.user_packet_info (packet_id, user_id) VALUES (7, 1000004);
INSERT INTO nest.user_packet_info (packet_id, user_id) VALUES (8, 1000004);

#分组信息表
CREATE TABLE `packet_info` (
  `packet_name` varchar(20)         DEFAULT NULL,
  `packet_id`   bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`packet_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 24
  DEFAULT CHARSET = utf8;
INSERT INTO nest.packet_info (packet_name, packet_id) VALUES ('好友', 1);
INSERT INTO nest.packet_info (packet_name, packet_id) VALUES ('好友', 4);
INSERT INTO nest.packet_info (packet_name, packet_id) VALUES ('好友', 7);
INSERT INTO nest.packet_info (packet_name, packet_id) VALUES ('室友', 2);
INSERT INTO nest.packet_info (packet_name, packet_id) VALUES ('室友', 8);
INSERT INTO nest.packet_info (packet_name, packet_id) VALUES ('家人', 3);
INSERT INTO nest.packet_info (packet_name, packet_id) VALUES ('家人', 5);
INSERT INTO nest.packet_info (packet_name, packet_id) VALUES ('敌人', 6);

#组中包含的用户表
CREATE TABLE `packet_user_info` (
  `user_id` int(11) NOT NULL,
  `packet_id` int(11) NOT NULL,
  PRIMARY KEY (`packet_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000001, 1);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000002, 1);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000003, 1);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000004, 1);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000001, 2);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000002, 2);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000003, 2);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000004, 2);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000001, 3);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000002, 3);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000003, 3);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000004, 3);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000001, 4);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000002, 4);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000003, 4);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000004, 4);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000001, 5);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000002, 5);
INSERT INTO nest.packet_user_info (user_id, packet_id) VALUES (1000002, 6);

# 群消息表
CREATE TABLE `group_info` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(20) DEFAULT NULL,
  `avatar` varchar(200) DEFAULT 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8;

INSERT INTO nest.group_info (group_id, group_name, avatar) VALUES (101, 'java开发交流群', 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1');
INSERT INTO nest.group_info (group_id, group_name, avatar) VALUES (102, 'php开发交流群', 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1');
INSERT INTO nest.group_info (group_id, group_name, avatar) VALUES (103, 'python开发交流群', 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1');


#用户加入的群表
CREATE TABLE `user_group_info` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000001, 101);
INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000001, 102);
INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000001, 103);
INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000002, 101);
INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000002, 102);
INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000002, 103);
INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000003, 101);
INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000003, 102);
INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000004, 101);
INSERT INTO nest.user_group_info (user_id, group_id) VALUES (1000004, 102);

# 群聊信息存储表
CREATE TABLE `friend_msg` (
                            `username` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '',
                            `id` bigint(5) unsigned NOT NULL,
                            `rev_id` bigint(5) unsigned NOT NULL,
                            `avatar` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1',
                            `timestamp` bigint(6) unsigned NOT NULL DEFAULT '1467475443306',
                            `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '',
                            PRIMARY KEY (`id`,`rev_id`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用于存储用户好友聊天记录';


# 好友消息存储表
CREATE TABLE `group_msg` (
                           `username` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '',
                           `id` bigint(5) unsigned NOT NULL,
                           `rev_id` bigint(5) unsigned NOT NULL,
                           `avatar` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1',
                           `timestamp` bigint(6) unsigned NOT NULL DEFAULT '1467475443306',
                           `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '',
                           PRIMARY KEY (`id`,`rev_id`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用于存储群聊消息';
