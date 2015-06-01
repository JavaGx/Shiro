/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50519
Source Host           : localhost:3306
Source Database       : shiro

Target Server Type    : MYSQL
Target Server Version : 50519
File Encoding         : 65001

Date: 2015-06-01 16:54:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(100) NOT NULL,
  `url` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(100) DEFAULT NULL,
  `role_name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  KEY `FK_fn4pldu982p9u158rpk6nho5k` (`permission_id`),
  KEY `FK_j89g87bvih4d6jbxjcssrybks` (`role_id`),
  CONSTRAINT `FK_fn4pldu982p9u158rpk6nho5k` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`),
  CONSTRAINT `FK_j89g87bvih4d6jbxjcssrybks` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) NOT NULL,
  `username` varchar(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `FK_it77eq964jhfqtu54081ebtio` (`role_id`),
  KEY `FK_apcc8lxk2xnug8377fatvbn04` (`user_id`),
  CONSTRAINT `FK_apcc8lxk2xnug8377fatvbn04` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_it77eq964jhfqtu54081ebtio` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Procedure structure for findAll
-- ----------------------------
DROP PROCEDURE IF EXISTS `findAll`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `findAll`(IN `tableName` varchar(20))
BEGIN
	#Routine body goes here...
set @s=CONCAT("SELECT * from ",tableName);
PREPARE s from @s;
EXECUTE s;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for findById
-- ----------------------------
DROP PROCEDURE IF EXISTS `findById`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `findById`(IN `id` int,IN `tableName` varchar(20))
BEGIN
	#Routine body goes here...
set @s=CONCAT("select * from ",tableName," where id=",id);
PREPARE s FROM @s;
EXECUTE s;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for findByIds
-- ----------------------------
DROP PROCEDURE IF EXISTS `findByIds`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `findByIds`(IN `tableName` varchar(10),IN `ids` varchar(20))
BEGIN
	#Routine body goes here...
SET @q=CONCAT("SELECT * FROM ",tableName," WHERE id in(",ids,")");
PREPARE q from @q;
EXECUTE q;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for findByName
-- ----------------------------
DROP PROCEDURE IF EXISTS `findByName`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `findByName`(IN `name` varchar(20))
BEGIN
	#Routine body goes here...
select * from user where username=name;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getEmail
-- ----------------------------
DROP PROCEDURE IF EXISTS `getEmail`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getEmail`(IN `e-mail` varchar(50))
BEGIN
	#Routine body goes here...
SELECT u.email FROM `user` AS u WHERE u.email=e-mail; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getRolesPerm
-- ----------------------------
DROP PROCEDURE IF EXISTS `getRolesPerm`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRolesPerm`(IN `roleId` int)
BEGIN
	#Routine body goes here...
	SELECT p.id,p.permission_name,p.url FROM permission AS p 
			INNER JOIN (
					SELECT rp.permission_id FROM role_permission AS rp 
							INNER JOIN role AS r ON rp.role_id=r.id WHERE r.id=roleId
				) AS permId ON p.id=permId.permission_id;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getUsersRole
-- ----------------------------
DROP PROCEDURE IF EXISTS `getUsersRole`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUsersRole`(IN `userId` int)
BEGIN
	#Routine body goes here...
	SELECT r.id,r.role_name,r.description FROM role AS r INNER JOIN (
		SELECT ur.role_id AS roleId FROM `user` AS u 
			INNER JOIN user_role AS ur 
					ON u.id=ur.user_id WHERE u.id=userId
		) AS ids 
			ON ids.roleId=r.id; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for pageRecords
-- ----------------------------
DROP PROCEDURE IF EXISTS `pageRecords`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pageRecords`(IN `beginIndex` int,IN `endIndex` int)
BEGIN
	#Routine body goes here...
	select * from user limit beginIndex,endIndex;
END
;;
DELIMITER ;
