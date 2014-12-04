SET foreign_key_checks = 0;

DROP TABLE IF EXISTS `list_members`;

CREATE TABLE `list_members` (
  `list_id` bigint(20) NOT NULL,
  `todoshare_account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`list_id`,`todoshare_account_id`),
  KEY `FK_husq6we9wkb2vb8kgb7cceht1` (`todoshare_account_id`),
  CONSTRAINT `FK_95m6nb57018fqrm6htjy9rqe0` FOREIGN KEY (`list_id`) REFERENCES `todo_list` (`id`),
  CONSTRAINT `FK_husq6we9wkb2vb8kgb7cceht1` FOREIGN KEY (`todoshare_account_id`) REFERENCES `todoshare_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `todo`;

CREATE TABLE `todo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `done` bit(1) NOT NULL,
  `note` longtext,
  `title` longtext NOT NULL,
  `todoshare_account_id` bigint(20) NOT NULL,
  `todolist_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1hxvftuu3x08btsgukui3l4sg` (`todoshare_account_id`),
  KEY `FK_30ci5t1o8ks03c0hqf85bw53r` (`todolist_id`),
  CONSTRAINT `FK_30ci5t1o8ks03c0hqf85bw53r` FOREIGN KEY (`todolist_id`) REFERENCES `todo_list` (`id`),
  CONSTRAINT `FK_1hxvftuu3x08btsgukui3l4sg` FOREIGN KEY (`todoshare_account_id`) REFERENCES `todoshare_account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `todo_list`;

CREATE TABLE `todo_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `todoshare_account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_e0ivknuvmmueasib44gk7hqn8` (`todoshare_account_id`),
  CONSTRAINT `FK_e0ivknuvmmueasib44gk7hqn8` FOREIGN KEY (`todoshare_account_id`) REFERENCES `todoshare_account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `todoshare_account`;

CREATE TABLE `todoshare_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `email_address` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `todoshare_account_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8kcl04v9cs6v63gqr8ylacby8` (`todoshare_account_id`),
  CONSTRAINT `FK_8kcl04v9cs6v63gqr8ylacby8` FOREIGN KEY (`todoshare_account_id`) REFERENCES `todoshare_account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

SET foreign_key_checks = 1;