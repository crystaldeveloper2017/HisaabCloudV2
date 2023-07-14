-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 13.234.66.169    Database: customizedpos
-- ------------------------------------------------------
-- Server version	8.0.33-0ubuntu0.22.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acl_user_role_rlt`
--

DROP TABLE IF EXISTS `acl_user_role_rlt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acl_user_role_rlt` (
  `rlt_pk` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `role_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`rlt_pk`),
  KEY `acl_user_role_rlt_user_id_IDX` (`user_id`,`role_id`,`activate_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=266 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `booking_item_mpg`
--

DROP TABLE IF EXISTS `booking_item_mpg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_item_mpg` (
  `booking_item_id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `item_id` bigint NOT NULL,
  `qty` double NOT NULL,
  PRIMARY KEY (`booking_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cng_cars_spinny`
--

DROP TABLE IF EXISTS `cng_cars_spinny`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cng_cars_spinny` (
  `car_id` bigint NOT NULL AUTO_INCREMENT,
  `model` varchar(3000) DEFAULT NULL,
  `price` varchar(100) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`car_id`)
) ENGINE=InnoDB AUTO_INCREMENT=566 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `config_gmail`
--

DROP TABLE IF EXISTS `config_gmail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_gmail` (
  `config_id` bigint NOT NULL DEFAULT '0',
  `gmail_id` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  `gmail_password` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_delivery_routine`
--

DROP TABLE IF EXISTS `customer_delivery_routine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_delivery_routine` (
  `routine_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `item_id` bigint DEFAULT NULL,
  `custom_rate` bigint DEFAULT NULL,
  `qty` double DEFAULT NULL,
  `occurance` varchar(100) DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`routine_id`),
  KEY `customer_delivery_routine_customer_id_IDX` (`customer_id`) USING BTREE,
  KEY `customer_delivery_routine_item_id_IDX` (`item_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_group`
--

DROP TABLE IF EXISTS `customer_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_group` (
  `group_id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(500) DEFAULT NULL,
  `updated_by` bigint NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_user_mpg`
--

DROP TABLE IF EXISTS `customer_user_mpg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_user_mpg` (
  `customer_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dispenser_master`
--

DROP TABLE IF EXISTS `dispenser_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispenser_master` (
  `dispenser_id` bigint NOT NULL AUTO_INCREMENT,
  `dispenser_name` varchar(100) DEFAULT NULL,
  `app_id` varchar(100) DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`dispenser_id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `frm_audit_trail`
--

DROP TABLE IF EXISTS `frm_audit_trail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `frm_audit_trail` (
  `audit_id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(30) DEFAULT NULL,
  `url` varchar(10000) DEFAULT NULL,
  `parameters` longtext,
  `accessed_time` datetime DEFAULT NULL,
  `ip` varchar(15) DEFAULT NULL,
  `browser_name` varchar(1300) DEFAULT NULL,
  `responded_time` datetime DEFAULT NULL,
  `response_string` longtext,
  PRIMARY KEY (`audit_id`),
  KEY `frm_audit_trail_user_name_IDX` (`user_name`) USING BTREE,
  KEY `frm_audit_trail_user_name_IDX_2` (`user_name`,`accessed_time`) USING BTREE,
  KEY `frm_audit_trail_accessed_time_IDX` (`accessed_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=777205 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `frm_error_log`
--

DROP TABLE IF EXISTS `frm_error_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `frm_error_log` (
  `error_id` int NOT NULL AUTO_INCREMENT,
  `error_message` mediumtext,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`error_id`)
) ENGINE=InnoDB AUTO_INCREMENT=220 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `frm_query_log`
--

DROP TABLE IF EXISTS `frm_query_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `frm_query_log` (
  `query_id` bigint NOT NULL AUTO_INCREMENT,
  `query_string` mediumtext NOT NULL,
  `time_taken` decimal(10,0) NOT NULL,
  `accessed_time` datetime DEFAULT NULL,
  PRIMARY KEY (`query_id`),
  KEY `frm_query_log_time_taken_IDX` (`time_taken`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hst_mst_items`
--

DROP TABLE IF EXISTS `hst_mst_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hst_mst_items` (
  `item_id` int NOT NULL DEFAULT '0',
  `parent_category_id` int NOT NULL,
  `debit_in` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `item_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `wholesale_price` decimal(10,2) DEFAULT NULL,
  `franchise_rate` decimal(10,2) DEFAULT NULL,
  `loyalcustomerrate1` decimal(10,2) DEFAULT NULL,
  `loyalcustomerrate2` decimal(10,2) DEFAULT NULL,
  `loyalcustomerrate3` decimal(10,2) DEFAULT NULL,
  `activate_flag` tinyint NOT NULL,
  `updated_by` int DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `product_code` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `average_cost` decimal(10,2) DEFAULT NULL,
  `distributor_rate` decimal(10,2) DEFAULT NULL,
  `b2b_rate` decimal(10,2) DEFAULT NULL,
  `shrikhand` decimal(10,2) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `sgst` decimal(10,2) DEFAULT NULL,
  `product_details` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `hsn_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `catalog_no` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `order_no` int DEFAULT NULL,
  `cgst` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `instance_manager`
--

DROP TABLE IF EXISTS `instance_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instance_manager` (
  `instance_id` bigint NOT NULL AUTO_INCREMENT,
  `instance_name` varchar(100) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`instance_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invoice_formats`
--

DROP TABLE IF EXISTS `invoice_formats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_formats` (
  `format_id` bigint NOT NULL AUTO_INCREMENT,
  `format_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`format_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invoice_types`
--

DROP TABLE IF EXISTS `invoice_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_types` (
  `invoice_type_id` bigint NOT NULL,
  `invoice_type_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`invoice_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_app`
--

DROP TABLE IF EXISTS `mst_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_app` (
  `app_id` bigint NOT NULL AUTO_INCREMENT,
  `app_name` varchar(100) DEFAULT NULL,
  `about_us_content` mediumtext,
  `contact_1` varchar(100) DEFAULT NULL,
  `contact_2` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `map_cordinates` mediumtext,
  `address` varchar(200) DEFAULT NULL,
  `valid_till` date DEFAULT NULL,
  `app_type` varchar(100) DEFAULT NULL,
  `threads_overlap` int DEFAULT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_bank`
--

DROP TABLE IF EXISTS `mst_bank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_bank` (
  `bank_id` bigint NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `account_no` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `ifsc_code` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`bank_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_cashback`
--

DROP TABLE IF EXISTS `mst_cashback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_cashback` (
  `cashback_id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(30) NOT NULL,
  `percentage` int NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`cashback_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_category`
--

DROP TABLE IF EXISTS `mst_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) NOT NULL,
  `activate_flag` tinyint NOT NULL,
  `created_Date` datetime NOT NULL,
  `updated_Date` datetime DEFAULT NULL,
  `updated_by` int DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `order_no` int DEFAULT '0',
  PRIMARY KEY (`category_id`),
  KEY `mst_category_activate_flag_IDX` (`activate_flag`,`app_id`) USING BTREE,
  KEY `mst_category_app_id_IDX` (`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=89547 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_config`
--

DROP TABLE IF EXISTS `mst_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_config` (
  `config_id` int NOT NULL AUTO_INCREMENT,
  `printer_name` varchar(300) DEFAULT NULL,
  `no_of_copies` int NOT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`config_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_customer`
--

DROP TABLE IF EXISTS `mst_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(100) DEFAULT NULL,
  `mobile_number` mediumtext NOT NULL,
  `city` varchar(50) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `customer_type` varchar(100) NOT NULL,
  `activate_flag` tinyint NOT NULL,
  `created_Date` datetime NOT NULL,
  `updated_by` int DEFAULT NULL,
  `updated_Date` datetime DEFAULT NULL,
  `group_id` bigint DEFAULT NULL,
  `alternate_mobile_no` varchar(10) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `customer_reference` varchar(1000) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `anniversary` date DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  KEY `mst_customer_activate_flag_IDX` (`activate_flag`,`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5478 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_employee`
--

DROP TABLE IF EXISTS `mst_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_employee` (
  `employee_id` int NOT NULL AUTO_INCREMENT,
  `employee_name` varchar(100) NOT NULL,
  `employee_role` varchar(50) NOT NULL,
  `mobile_number` mediumtext NOT NULL,
  `activate_flag` tinyint NOT NULL,
  `created_Date` datetime NOT NULL,
  `updated_Date` datetime DEFAULT NULL,
  `updated_by` int DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_items`
--

DROP TABLE IF EXISTS `mst_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_items` (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `parent_category_id` int NOT NULL,
  `debit_in` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `item_name` varchar(1000) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `wholesale_price` decimal(10,2) DEFAULT NULL,
  `franchise_rate` decimal(10,2) DEFAULT NULL,
  `loyalcustomerrate1` decimal(10,2) DEFAULT NULL,
  `loyalcustomerrate2` decimal(10,2) DEFAULT NULL,
  `loyalcustomerrate3` decimal(10,2) DEFAULT NULL,
  `activate_flag` tinyint NOT NULL,
  `updated_by` int DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `product_code` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `average_cost` decimal(10,2) DEFAULT NULL,
  `distributor_rate` decimal(10,2) DEFAULT NULL,
  `b2b_rate` decimal(10,2) DEFAULT NULL,
  `shrikhand` decimal(10,2) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `sgst` decimal(10,2) DEFAULT NULL,
  `product_details` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `hsn_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `catalog_no` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `order_no` int DEFAULT NULL,
  `cgst` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  UNIQUE KEY `mst_items_UN` (`app_id`,`product_code`,`activate_flag`),
  KEY `parent_category_id` (`parent_category_id`) USING BTREE,
  KEY `mst_items_parent_category_id_IDX` (`parent_category_id`,`activate_flag`,`app_id`) USING BTREE,
  KEY `mst_items_app_id_IDX` (`app_id`) USING BTREE,
  KEY `mst_items_app_id_IDX_2` (`app_id`,`activate_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=588531 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_qr_code`
--

DROP TABLE IF EXISTS `mst_qr_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_qr_code` (
  `qr_id` bigint NOT NULL AUTO_INCREMENT,
  `qr_code_number` varchar(30) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `currently_assigned_to` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  PRIMARY KEY (`qr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_store`
--

DROP TABLE IF EXISTS `mst_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_store` (
  `store_id` int NOT NULL AUTO_INCREMENT,
  `store_name` varchar(45) NOT NULL,
  `address_line_1` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `store_email` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` int DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint NOT NULL,
  `address_line_2` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `city` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `pincode` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `gst_no` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `mobile_no` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `address_line_3` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `store_timing` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_tables`
--

DROP TABLE IF EXISTS `mst_tables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_tables` (
  `table_id` bigint NOT NULL AUTO_INCREMENT,
  `store_id` bigint NOT NULL,
  `table_no` varchar(100) DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=161 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_terms_and_conditions`
--

DROP TABLE IF EXISTS `mst_terms_and_conditions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_terms_and_conditions` (
  `terms_condition_id` bigint NOT NULL AUTO_INCREMENT,
  `terms_condition_content` varchar(500) NOT NULL,
  `order` int NOT NULL,
  `app_id` tinyint DEFAULT NULL,
  PRIMARY KEY (`terms_condition_id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_vehicle`
--

DROP TABLE IF EXISTS `mst_vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_vehicle` (
  `vehicle_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint DEFAULT NULL,
  `vehicle_name` varchar(100) DEFAULT NULL,
  `vehicle_number` varchar(30) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mst_vendor`
--

DROP TABLE IF EXISTS `mst_vendor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mst_vendor` (
  `vendor_id` bigint NOT NULL AUTO_INCREMENT,
  `vendor_name` varchar(100) DEFAULT NULL,
  `mobile_number` mediumtext NOT NULL,
  `city` varchar(50) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `activate_flag` tinyint NOT NULL,
  `created_Date` datetime NOT NULL,
  `updated_by` int DEFAULT NULL,
  `updated_Date` datetime DEFAULT NULL,
  `alternate_mobile_no` varchar(10) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `vendor_reference` varchar(1000) DEFAULT NULL,
  `gst_no` varchar(100) DEFAULT NULL,
  `business_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`vendor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nozzle_master`
--

DROP TABLE IF EXISTS `nozzle_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nozzle_master` (
  `nozzle_id` bigint NOT NULL AUTO_INCREMENT,
  `nozzle_name` varchar(100) DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `parent_dispenser_id` bigint DEFAULT NULL,
  PRIMARY KEY (`nozzle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quote_terms_details`
--

DROP TABLE IF EXISTS `quote_terms_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quote_terms_details` (
  `quote_terms_id` bigint NOT NULL AUTO_INCREMENT,
  `quote_id` varchar(100) DEFAULT NULL,
  `term` varchar(300) DEFAULT NULL,
  `order` int DEFAULT NULL,
  PRIMARY KEY (`quote_terms_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rlt_composite_item_mpg`
--

DROP TABLE IF EXISTS `rlt_composite_item_mpg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rlt_composite_item_mpg` (
  `composite_id` bigint NOT NULL AUTO_INCREMENT,
  `item_id` bigint DEFAULT NULL,
  `child_item_id` bigint DEFAULT NULL,
  `qty` double DEFAULT NULL,
  PRIMARY KEY (`composite_id`),
  KEY `rlt_composite_item_mpg_item_id_IDX` (`item_id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rlt_invoice_fuel_details`
--

DROP TABLE IF EXISTS `rlt_invoice_fuel_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rlt_invoice_fuel_details` (
  `rlt_invoice_fuel_pk` bigint NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint DEFAULT NULL,
  `shift_id` bigint DEFAULT NULL,
  `attendant_id` bigint DEFAULT NULL,
  `nozzle_id` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `swipe_id` bigint DEFAULT NULL,
  PRIMARY KEY (`rlt_invoice_fuel_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=310 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seq_master`
--

DROP TABLE IF EXISTS `seq_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seq_master` (
  `sequence_id` bigint NOT NULL AUTO_INCREMENT,
  `sequence_name` varchar(100) NOT NULL,
  `current_seq_no` bigint NOT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`sequence_id`),
  KEY `seq_master_app_id_IDX` (`app_id`,`sequence_name`) USING BTREE,
  KEY `seq_master_sequence_name_IDX` (`sequence_name`,`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shift_master`
--

DROP TABLE IF EXISTS `shift_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift_master` (
  `shift_id` bigint NOT NULL AUTO_INCREMENT,
  `shift_name` varchar(100) DEFAULT NULL,
  `from_time` time DEFAULT NULL,
  `to_time` time DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`shift_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_modification_addremove`
--

DROP TABLE IF EXISTS `stock_modification_addremove`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_modification_addremove` (
  `stock_modification_addremove_id` bigint NOT NULL AUTO_INCREMENT,
  `stock_modification_id` bigint NOT NULL,
  `item_id` bigint DEFAULT NULL,
  `current_stock` decimal(10,0) DEFAULT NULL,
  `qty` decimal(10,0) DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`stock_modification_addremove_id`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_modification_inventorycounting`
--

DROP TABLE IF EXISTS `stock_modification_inventorycounting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_modification_inventorycounting` (
  `stock_modification_inventorycounting_id` bigint NOT NULL AUTO_INCREMENT,
  `stock_modification_id` bigint NOT NULL,
  `item_id` bigint DEFAULT NULL,
  `expected_count` decimal(10,0) NOT NULL,
  `current_count` decimal(10,0) DEFAULT NULL,
  `difference` decimal(10,0) DEFAULT NULL,
  `difference_amount` decimal(10,0) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`stock_modification_inventorycounting_id`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_modification_master`
--

DROP TABLE IF EXISTS `stock_modification_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_modification_master` (
  `stock_modification_id` bigint NOT NULL AUTO_INCREMENT,
  `stock_modification_type` varchar(100) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_user` bigint DEFAULT NULL,
  `store_id` bigint DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`stock_modification_id`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_modification_transferstock`
--

DROP TABLE IF EXISTS `stock_modification_transferstock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_modification_transferstock` (
  `stock_modification_transferstock_id` bigint NOT NULL AUTO_INCREMENT,
  `stock_modification_id` bigint NOT NULL,
  `item_id` bigint DEFAULT NULL,
  `sourcebefore` decimal(10,0) DEFAULT NULL,
  `sourceafter` decimal(10,0) NOT NULL,
  `qty` decimal(10,0) DEFAULT NULL,
  `destinationbefore` decimal(10,0) DEFAULT NULL,
  `destinationafter` decimal(10,0) DEFAULT NULL,
  `sourcestore` bigint DEFAULT NULL,
  `destinationstore` bigint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`stock_modification_transferstock_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_status`
--

DROP TABLE IF EXISTS `stock_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_status` (
  `stock_id` bigint NOT NULL AUTO_INCREMENT,
  `store_id` bigint DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `qty_available` decimal(10,3) DEFAULT NULL,
  `activate_flag` tinyint NOT NULL,
  `low_stock_limit` decimal(10,3) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`stock_id`),
  KEY `stock_status_item_id_IDX` (`item_id`,`store_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=627 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store_item_mpg`
--

DROP TABLE IF EXISTS `store_item_mpg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_item_mpg` (
  `store_item_id` bigint NOT NULL AUTO_INCREMENT,
  `store_id` bigint NOT NULL,
  `item_id` bigint DEFAULT NULL,
  `update_date` bigint NOT NULL,
  `updated_by` bigint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`store_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14939 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `swipe_machine_master`
--

DROP TABLE IF EXISTS `swipe_machine_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `swipe_machine_master` (
  `swipe_machine_id` bigint NOT NULL AUTO_INCREMENT,
  `swipe_machine_name` varchar(100) DEFAULT NULL,
  `bank_id` bigint DEFAULT NULL,
  `swipe_machine_account_no` varchar(20) DEFAULT NULL,
  `swipe_machine_short_name` varchar(10) DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`swipe_machine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_attachment_mst`
--

DROP TABLE IF EXISTS `tbl_attachment_mst`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_attachment_mst` (
  `attachment_id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(200) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `file_id` int DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `attachment_asblob` longblob,
  PRIMARY KEY (`attachment_id`),
  KEY `tbl_attachment_mst_file_id_IDX` (`file_id`) USING BTREE,
  KEY `tbl_attachment_mst_file_id_IDX_1` (`file_id`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=660 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_user_mst`
--

DROP TABLE IF EXISTS `tbl_user_mst`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_user_mst` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `activate_flag` tinyint DEFAULT '1',
  `name` varchar(200) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `store_id` bigint NOT NULL,
  `app_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `tbl_user_mst_username_IDX` (`username`,`password`,`activate_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_bank_reconcilation`
--

DROP TABLE IF EXISTS `trn_bank_reconcilation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_bank_reconcilation` (
  `reconcilation_id` bigint NOT NULL AUTO_INCREMENT,
  `bank_account_id` bigint DEFAULT NULL,
  `reconcilation_date` datetime DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`reconcilation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_booking_register`
--

DROP TABLE IF EXISTS `trn_booking_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_booking_register` (
  `booking_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `from_date` datetime NOT NULL,
  `to_date` datetime DEFAULT NULL,
  `preffered_employee` bigint NOT NULL,
  `app_id` bigint NOT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `activate_flag` tinyint NOT NULL,
  `status` varchar(100) DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `model` varchar(100) DEFAULT NULL,
  `uniqueno` varchar(100) DEFAULT NULL,
  `store_id` bigint DEFAULT NULL,
  PRIMARY KEY (`booking_id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_cash_to_vault`
--

DROP TABLE IF EXISTS `trn_cash_to_vault`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_cash_to_vault` (
  `submission_id` bigint NOT NULL AUTO_INCREMENT,
  `supervisor_id` bigint DEFAULT NULL,
  `shift_id` bigint DEFAULT NULL,
  `accounting_date` date DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `notes` bigint DEFAULT NULL,
  `coins` bigint DEFAULT NULL,
  PRIMARY KEY (`submission_id`)
) ENGINE=MyISAM AUTO_INCREMENT=57 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_cashback_register`
--

DROP TABLE IF EXISTS `trn_cashback_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_cashback_register` (
  `cashback_id` int NOT NULL AUTO_INCREMENT,
  `orderId` bigint DEFAULT NULL,
  `orderType` varchar(10) NOT NULL,
  `cashback_percentage` float(5,2) DEFAULT NULL,
  `cashback_amount` float(10,2) DEFAULT NULL,
  `created_date` datetime NOT NULL,
  PRIMARY KEY (`cashback_id`),
  KEY `mst_cashback_id` (`orderType`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_expense_register`
--

DROP TABLE IF EXISTS `trn_expense_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_expense_register` (
  `expense_id` bigint NOT NULL AUTO_INCREMENT,
  `expense_date` date DEFAULT NULL,
  `expense_name` varchar(1000) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `app_id` int DEFAULT NULL,
  `qty` double DEFAULT NULL,
  `store_id` bigint DEFAULT NULL,
  PRIMARY KEY (`expense_id`),
  KEY `trn_expense_register_app_id_IDX` (`app_id`) USING BTREE,
  KEY `trn_expense_register_expense_date_IDX` (`expense_date`) USING BTREE,
  KEY `trn_expense_register_activate_flag_IDX` (`activate_flag`,`app_id`,`expense_date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7245 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_fuel_price_register`
--

DROP TABLE IF EXISTS `trn_fuel_price_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_fuel_price_register` (
  `trn_fuel_id` bigint NOT NULL AUTO_INCREMENT,
  `fuel_id` bigint DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `from_date_time` datetime DEFAULT NULL,
  `to_date_time` datetime DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`trn_fuel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_incoming_online_payments`
--

DROP TABLE IF EXISTS `trn_incoming_online_payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_incoming_online_payments` (
  `order_id` varchar(50) NOT NULL,
  `bhim_upi_id` varchar(100) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `date_time_from_payment` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `claimed_by_user_id` bigint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `shift_date` date DEFAULT NULL,
  `accepted_shift_id` bigint DEFAULT NULL,
  `store_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_invoice_details`
--

DROP TABLE IF EXISTS `trn_invoice_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_invoice_details` (
  `details_id` bigint NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `qty` decimal(10,3) DEFAULT NULL,
  `rate` decimal(10,2) DEFAULT NULL,
  `custom_rate` decimal(10,2) DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `gst_amount` decimal(10,2) DEFAULT NULL,
  `weight` decimal(10,3) DEFAULT NULL,
  `size` varchar(100) DEFAULT NULL,
  `purchase_details_id` bigint DEFAULT NULL,
  `sgst_percentage` decimal(10,2) DEFAULT NULL,
  `sgst_amount` decimal(10,2) DEFAULT NULL,
  `cgst_percentage` decimal(10,2) DEFAULT NULL,
  `cgst_amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`details_id`),
  KEY `trn_invoice_details_invoice_id_IDX` (`invoice_id`) USING BTREE,
  KEY `trn_invoice_details_purchase_details_id_IDX` (`purchase_details_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=280622 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_invoice_register`
--

DROP TABLE IF EXISTS `trn_invoice_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_invoice_register` (
  `invoice_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `gross_amount` decimal(10,2) NOT NULL,
  `item_discount` decimal(10,2) DEFAULT NULL,
  `invoice_discount` decimal(10,2) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `payment_type` varchar(100) DEFAULT NULL,
  `invoice_date` date NOT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `store_id` bigint DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `invoice_no` bigint DEFAULT NULL,
  `total_gst` decimal(10,2) DEFAULT NULL,
  `model_no` varchar(100) DEFAULT NULL,
  `unique_no` varchar(100) DEFAULT NULL,
  `total_sgst` decimal(10,2) DEFAULT NULL,
  `total_cgst` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`invoice_id`),
  KEY `trn_invoice_register_invoice_date_IDX` (`invoice_date`) USING BTREE,
  KEY `trn_invoice_register_customer_id_IDX` (`customer_id`) USING BTREE,
  KEY `trn_invoice_register_app_id_IDX` (`app_id`,`invoice_date`) USING BTREE,
  KEY `trn_invoice_register_app_id_activate_flag_IDX` (`app_id`,`activate_flag`) USING BTREE,
  KEY `trn_invoice_register_invoice_id_IDX` (`invoice_id`,`activate_flag`) USING BTREE,
  KEY `trn_invoice_register_app_id_IDX_invoice_date` (`app_id`,`invoice_date`) USING BTREE,
  KEY `trn_invoice_register_invoice_no_IDX` (`invoice_no`,`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=116621 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_lr_register`
--

DROP TABLE IF EXISTS `trn_lr_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_lr_register` (
  `lr_no` int NOT NULL,
  `stockist_name` varchar(300) DEFAULT NULL,
  `wadhwan_to` varchar(200) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `city` varchar(300) DEFAULT NULL,
  `tel_no` varchar(10) DEFAULT NULL,
  `truck_no` varchar(20) DEFAULT NULL,
  `weight` int DEFAULT NULL,
  `cement` varchar(10) DEFAULT NULL,
  `bags` int DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`lr_no`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_nozzle_register`
--

DROP TABLE IF EXISTS `trn_nozzle_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_nozzle_register` (
  `trn_nozzle_id` bigint NOT NULL AUTO_INCREMENT,
  `nozzle_id` bigint DEFAULT NULL,
  `attendant_id` bigint DEFAULT NULL,
  `check_in_time` datetime DEFAULT NULL,
  `check_out_time` datetime DEFAULT NULL,
  `opening_reading` decimal(20,4) DEFAULT NULL,
  `closing_reading` decimal(20,4) DEFAULT NULL,
  `totalizer_opening_reading` decimal(20,4) DEFAULT NULL,
  `totalizer_closing_reading` decimal(20,4) DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `shift_id` bigint DEFAULT NULL,
  `rate` decimal(10,3) DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `accounting_date` date DEFAULT NULL,
  PRIMARY KEY (`trn_nozzle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_order_details`
--

DROP TABLE IF EXISTS `trn_order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_order_details` (
  `order_details_id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `qty` double DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `ordered_time` datetime DEFAULT NULL,
  `served_time` datetime DEFAULT NULL,
  `cancelled_time` datetime DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `running_flag` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`order_details_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3867 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_order_register`
--

DROP TABLE IF EXISTS `trn_order_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_order_register` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `table_id` bigint NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1581 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_order_register_frommobileapp`
--

DROP TABLE IF EXISTS `trn_order_register_frommobileapp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_order_register_frommobileapp` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `number` bigint DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `activate_flag` tinyint NOT NULL DEFAULT '0',
  `remarks` varchar(100) DEFAULT NULL,
  `orderType` varchar(10) DEFAULT NULL,
  `amount` float(8,2) NOT NULL,
  `previouscashbackamountused` float(8,2) DEFAULT '0.00',
  `amounttopay` float(8,2) NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `curr_status` varchar(1) NOT NULL,
  `special_cooking_instruction` varchar(300) DEFAULT NULL,
  `gst_tax` float NOT NULL,
  `pushUniqueToken` varchar(200) NOT NULL,
  `app_id` bigint NOT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=MyISAM AUTO_INCREMENT=55 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_otp_register`
--

DROP TABLE IF EXISTS `trn_otp_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_otp_register` (
  `register_id` int NOT NULL DEFAULT '0',
  `Mobile_number` bigint DEFAULT NULL,
  `otp` smallint DEFAULT NULL,
  `time_start` datetime DEFAULT NULL,
  `time_end` datetime DEFAULT NULL,
  `activate_flag` tinyint NOT NULL DEFAULT '0',
  `app_id` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_payment_register`
--

DROP TABLE IF EXISTS `trn_payment_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_payment_register` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint DEFAULT NULL,
  `payment_date` date DEFAULT NULL,
  `payment_mode` varchar(100) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `store_id` bigint DEFAULT NULL,
  `ref_id` bigint DEFAULT NULL,
  `payment_for` varchar(100) DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `activate_flag` bigint DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `trn_payment_register_payment_for_IDX` (`payment_for`) USING BTREE,
  KEY `trn_payment_register_payment_date_IDX` (`payment_date`) USING BTREE,
  KEY `trn_payment_register_payment_for_payment_date_IDX` (`payment_for`,`payment_date`) USING BTREE,
  KEY `trn_payment_register_customer_id_IDX` (`customer_id`) USING BTREE,
  KEY `trn_payment_register_ref_id_IDX` (`ref_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71270 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_purchase_invoice_details`
--

DROP TABLE IF EXISTS `trn_purchase_invoice_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_purchase_invoice_details` (
  `details_id` bigint NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `qty` decimal(10,3) DEFAULT NULL,
  `rate` decimal(10,2) DEFAULT NULL,
  `sgst_amount` decimal(10,2) DEFAULT NULL,
  `sgst_percentage` decimal(10,2) DEFAULT NULL,
  `cgst_amount` decimal(10,2) DEFAULT NULL,
  `cgst_percentage` decimal(10,2) DEFAULT NULL,
  `item_amount` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`details_id`),
  KEY `trn_purchase_invoice_details_item_id_IDX` (`item_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_purchase_invoice_register`
--

DROP TABLE IF EXISTS `trn_purchase_invoice_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_purchase_invoice_register` (
  `invoice_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `gross_amount` decimal(10,2) NOT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `invoice_date` datetime NOT NULL,
  `updated_by` bigint NOT NULL,
  `updated_date` datetime NOT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `store_id` bigint NOT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `app_id` bigint NOT NULL,
  `invoice_no` bigint NOT NULL,
  `total_gst` decimal(10,2) NOT NULL,
  `tally_ref_no` varchar(100) DEFAULT NULL,
  `vendor_invoice_no` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`invoice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_quote_details`
--

DROP TABLE IF EXISTS `trn_quote_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_quote_details` (
  `details_id` bigint NOT NULL AUTO_INCREMENT,
  `quote_id` bigint DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `qty` decimal(10,3) DEFAULT NULL,
  `rate` decimal(10,0) DEFAULT NULL,
  `custom_rate` decimal(10,0) DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `gst_amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`details_id`),
  KEY `trn_invoice_details_invoice_id_IDX` (`quote_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19480 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_quote_register`
--

DROP TABLE IF EXISTS `trn_quote_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_quote_register` (
  `quote_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `gross_amount` decimal(10,2) NOT NULL,
  `item_discount` decimal(10,2) DEFAULT NULL,
  `invoice_discount` decimal(10,2) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `payment_type` varchar(100) DEFAULT NULL,
  `quote_date` datetime NOT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `store_id` bigint DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `quote_no` varchar(100) DEFAULT NULL,
  `total_gst` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`quote_id`),
  KEY `trn_invoice_register_invoice_date_IDX` (`quote_date`) USING BTREE,
  KEY `trn_invoice_register_customer_id_IDX` (`customer_id`) USING BTREE,
  KEY `trn_invoice_register_app_id_IDX` (`app_id`,`quote_date`) USING BTREE,
  KEY `trn_invoice_register_app_id_activate_flag_IDX` (`app_id`,`activate_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11523 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_return_register`
--

DROP TABLE IF EXISTS `trn_return_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_return_register` (
  `return_id` bigint NOT NULL AUTO_INCREMENT,
  `details_id` bigint NOT NULL,
  `qty_to_return` decimal(10,2) NOT NULL,
  `updated_by` bigint NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`return_id`),
  KEY `trn_return_register_details_id_IDX` (`details_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=290 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_sph_details`
--

DROP TABLE IF EXISTS `trn_sph_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_sph_details` (
  `details_id` bigint DEFAULT NULL,
  `sph_r` varchar(100) DEFAULT NULL,
  `sph_l` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_stock_register`
--

DROP TABLE IF EXISTS `trn_stock_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_stock_register` (
  `stock_register_id` bigint NOT NULL AUTO_INCREMENT,
  `store_id` bigint NOT NULL,
  `item_id` bigint NOT NULL,
  `qty` decimal(10,2) NOT NULL,
  `type` varchar(100) NOT NULL,
  `updated_by` bigint NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `invoice_id` bigint DEFAULT NULL,
  `stock_date` date DEFAULT NULL,
  `closing_qty` decimal(10,2) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  PRIMARY KEY (`stock_register_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2359 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_suborder_register`
--

DROP TABLE IF EXISTS `trn_suborder_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_suborder_register` (
  `suborder_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `item_id` int DEFAULT NULL,
  `item_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `item_price` int DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `remarks` varchar(300) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`suborder_id`)
) ENGINE=MyISAM AUTO_INCREMENT=74 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_supervisor_collection`
--

DROP TABLE IF EXISTS `trn_supervisor_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_supervisor_collection` (
  `collection_id` bigint NOT NULL AUTO_INCREMENT,
  `attendant_id` bigint NOT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `shift_id` bigint DEFAULT NULL,
  `collection_date` date DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `collection_mode` varchar(100) DEFAULT NULL,
  `shift_date` date DEFAULT NULL,
  PRIMARY KEY (`collection_id`)
) ENGINE=MyISAM AUTO_INCREMENT=424 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trn_test_fuel_register`
--

DROP TABLE IF EXISTS `trn_test_fuel_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trn_test_fuel_register` (
  `test_id` bigint NOT NULL AUTO_INCREMENT,
  `test_quantity` decimal(10,3) DEFAULT NULL,
  `nozzle_id` bigint DEFAULT NULL,
  `shift_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `test_date` datetime DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  `test_type` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`test_id`)
) ENGINE=MyISAM AUTO_INCREMENT=187 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_configurations`
--

DROP TABLE IF EXISTS `user_configurations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_configurations` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `invoice_format` tinyint NOT NULL DEFAULT '1',
  `invoice_default_checked_print` char(1) NOT NULL DEFAULT 'N',
  `invoice_default_checked_generatepdf` char(1) NOT NULL DEFAULT 'N',
  `restaurant_default_checked_generatepdf` char(1) NOT NULL DEFAULT '0',
  `invoice_type` tinyint NOT NULL DEFAULT '0',
  `user_total_payments` char(1) DEFAULT NULL,
  `user_payment_collections` char(1) DEFAULT NULL,
  `user_counter_sales` char(1) DEFAULT NULL,
  `user_payment_sales` char(1) DEFAULT NULL,
  `user_store_sales` char(1) DEFAULT NULL,
  `user_store_bookings` char(1) DEFAULT NULL,
  `user_store_expenses` char(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visitor_entry`
--

DROP TABLE IF EXISTS `visitor_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitor_entry` (
  `visitor_id` int NOT NULL AUTO_INCREMENT,
  `visitor_name` varchar(45) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `purpose_of_visit` varchar(100) DEFAULT NULL,
  `remarks` varchar(50) DEFAULT NULL,
  `col1` varchar(100) DEFAULT NULL,
  `mobile_no` varchar(100) DEFAULT NULL,
  `email_id` varchar(45) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `app_id` bigint NOT NULL,
  `in_time` datetime DEFAULT NULL,
  `activate_flag` tinyint DEFAULT NULL,
  PRIMARY KEY (`visitor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'customizedpos'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-08 13:38:40
