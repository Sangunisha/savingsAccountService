/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`savings_account` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `savings_account`;

/*Table structure for table `account_statement` */

DROP TABLE IF EXISTS `account_statement`;

CREATE TABLE `account_statement` (
  `id` varchar(191) NOT NULL,
  `created_ts` bigint(20) DEFAULT NULL,
  `modified_ts` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `new_balance_amount` double NOT NULL,
  `old_balance_amount` double NOT NULL,
  `transaction_amount` double NOT NULL,
  `transaction_mode` int(11) DEFAULT NULL,
  `transaction_type` varchar(255) DEFAULT NULL,
  `account_id` varchar(191) DEFAULT NULL,
  `transaction_id` varchar(191) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb5tp3twltankgg5ukuvhgmmu8` (`account_id`),
  KEY `FKjygrbwvxxmbye1ggesjpl6xi2` (`transaction_id`),
  CONSTRAINT `FKb5tp3twltankgg5ukuvhgmmu8` FOREIGN KEY (`account_id`) REFERENCES `savings_account` (`id`),
  CONSTRAINT `FKjygrbwvxxmbye1ggesjpl6xi2` FOREIGN KEY (`transaction_id`) REFERENCES `transaction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `fund_transfer` */

DROP TABLE IF EXISTS `fund_transfer`;

CREATE TABLE `fund_transfer` (
  `id` varchar(191) NOT NULL,
  `created_ts` bigint(20) DEFAULT NULL,
  `modified_ts` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `amount` double NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `customer_id` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fund_transfer_type` varchar(255) DEFAULT NULL,
  `to_beneficiary` varchar(255) DEFAULT NULL,
  `from_account_id` varchar(191) DEFAULT NULL,
  `to_account_id` varchar(191) DEFAULT NULL,
  `transaction_id` varchar(191) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8ey9085c51fj3xr4y420mtde0` (`from_account_id`),
  KEY `FKkdw8diaicoi2x569ryttekmux` (`to_account_id`),
  KEY `FK87ykn0e1xwf5t38idf66otohy` (`transaction_id`),
  CONSTRAINT `FK87ykn0e1xwf5t38idf66otohy` FOREIGN KEY (`transaction_id`) REFERENCES `transaction` (`id`),
  CONSTRAINT `FK8ey9085c51fj3xr4y420mtde0` FOREIGN KEY (`from_account_id`) REFERENCES `savings_account` (`id`),
  CONSTRAINT `FKkdw8diaicoi2x569ryttekmux` FOREIGN KEY (`to_account_id`) REFERENCES `savings_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `savings_account` */

DROP TABLE IF EXISTS `savings_account`;

CREATE TABLE `savings_account` (
  `id` varchar(191) NOT NULL,
  `created_ts` bigint(20) DEFAULT NULL,
  `modified_ts` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `account_number` varchar(255) DEFAULT NULL,
  `balance_amount` double DEFAULT NULL,
  `branch_id` varchar(255) DEFAULT NULL,
  `customer_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `transaction` */

DROP TABLE IF EXISTS `transaction`;

CREATE TABLE `transaction` (
  `id` varchar(191) NOT NULL,
  `created_ts` bigint(20) DEFAULT NULL,
  `modified_ts` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `amount` double NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `failure_reason` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `transaction_status` varchar(255) DEFAULT NULL,
  `customer_id` varchar(255) DEFAULT NULL,
  `account_id` varchar(191) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmgyapv4th7oovdj91tw3dusy4` (`account_id`),
  CONSTRAINT `FKmgyapv4th7oovdj91tw3dusy4` FOREIGN KEY (`account_id`) REFERENCES `savings_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
