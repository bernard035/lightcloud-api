-- ----------------------------
-- Table structure for user
-- ----------------------------

DROP TABLE IF EXISTS `user`;
CREATE TABLE user
(
    id       int(11)                                              NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email    varchar(255) UNIQUE KEY                                       DEFAULT NULL,
    password varchar(255)                                                  DEFAULT NULL,
    username char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    used     bigint                                               NOT NULL DEFAULT 0
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  DEFAULT CHARSET = utf8;