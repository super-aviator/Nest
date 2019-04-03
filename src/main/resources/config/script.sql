# 存储群聊信息
CREATE TABLE `friend_msg` (
                            `username` varchar(16) COLLATE utf8_bin DEFAULT '',
                            `id` bigint(5) unsigned NOT NULL,
                            `rev_id` bigint(5) unsigned NOT NULL,
                            `avatar` varchar(200) COLLATE utf8_bin DEFAULT 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1',
                            `timestamp` bigint(6) unsigned DEFAULT '1467475443306',
                            `content` varchar(1000) COLLATE utf8_bin DEFAULT '',
                            PRIMARY KEY (`id`,`rev_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用于存储用户好友聊天记录';


# 存储好友消息
CREATE TABLE `group_msg` (
                           `username` varchar(16) COLLATE utf8_bin DEFAULT '',
                           `id` bigint(5) unsigned NOT NULL,
                           `rev_id` bigint(5) unsigned NOT NULL,
                           `avatar` varchar(200) COLLATE utf8_bin DEFAULT 'http://tp4.sinaimg.cn/2145291155/180/5601307179/1',
                           `timestamp` bigint(6) unsigned DEFAULT '1467475443306',
                           `content` varchar(1000) COLLATE utf8_bin DEFAULT '',
                           PRIMARY KEY (`id`,`rev_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用于存储群聊消息';
