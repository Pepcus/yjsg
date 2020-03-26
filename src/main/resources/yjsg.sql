/*
SQLyog - Free MySQL GUI v5.17
Host - 5.7.21 : Database - studentapp
*********************************************************************
Server version : 5.7.21
*/


SET NAMES utf8;

SET SQL_MODE='';

create database if not exists `yjsg`;

USE `yjsg`;

SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';

/*Table structure for table `flag` */
DROP TABLE IF EXISTS `flag`;
CREATE TABLE `flag` (
  `id` int(11) NOT NULL,
  `flag_name` varchar(45) DEFAULT NULL,
  `flag_value` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `student` */
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `father_name` varchar(255) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `age` varchar(20) DEFAULT NULL,
  `education` varchar(255) DEFAULT NULL,
  `occupation` varchar(255) DEFAULT NULL,
  `mother_mobile` varchar(20) DEFAULT NULL,
  `father_mobile` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `bus_num` varchar(255) DEFAULT NULL,
  `bus_stop` varchar(255) DEFAULT NULL,
  `print_status` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `secret_key` varchar(255) DEFAULT NULL,
  `class_attended_2016` varchar(255) DEFAULT NULL,
  `class_attended_2017` varchar(255) DEFAULT NULL,
  `class_attended_2018` varchar(255) DEFAULT NULL,
  `class_attended_2019` varchar(255) DEFAULT NULL,
  `class_attended_2020` varchar(255) DEFAULT NULL,
  `class_room_no_2016` varchar(255) DEFAULT NULL,
  `class_room_no_2017` varchar(255) DEFAULT NULL,
  `class_room_no_2018` varchar(255) DEFAULT NULL,
  `class_room_no_2019` varchar(255) DEFAULT NULL,
  `class_room_no_2020` varchar(255) DEFAULT NULL,
  `attendance_2016` varchar(255) DEFAULT NULL,
  `attendance_2017` varchar(255) DEFAULT NULL,
  `attendance_2018` varchar(255) DEFAULT NULL,
  `attendance_2019` varchar(255) DEFAULT NULL,
  `attendance_2020` varchar(255) DEFAULT NULL,
  `marks_2016` varchar(255) DEFAULT NULL,
  `marks_2017` varchar(255) DEFAULT NULL,
  `marks_2018` varchar(255) DEFAULT NULL,
  `marks_2019` varchar(255) DEFAULT NULL,
  `marks_2020` varchar(255) DEFAULT NULL,
  `opt_in_2018` tinyint(1) DEFAULT NULL,
  `opt_in_2019` varchar(255) DEFAULT NULL,
  `opt_in_2020` varchar(255) DEFAULT NULL,
  `day1` varchar(255) DEFAULT NULL,
  `day2` varchar(255) DEFAULT NULL,
  `day3` varchar(255) DEFAULT NULL,
  `day4` varchar(255) DEFAULT NULL,
  `day5` varchar(255) DEFAULT NULL,
  `day6` varchar(255) DEFAULT NULL,
  `day7` varchar(255) DEFAULT NULL,
  `day8` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `duplicate_registration` */
DROP TABLE IF EXISTS `duplicate_registration`;
CREATE TABLE `duplicate_registration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `duplicate_of_student_id` int(10) NOT NULL,
  `duplicate_student_json` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `student_gms` */
DROP TABLE IF EXISTS `student_gms`;
CREATE TABLE `student_gms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `age` int(3) DEFAULT NULL,
  `mobile` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(28) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_whatsapp` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `payment_status` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `food_opt` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `document` */
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `document_rank` int(11) DEFAULT NULL,
  `document_url` varchar(2056) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `document_uploaded_date` date DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `last_modified_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `person` */
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `members` int(11) DEFAULT NULL,
  `phone_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `last_modified_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `department` */
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `internal_name` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `department_value_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


/*Table structure for table `department_value` */
DROP TABLE IF EXISTS `department_value`;
CREATE TABLE `department_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department_id` integer NOT NULL,
  `internal_name` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT fk_department_value_department FOREIGN KEY (department_id) REFERENCES department (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


/*Table structure for table `coordinator` */
DROP TABLE IF EXISTS `coordinator`;
CREATE TABLE `coordinator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `dob` varchar(20) DEFAULT NULL,
  `gender` varchar(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `whatsapp_number` varchar(20) DEFAULT NULL,
  `alternate_number` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `secret_key` varchar(255) DEFAULT NULL,
  `is_active` varchar(6) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


/*Table structure for table `coordinator_assigned_departments` */
DROP TABLE IF EXISTS `coordinator_assigned_departments`;
CREATE TABLE `coordinator_assigned_departments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department_id` integer NOT NULL,
  `coordinator_id` integer NOT NULL,
  `department_value_id` integer DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT fk_coordinator_assigned_departments_department FOREIGN KEY (department_id) REFERENCES department (id),
  CONSTRAINT fk_coordinator_assigned_departments_coordinator FOREIGN KEY (coordinator_id) REFERENCES coordinator (id),
  CONSTRAINT fk_coordinator_assigned_departments_department_value FOREIGN KEY (department_value_id) REFERENCES department_value(id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `coordinator_interested_departments` */
DROP TABLE IF EXISTS `coordinator_interested_departments`;
CREATE TABLE `coordinator_interested_departments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department_id` integer NOT NULL,
  `coordinator_id` integer NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT fk_coordinator_interested_departments_department FOREIGN KEY (department_id) REFERENCES department (id),
  CONSTRAINT fk_coordinator_interested_departments_coordinator FOREIGN KEY (coordinator_id) REFERENCES coordinator (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

