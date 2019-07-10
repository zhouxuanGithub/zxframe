/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.21-20-log : Database - zxframe_test_01
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`zxframe_test_01` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `zxframe_test_01`;

/*Table structure for table `properties` */

DROP TABLE IF EXISTS `properties`;

CREATE TABLE `properties` (
  `key` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `properties` */

insert  into `properties`(`key`,`value`,`description`) values ('system-isTest','false','系统是否是测试模式'),('system-version','2','系统版本号，更改后1分钟内所有服务器使用最新properties配置'),('zxframe-test-pps','ok','测试lesson12');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`age`) values ('019bc085-e0db-4ad8-8154-f2f232980e99','06隔壁236哥',18),('5d485bfb-0da4-4947-a557-3a58e6ecb711','06隔壁194哥',18),('6f8ca665-72e4-4834-a2bf-89f963994412','06隔壁238哥',18),('79cbc702-9092-4042-b790-5e9523661b8c','隔壁433哥',18),('e272cf49-e42d-4500-b916-2220a597b047','06隔壁770哥',18),('ff397b90-95ab-4c01-b638-8134e19368ee','隔壁932哥',18);

/*Table structure for table `user07` */

DROP TABLE IF EXISTS `user07`;

CREATE TABLE `user07` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user07` */

insert  into `user07`(`id`,`name`,`age`) values ('0f678026-524e-436e-a948-1e4dc9b7a80a','07隔壁743哥',69),('501a089d-7246-4aa9-81e1-f68b4818ac5b','07隔壁931哥',13);

/*Table structure for table `user11` */

DROP TABLE IF EXISTS `user11`;

CREATE TABLE `user11` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user11` */

insert  into `user11`(`id`,`name`,`age`) values ('29cbc702-9092-4042-b790-5e9523661b8c','user11-2',100),('79cbc702-9092-4042-b790-5e9523661b8c','user11',100);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
