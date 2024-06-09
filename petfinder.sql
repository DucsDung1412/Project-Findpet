-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.27-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for petfinder
DROP DATABASE IF EXISTS `petfinder`;
CREATE DATABASE IF NOT EXISTS `petfinder` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `petfinder`;

-- Dumping structure for table petfinder.adopt
DROP TABLE IF EXISTS `adopt`;
CREATE TABLE IF NOT EXISTS `adopt` (
  `adopt_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adopt_date` date NOT NULL,
  `adopt_fee` double NOT NULL,
  `adopt_status` varchar(255) NOT NULL,
  `animals_id` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`adopt_id`),
  KEY `FK6ra331pqfosb5sle4rdqxfgc4` (`animals_id`),
  KEY `FKgaveljy9xmib0wm6aly8m9qu8` (`username`),
  CONSTRAINT `FK6ra331pqfosb5sle4rdqxfgc4` FOREIGN KEY (`animals_id`) REFERENCES `animals` (`animals_id`),
  CONSTRAINT `FKgaveljy9xmib0wm6aly8m9qu8` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.adopt: ~4 rows (approximately)
REPLACE INTO `adopt` (`adopt_id`, `adopt_date`, `adopt_fee`, `adopt_status`, `animals_id`, `username`) VALUES
	(2, '2024-05-22', 500000, 'đã nhân', 3, 'nguyenhoahung1007@gmail.com'),
	(3, '2024-05-20', 300000, 'chưa nhận', 4, 'nguyenhoahung1007@gmail.com'),
	(4, '2024-05-10', 500000, 'đã nhận', 6, 'nguyenhoahung1007@gmail.com'),
	(5, '2024-05-21', 100000, 'đã nhận', 14, 'daoducdung2000@gmail.com'),
	(6, '2024-05-18', 400000, 'đã nhân', 16, 'daoducdung2000@gmail.com');

