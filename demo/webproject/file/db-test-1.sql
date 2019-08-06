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

insert  into `properties`(`key`,`value`,`description`) values ('system-isTest','false','系统是否是测试模式'),('system-version','3','系统版本号，更改后1分钟内所有服务器使用最新properties配置'),('zxframe-test-pps','ok32131','测试lesson12');

/*Table structure for table `testid` */

DROP TABLE IF EXISTS `testid`;

CREATE TABLE `testid` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(244) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `testid` */

insert  into `testid`(`id`,`value`) values (2,'ba7553b0-4174-4ed9-9f0f-7ffb14926cf4'),(3,'3acd1324-2e32-46ac-802d-914faabf0dbb'),(4,'03d7a8f1-c6ef-4e89-b6d2-5b7a48062409'),(5,'23a337aa-0b39-4155-87fe-8f2806c7963b'),(6,'47075f89-8955-4f2a-b3c7-f3fb569e4436'),(7,'0615c3f3-2893-40cc-a3e0-453813f81ec2'),(8,'1e628a2f-7beb-4a87-960e-de5eec0f8398');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`age`,`version`,`birthday`,`updatetime`) values ('019bc085-e0db-4ad8-8154-f2f232980e99','06隔壁236哥',33,1,NULL,NULL),('172bec84-4f28-4853-8bae-d198d1b50e6a','06隔壁934哥222',33,0,'2019-08-05','2019-08-05 21:31:41'),('23066c7c-fdad-4584-8aeb-67c49b589049','06隔壁586哥',33,0,'2019-08-05','2019-08-05 02:11:46'),('4522c4f7-1a67-4183-a5ab-8dbc774b8dc1','06隔壁245哥',33,0,'2019-07-24','2019-07-24 05:38:50'),('5d485bfb-0da4-4947-a557-3a58e6ecb711','06隔壁194哥',33,1,NULL,NULL),('6f8ca665-72e4-4834-a2bf-89f963994412','06隔壁238哥',33,1,NULL,NULL),('79cbc702-9092-4042-b790-5e9523661b8c','隔壁433哥',33,10,'2019-08-05','2019-08-05 02:15:20'),('7fee5297-a85f-407a-9d91-921185a95d85','06隔壁248哥',33,0,'2019-07-24',NULL),('974ebe61-875c-4e2f-baec-1a4d0476fb1d','06隔壁160哥',33,0,'2019-08-05','2019-08-05 02:13:58'),('995d53ad-7388-4e39-ad8a-8feab829cd17','06隔壁309哥',33,0,'2019-07-24','2019-07-24 05:33:00'),('ae3b79f4-a113-4ba3-972e-e7a7229e19a7','06隔壁411哥',33,0,'2019-07-24',NULL),('b1017955-8e4a-4895-87d6-561153a6fc1a','06隔壁896哥',33,0,'2019-07-24',NULL),('e272cf49-e42d-4500-b916-2220a597b047','06隔壁770哥',33,1,NULL,NULL),('e71f370a-15dd-497b-bc35-d641fe281386','06隔壁70哥',33,0,'2019-08-05','2019-08-05 21:30:58'),('fb3c7130-a7af-49ca-9cbd-b9e89dde071d','06隔壁772哥',33,0,'2019-08-05','2019-08-05 02:08:51'),('ff397b90-95ab-4c01-b638-8134e19368ee','隔壁932哥',33,1,NULL,NULL);

/*Table structure for table `user07` */

DROP TABLE IF EXISTS `user07`;

CREATE TABLE `user07` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user07` */

insert  into `user07`(`id`,`name`,`age`) values ('0f678026-524e-436e-a948-1e4dc9b7a80a','07隔壁743哥',69),('33fb2186-bd08-4b74-8431-8a657c29ac95','07隔壁491哥',11),('501a089d-7246-4aa9-81e1-f68b4818ac5b','07隔壁931哥',13),('cd4cb39a-d6d9-456b-b600-30c2bba91202','07隔壁839哥',28);

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