-- Dumping structure for table petfinder.adopter_profile
DROP TABLE IF EXISTS `adopter_profile`;
CREATE TABLE IF NOT EXISTS `adopter_profile` (
  `profile_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `profile_age_of_pet` varchar(255) NOT NULL,
  `profile_breed_of_pet` varchar(255) NOT NULL,
  `profile_desired` varchar(50) NOT NULL,
  `profile_experience` bit(1) NOT NULL,
  `profile_gender_of_pet` bit(1) NOT NULL,
  `profile_need` varchar(255) NOT NULL,
  `profile_outdoor` bit(1) NOT NULL,
  `profile_pets_at_home` varchar(50) NOT NULL,
  `profile_size_of_pet` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`profile_id`),
  UNIQUE KEY `UK_h5b3yw2obiwujmn9yny2mk2eo` (`username`),
  CONSTRAINT `FKd3ix6ntwho74p9244667i4k07` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.adopter_profile: ~3 rows (approximately)
REPLACE INTO `adopter_profile` (`profile_id`, `profile_age_of_pet`, `profile_breed_of_pet`, `profile_desired`, `profile_experience`, `profile_gender_of_pet`, `profile_need`, `profile_outdoor`, `profile_pets_at_home`, `profile_size_of_pet`, `username`) VALUES
	(0, '2', 'Mèo Anh', '2', b'1', b'0', '1', b'0', '1', 'nhỏ', 'nguyenhoahung1007@gmail.com'),
	(2, '3', 'Mèo Mỹ', '1', b'1', b'1', '1', b'1', '1', 'nhỏ', 'daoducdung2000@gmail.com'),
	(3, '1', 'Chó Phốc', '2', b'1', b'1', '1', b'1', '1', 'nhỏ', 'admin');

-- Dumping structure for table petfinder.animals
DROP TABLE IF EXISTS `animals`;
CREATE TABLE IF NOT EXISTS `animals` (
  `animals_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `animal_age` varchar(255) NOT NULL,
  `animal_avatar` varchar(255) NOT NULL,
  `animal_date` date NOT NULL,
  `animal_gender` bit(1) NOT NULL,
  `animal_name` varchar(50) NOT NULL,
  `animal_size` varchar(255) NOT NULL,
  `breed_id` bigint(20) NOT NULL,
  `shelter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`animals_id`),
  UNIQUE KEY `UK_5vkomwnfb04e0pqjrahduar15` (`breed_id`),
  KEY `FK7fmlpw3o4ourhtv3qy8gl6cn5` (`shelter_id`),
  CONSTRAINT `FK7fmlpw3o4ourhtv3qy8gl6cn5` FOREIGN KEY (`shelter_id`) REFERENCES `shelters` (`shelters_id`),
  CONSTRAINT `FKdwdlcloh1grxeqasi88bqdth2` FOREIGN KEY (`breed_id`) REFERENCES `breed` (`breed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.animals: ~20 rows (approximately)
REPLACE INTO `animals` (`animals_id`, `animal_age`, `animal_avatar`, `animal_date`, `animal_gender`, `animal_name`, `animal_size`, `breed_id`, `shelter_id`) VALUES
	(3, '2', '', '2024-05-21', b'1', 'đen', 'vừa', 1, 1),
	(4, '2', '', '2024-04-15', b'0', 'xám', 'nhỏ', 2, 1),
	(5, '1', '', '2024-04-20', b'1', 'trắng', 'nhỏ', 3, 1),
	(6, '3', '', '2024-05-10', b'1', 'xám', 'nhỏ', 4, 1),
	(7, '2', '', '2024-05-05', b'0', 'xám', 'nhỏ', 5, 1),
	(9, '4', '', '2024-05-08', b'1', 'đen', 'nhỏ', 6, 1),
	(10, '3', '', '2024-05-18', b'0', 'nâu, trắng', 'nhỏ', 7, 1),
	(11, '4', '', '2024-03-28', b'0', 'trắng, đen', 'nhỏ', 8, 1),
	(12, '4', '', '2024-04-28', b'0', 'đen', 'nhỏ', 9, 1),
	(13, '3', '', '2024-04-25', b'1', 'xám, trắng', 'nhỏ', 10, 1),
	(14, '2', '', '2024-04-18', b'1', 'trắng', 'vừa', 11, 2),
	(15, '3', '', '2024-03-20', b'0', 'vàng, trắng', 'nhỏ', 12, 2),
	(16, '2', '', '2024-03-15', b'0', 'vàng', 'nhỏ', 13, 2),
	(19, '1', '', '2024-03-19', b'0', 'đen', 'nhỏ', 14, 2),
	(20, '5', '', '2024-04-05', b'1', 'đen', 'nhỏ', 15, 2),
	(21, '3', '', '2024-04-22', b'1', 'trắng,đen', 'vừa', 16, 2),
	(22, '3', '', '2024-04-08', b'1', 'trắng,đen', 'vừa', 17, 2),
	(23, '5', '', '2024-05-03', b'0', 'vàng', 'to', 18, 2),
	(25, '4', '', '2024-05-10', b'0', 'vàng,đen', 'to', 19, 2),
	(26, '3', '', '2024-04-22', b'1', 'nâu', 'nhỏ', 20, 2);

-- Dumping structure for table petfinder.animal_info
DROP TABLE IF EXISTS `animal_info`;
CREATE TABLE IF NOT EXISTS `animal_info` (
  `animal_info_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `animal_info_characteristics` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `animal_info_color` varchar(255) NOT NULL,
  `animal_info_description` longtext NOT NULL,
  `animal_info_harmony` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `animal_info_health` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `animal_info_leg` varchar(20) NOT NULL,
  `animal_id` bigint(20) NOT NULL,
  PRIMARY KEY (`animal_info_id`),
  UNIQUE KEY `UK_4hm1c1u7ue99do9436mbr0p0s` (`animal_id`),
  CONSTRAINT `FKtidvsoxsb0fldq9v5vj17fbug` FOREIGN KEY (`animal_id`) REFERENCES `animals` (`animals_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.animal_info: ~2 rows (approximately)
REPLACE INTO `animal_info` (`animal_info_id`, `animal_info_characteristics`, `animal_info_color`, `animal_info_description`, `animal_info_harmony`, `animal_info_health`, `animal_info_leg`, `animal_id`) VALUES
	(1, 'Lông ngắn', 'Trắng', '', '', '', 'Chân ngắn', 5),
	(2, '', 'Đen', '', '', '', '', 9);

-- Dumping structure for table petfinder.authorities
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE IF NOT EXISTS `authorities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authority` varchar(18) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK2uf74smucdwf9qal2n67m2343` (`username`,`authority`),
  UNIQUE KEY `UK_baahryprcge2u172egph1qwur` (`username`),
  CONSTRAINT `FKhjuy9y4fd8v5m3klig05ktofg` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.authorities: ~2 rows (approximately)
REPLACE INTO `authorities` (`id`, `authority`, `username`) VALUES
	(1, 'admin', 'admin'),
	(3, 'dung', 'daoducdung2000@gmail.com'),
	(2, 'hung', 'nguyenhoahung1007@gmail.com');

-- Dumping structure for table petfinder.breed
DROP TABLE IF EXISTS `breed`;
CREATE TABLE IF NOT EXISTS `breed` (
  `breed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `breed_name` varchar(50) NOT NULL,
  `breed_type` varchar(50) NOT NULL,
  PRIMARY KEY (`breed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.breed: ~17 rows (approximately)
REPLACE INTO `breed` (`breed_id`, `breed_name`, `breed_type`) VALUES
	(1, 'Chó Việt', ''),
	(2, 'Mèo Anh', 'Lông ngắn'),
	(3, 'Mèo Anh', 'Lông dài'),
	(4, 'Mèo Mỹ', 'Lông ngắn'),
	(5, 'Mèo Mỹ', 'Lông dài'),
	(6, 'Mèo Bombay', ''),
	(7, 'Mèo Manx', ''),
	(8, 'Mèo Mau Ai cập', ''),
	(9, 'Mèo Sphynx', ''),
	(10, 'Mèo Mucnhkin', ''),
	(11, 'Chó Samoyed', ''),
	(12, 'Chỏ Corgi', ''),
	(13, 'Chó Shiba', ''),
	(14, 'Chó Phốc', ''),
	(15, 'Chó Chihuahua', ''),
	(16, 'Chó Alaska', ''),
	(17, 'Chó Husky', ''),
	(18, 'Chó Golden', ''),
	(19, 'Chó Becgie Đức', ''),
	(20, 'Chó Dachshund', '');

-- Dumping structure for table petfinder.favorites
DROP TABLE IF EXISTS `favorites`;
CREATE TABLE IF NOT EXISTS `favorites` (
  `favorites_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `favorites_date` date NOT NULL,
  `animals_id` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`favorites_id`),
  KEY `FKshxity0xxlmf9yhehp162898y` (`animals_id`),
  KEY `FKgun9e0l2253lebhqp387fxq1m` (`username`),
  CONSTRAINT `FKgun9e0l2253lebhqp387fxq1m` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  CONSTRAINT `FKshxity0xxlmf9yhehp162898y` FOREIGN KEY (`animals_id`) REFERENCES `animals` (`animals_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.favorites: ~0 rows (approximately)

-- Dumping structure for table petfinder.shares
DROP TABLE IF EXISTS `shares`;
CREATE TABLE IF NOT EXISTS `shares` (
  `shares_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shares_email_send_to` varchar(255) NOT NULL,
  `shares_date` date NOT NULL,
  `animals_id` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`shares_id`),
  KEY `FKqhxvyn647ip59hasbpfnqwp6f` (`animals_id`),
  KEY `FKh3h55il0vgeqjxj1nv919i8ry` (`username`),
  CONSTRAINT `FKh3h55il0vgeqjxj1nv919i8ry` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  CONSTRAINT `FKqhxvyn647ip59hasbpfnqwp6f` FOREIGN KEY (`animals_id`) REFERENCES `animals` (`animals_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.shares: ~0 rows (approximately)

-- Dumping structure for table petfinder.shelters
DROP TABLE IF EXISTS `shelters`;
CREATE TABLE IF NOT EXISTS `shelters` (
  `shelters_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shelter_address` varchar(255) NOT NULL,
  `shelter_avatar` varchar(255) NOT NULL,
  `shelter_date` date NOT NULL,
  `shelter_email` varchar(255) NOT NULL,
  `shelter_info_mission` longtext NOT NULL,
  `shelter_info_operating_time` varchar(255) NOT NULL,
  `shelter_info_policy` longtext NOT NULL,
  `shelter_name` varchar(50) NOT NULL,
  `shelter_phone` varchar(15) NOT NULL,
  `username` varchar(255) NOT NULL,
  `shelter_info_facebook` varchar(255) NOT NULL,
  `shelter_info_instagram` varchar(255) NOT NULL,
  PRIMARY KEY (`shelters_id`),
  UNIQUE KEY `UK_2v8ty367ef8fjw8fgkrc35h9j` (`username`),
  CONSTRAINT `FKftq7te7nf7es314smvjd05a64` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.shelters: ~1 rows (approximately)
REPLACE INTO `shelters` (`shelters_id`, `shelter_address`, `shelter_avatar`, `shelter_date`, `shelter_email`, `shelter_info_mission`, `shelter_info_operating_time`, `shelter_info_policy`, `shelter_name`, `shelter_phone`, `username`, `shelter_info_facebook`, `shelter_info_instagram`) VALUES
	(1, 'Quận 12, Thành Phố Hồ Chí Minh', 'shelter_a1', '2022-04-22', 'hoahung@gmail.com', 'Trao những động vật vô chủ ', 'Hoạt động từ 8 giờ sáng tới 6 giờ chiều', 'Nếu bạn không thương chúng, hãy trao về chúng tôi', 'Trại cứu hộ động vật Hoà Hưng', '0358718634', 'nguyenhoahung1007@gmail.com', '', ''),
	(2, 'Quận Thủ Đức, Thành Phố Hồ Chí Minh', 'shelter_a2', '2022-05-10', 'ducdung@gmail.com', 'Gửi những động vật không có chỗ ở về với người chủ thương nó', 'Hoạt động từ 9 giờ sáng tới 6 giờ chiều', 'Nếu bạn không thương chúng, hãy trao về chúng tôi', 'Trại cứu hộ động vật Đức Dũng', '0238374434', 'daoducdung2000@gmail.com', '', '');

-- Dumping structure for table petfinder.spons
DROP TABLE IF EXISTS `spons`;
CREATE TABLE IF NOT EXISTS `spons` (
  `spons_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `spons_date` date NOT NULL,
  `spons_gift` double NOT NULL,
  `spons_message` longtext DEFAULT NULL,
  `shelter_id` bigint(20) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`spons_id`),
  KEY `FK6rlootno48jdbevqxxqlvk3l8` (`shelter_id`),
  KEY `FK3mtg69luh0np7s43lxgq62rh2` (`username`),
  CONSTRAINT `FK3mtg69luh0np7s43lxgq62rh2` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  CONSTRAINT `FK6rlootno48jdbevqxxqlvk3l8` FOREIGN KEY (`shelter_id`) REFERENCES `shelters` (`shelters_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.spons: ~0 rows (approximately)

-- Dumping structure for table petfinder.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(255) NOT NULL,
  `info_address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `info_avatar` varchar(255) DEFAULT NULL,
  `info_country` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `info_date` date NOT NULL,
  `enabled` bit(1) NOT NULL,
  `info_firstname` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `info_lastname` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) NOT NULL,
  `info_phone` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.users: ~2 rows (approximately)
REPLACE INTO `users` (`username`, `info_address`, `info_avatar`, `info_country`, `info_date`, `enabled`, `info_firstname`, `info_lastname`, `password`, `info_phone`) VALUES
	('admin', NULL, NULL, 'Việt Nam', '2000-02-20', b'0', 'admin', 'admin', 'admin', '0334947434'),
	('daoducdung2000@gmail.com', 'Quận Thủ Đức, Thành Phố Hồ Chí Minh', '', 'Việt Nam', '2000-12-09', b'0', 'Dũng', 'Đào', '123789', '0238374434'),
	('nguyenhoahung1007@gmail.com', 'Quận 12, Thành phố Hồ Chí Minh', NULL, 'Việt Nam', '2002-07-10', b'0', 'Hưng', 'Nguyễn', '123456', '0358718643');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
