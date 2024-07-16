-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
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
  `adopt_status` varchar(255) NOT NULL,
  `animals_id` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`adopt_id`),
  KEY `FK6ra331pqfosb5sle4rdqxfgc4` (`animals_id`),
  KEY `FKgaveljy9xmib0wm6aly8m9qu8` (`username`),
  CONSTRAINT `FK6ra331pqfosb5sle4rdqxfgc4` FOREIGN KEY (`animals_id`) REFERENCES `animals` (`animals_id`),
  CONSTRAINT `FKgaveljy9xmib0wm6aly8m9qu8` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.adopt: ~7 rows (approximately)
REPLACE INTO `adopt` (`adopt_id`, `adopt_date`, `adopt_status`, `animals_id`, `username`) VALUES
	(1, '2024-03-10', 'Awaiting', 13, 'daoducdung2000@gmail.com'),
	(2, '2024-06-10', 'Adopted', 16, 'dungddps26188@fpt.edu.vn'),
	(3, '2024-07-15', 'Cancel', 31, 'daoducdung2000@gmail.com'),
	(5, '2024-06-10', 'Adopted', 3, 'dungddps26188@fpt.edu.vn'),
	(8, '2024-07-16', 'Adopted', 36, 'daoducdung2000@gmail.com'),
	(9, '2024-07-16', 'Cancel', 1, 'dungddps26188@fpt.edu.vn'),
	(16, '2024-07-15', 'Adopted', 46, 'daoducdung2000@gmail.com'),
	(23, '2024-07-16', 'Cancel', 8, 'daoducdung2000@gmail.com');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.adopter_profile: ~0 rows (approximately)

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
  KEY `FKdwdlcloh1grxeqasi88bqdth2` (`breed_id`),
  KEY `FK7fmlpw3o4ourhtv3qy8gl6cn5` (`shelter_id`),
  CONSTRAINT `FK7fmlpw3o4ourhtv3qy8gl6cn5` FOREIGN KEY (`shelter_id`) REFERENCES `shelters` (`shelters_id`),
  CONSTRAINT `FKdwdlcloh1grxeqasi88bqdth2` FOREIGN KEY (`breed_id`) REFERENCES `breed` (`breed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.animals: ~33 rows (approximately)
REPLACE INTO `animals` (`animals_id`, `animal_age`, `animal_avatar`, `animal_date`, `animal_gender`, `animal_name`, `animal_size`, `breed_id`, `shelter_id`) VALUES
	(1, 'Kitten', 'avatar13.jpg', '2023-02-12', b'1', 'Toby', 'Small', 3, 2),
	(2, 'Senior', 'avatar12.jpg', '2019-10-05', b'0', 'Sadie', 'Large', 2, 2),
	(3, 'Senior', 'avatar20.jpg', '2020-04-18', b'0', 'Ruby', 'Extra Large', 10, 2),
	(4, 'Kitten', 'avatar9.jpg', '2023-03-14', b'0', 'Rocky', 'Small', 9, 2),
	(5, 'Adult', 'avatar15.jpg', '2021-09-18', b'1', 'Riley', 'Large', 5, 2),
	(6, 'Senior', 'avatar16.jpg', '2020-01-20', b'0', 'Oscar', 'Extra Large', 6, 2),
	(7, 'Young', 'avatar6.jpg', '2022-09-18', b'1', 'Molly', 'Medium', 6, 2),
	(8, 'Puppy', 'avatar1.jpg', '2023-01-10', b'0', 'Max', 'Small', 1, 2),
	(9, 'Young', 'avatar14.jpg', '2022-08-15', b'0', 'Maggie', 'Medium', 4, 2),
	(10, 'Young', 'avatar10.jpg', '2022-07-22', b'1', 'Luna', 'Medium', 10, 2),
	(11, 'Senior', 'avatar4.jpg', '2019-03-25', b'1', 'Lucy', 'Extra Large', 4, 2),
	(12, 'Young', 'avatar2.jpg', '2022-05-15', b'1', 'Bella', 'Medium', 2, 2),
	(13, 'Adult', 'avatar3.jpg', '2021-11-20', b'0', 'Charlie', 'Large', 3, 0),
	(14, 'Young', 'avatar18.jpg', '2022-11-25', b'0', 'Harley', 'Medium', 8, 2),
	(15, 'Puppy', 'avatar5.jpg', '2023-04-10', b'0', 'Cooper', 'Small', 5, 2),
	(16, 'Adult', 'avatar19.jpg', '2021-07-14', b'1', 'Duke', 'Large', 9, 0),
	(17, 'Adult', 'avatar7.jpg', '2021-06-05', b'0', 'Buddy', 'Large', 7, 2),
	(18, 'Senior', 'avatar8.jpg', '2018-12-30', b'1', 'Daisy', 'Extra Large', 8, 2),
	(19, 'Kitten', 'avatar17.jpg', '2023-03-22', b'1', 'Coco', 'Small', 7, 2),
	(20, 'Adult', 'avatar11.jpg', '2021-08-10', b'1', 'Bailey', 'Medium', 1, 2),
	(31, 'Adult', 'avatar12.jpg', '2021-08-10', b'1', 'Bailey', 'Medium', 1, 2),
	(32, 'Senior', 'avatar12.jpg', '2019-10-05', b'0', 'Sadie', 'Large', 2, 2),
	(33, 'Puppy', 'avatar13.jpg', '2023-02-12', b'1', 'Toby', 'Small', 1, 2),
	(34, 'Young', 'avatar14.jpg', '2022-08-15', b'0', 'Maggie', 'Medium', 4, 2),
	(35, 'Adult', 'avatar15.jpg', '2021-09-18', b'1', 'Riley', 'Large', 5, 2),
	(36, 'Senior', 'avatar16.jpg', '2020-01-20', b'0', 'Oscar', 'Extra Large', 2, 2),
	(37, 'Kitten', 'avatar15.jpg', '2023-03-22', b'1', 'Coco', 'Small', 7, 2),
	(38, 'Young', 'avatar18.jpg', '2022-11-25', b'0', 'Harley', 'Medium', 8, 2),
	(39, 'Adult', 'avatar19.jpg', '2021-07-14', b'1', 'Duke', 'Large', 10, 2),
	(40, 'Senior', 'avatar20.jpg', '2020-04-18', b'0', 'Ruby', 'Extra Large', 1, 2),
	(45, 'Puppy', 'Screenshot (25).png', '2024-07-01', b'1', '1', 'Medium', 15, 2),
	(46, 'Adult', 'Screenshot (12).png', '2024-07-14', b'1', 'Thuat', 'Small', 16, 2),
	(47, 'Puppy', 'Screenshot (27).png', '2024-07-16', b'1', 'Hung', 'Small', 17, 2);

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.animal_info: ~1 rows (approximately)
REPLACE INTO `animal_info` (`animal_info_id`, `animal_info_characteristics`, `animal_info_color`, `animal_info_description`, `animal_info_harmony`, `animal_info_health`, `animal_info_leg`, `animal_id`) VALUES
	(5, '111', '1', '\n\n\nQuill Rich Text Editor\n\n\n\n\nQuill is a free, open-source WYSIWYG editor built for the modern web. With its modular architecture and expressive API, it is completely customizable to fit any need.\n\n\n\n\nInsipidity the sufficient discretion imprudence resolution sir him decisively. Proceed how any engaged visitor. Explained propriety off out perpetual his you. Feel sold off felt nay rose met you. We so entreaties cultivated astonished is. Was sister for a few longer Mrs sudden talent become. Done may bore quit evil old mile. If likely am of beauty tastes.\n\n\n\n\nAffronting imprudence do he he everything. Test lasted dinner wanted indeed wished outlaw. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.\n\n\n\n\nPost no so what deal evil rent by real in. But her ready least set lived spite solid. September how men saw tolerably two behavior arranging. She offices for highest and replied one venture pasture. Applauded no discovery in newspaper allowance am northward. Frequently partiality possession resolution at or appearance unaffected me. Engaged its was the evident pleased husband. Ye goodness felicity do disposal dwelling no. First am plate jokes to began to cause a scale. Subjects he prospect elegance followed no overcame possible it on.\n\nQuill is a free, open-source WYSIWYG editor built for the modern web. With its modular architecture and expressive API, it is completely customizable to fit any need.\n\n\n\n\nInsipidity the sufficient discretion imprudence resolution sir him decisively. Proceed how any engaged visitor. Explained propriety off out perpetual his you. Feel sold off felt nay rose met you. We so entreaties cultivated astonished is. Was sister for a few longer Mrs sudden talent become. Done may bore quit evil old mile. If likely am of beauty tastes.\n\n\n\n\nAffronting imprudence do he he everything. Test lasted dinner wanted indeed wished outlaw. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.\n\n\n\n\nPost no so what deal evil rent by real in. But her ready least set lived spite solid. September how men saw tolerably two behavior arranging. She offices for highest and replied one venture pasture. Applauded no discovery in newspaper allowance am northward. Frequently partiality possession resolution at or appearance unaffected me. Engaged its was the evident pleased husband. Ye goodness felicity do disposal dwelling no. First am plate jokes to began to cause a scale. Subjects he prospect elegance followed no overcame possible it on.\n\nQuill is a free, open-source WYSIWYG editor built for the modern web. With its modular architecture and expressive API, it is completely customizable to fit any need.\n\n\n\n\nInsipidity the sufficient discretion imprudence resolution sir him decisively. Proceed how any engaged visitor. Explained propriety off out perpetual his you. Feel sold off felt nay rose met you. We so entreaties cultivated astonished is. Was sister for a few longer Mrs sudden talent become. Done may bore quit evil old mile. If likely am of beauty tastes.\n\n\n\n\nAffronting imprudence do he he everything. Test lasted dinner wanted indeed wished outlaw. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.\n\n\n\n\nPost no so what deal evil rent by real in. But her ready least set lived spite solid. September how men saw tolerably two behavior arranging. She offices for highest and replied one venture pasture. Applauded no discovery in newspaper allowance am northward. Frequently partiality possession resolution at or appearance unaffected me. Engaged its was the evident pleased husband. Ye goodness felicity do disposal dwelling no. First am plate jokes to began to cause a scale. Subjects he prospect elegance followed no overcame possible it on.', 'Dog, Cat', 'Vaccinatedundefined, Dewormedundefined, Spayedundefined', 'Long', 45),
	(6, 'Hello', 'White', '\n\n\nQuill Rich Text Editor\n\n\n\n\nQuill is a free, open-source WYSIWYG editor built for the modern web. With its modular architecture and expressive API, it is completely customizable to fit any need.\n\n\n\n\nInsipidity the sufficient discretion imprudence resolution sir him decisively. Proceed how any engaged visitor. Explained propriety off out perpetual his you. Feel sold off felt nay rose met you. We so entreaties cultivated astonished is. Was sister for a few longer Mrs sudden talent become. Done may bore quit evil old mile. If likely am of beauty tastes.\n\n\n\n\nAffronting imprudence do he he everything. Test lasted dinner wanted indeed wished outlaw. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.\n\n\n\n\nPost no so what deal evil rent by real in. But her ready least set lived spite solid. September how men saw tolerably two behavior arranging. She offices for highest and replied one venture pasture. Applauded no discovery in newspaper allowance am northward. Frequently partiality possession resolution at or appearance unaffected me. Engaged its was the evident pleased husband. Ye goodness felicity do disposal dwelling no. First am plate jokes to began to cause a scale. Subjects he prospect elegance followed no overcame possible it on.\n\nQuill is a free, open-source WYSIWYG editor built for the modern web. With its modular architecture and expressive API, it is completely customizable to fit any need.\n\n\n\n\nInsipidity the sufficient discretion imprudence resolution sir him decisively. Proceed how any engaged visitor. Explained propriety off out perpetual his you. Feel sold off felt nay rose met you. We so entreaties cultivated astonished is. Was sister for a few longer Mrs sudden talent become. Done may bore quit evil old mile. If likely am of beauty tastes.\n\n\n\n\nAffronting imprudence do he he everything. Test lasted dinner wanted indeed wished outlaw. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.\n\n\n\n\nPost no so what deal evil rent by real in. But her ready least set lived spite solid. September how men saw tolerably two behavior arranging. She offices for highest and replied one venture pasture. Applauded no discovery in newspaper allowance am northward. Frequently partiality possession resolution at or appearance unaffected me. Engaged its was the evident pleased husband. Ye goodness felicity do disposal dwelling no. First am plate jokes to began to cause a scale. Subjects he prospect elegance followed no overcame possible it on.\n\nQuill is a free, open-source WYSIWYG editor built for the modern web. With its modular architecture and expressive API, it is completely customizable to fit any need.\n\n\n\n\nInsipidity the sufficient discretion imprudence resolution sir him decisively. Proceed how any engaged visitor. Explained propriety off out perpetual his you. Feel sold off felt nay rose met you. We so entreaties cultivated astonished is. Was sister for a few longer Mrs sudden talent become. Done may bore quit evil old mile. If likely am of beauty tastes.\n\n\n\n\nAffronting imprudence do he he everything. Test lasted dinner wanted indeed wished outlaw. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.\n\n\n\n\nPost no so what deal evil rent by real in. But her ready least set lived spite solid. September how men saw tolerably two behavior arranging. She offices for highest and replied one venture pasture. Applauded no discovery in newspaper allowance am northward. Frequently partiality possession resolution at or appearance unaffected me. Engaged its was the evident pleased husband. Ye goodness felicity do disposal dwelling no. First am plate jokes to began to cause a scale. Subjects he prospect elegance followed no overcame possible it on.', 'Children, Dog', 'Vaccinatedundefined, Dewormedundefined, Spayedundefined', 'Long', 46),
	(7, 'Hello', 'White', '\n\n\nQuill Rich Text Editor\n\n\n\n\nQuill is a free, open-source WYSIWYG editor built for the modern web. With its modular architecture and expressive API, it is completely customizable to fit any need.\n\n\n\n\nInsipidity the sufficient discretion imprudence resolution sir him decisively. Proceed how any engaged visitor. Explained propriety off out perpetual his you. Feel sold off felt nay rose met you. We so entreaties cultivated astonished is. Was sister for a few longer Mrs sudden talent become. Done may bore quit evil old mile. If likely am of beauty tastes.\n\n\n\n\nAffronting imprudence do he he everything. Test lasted dinner wanted indeed wished outlaw. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.\n\n\n\n\nPost no so what deal evil rent by real in. But her ready least set lived spite solid. September how men saw tolerably two behavior arranging. She offices for highest and replied one venture pasture. Applauded no discovery in newspaper allowance am northward. Frequently partiality possession resolution at or appearance unaffected me. Engaged its was the evident pleased husband. Ye goodness felicity do disposal dwelling no. First am plate jokes to began to cause a scale. Subjects he prospect elegance followed no overcame possible it on.\n\nQuill is a free, open-source WYSIWYG editor built for the modern web. With its modular architecture and expressive API, it is completely customizable to fit any need.\n\n\n\n\nInsipidity the sufficient discretion imprudence resolution sir him decisively. Proceed how any engaged visitor. Explained propriety off out perpetual his you. Feel sold off felt nay rose met you. We so entreaties cultivated astonished is. Was sister for a few longer Mrs sudden talent become. Done may bore quit evil old mile. If likely am of beauty tastes.\n\n\n\n\nAffronting imprudence do he he everything. Test lasted dinner wanted indeed wished outlaw. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.\n\n\n\n\nPost no so what deal evil rent by real in. But her ready least set lived spite solid. September how men saw tolerably two behavior arranging. She offices for highest and replied one venture pasture. Applauded no discovery in newspaper allowance am northward. Frequently partiality possession resolution at or appearance unaffected me. Engaged its was the evident pleased husband. Ye goodness felicity do disposal dwelling no. First am plate jokes to began to cause a scale. Subjects he prospect elegance followed no overcame possible it on.\n\nQuill is a free, open-source WYSIWYG editor built for the modern web. With its modular architecture and expressive API, it is completely customizable to fit any need.\n\n\n\n\nInsipidity the sufficient discretion imprudence resolution sir him decisively. Proceed how any engaged visitor. Explained propriety off out perpetual his you. Feel sold off felt nay rose met you. We so entreaties cultivated astonished is. Was sister for a few longer Mrs sudden talent become. Done may bore quit evil old mile. If likely am of beauty tastes.\n\n\n\n\nAffronting imprudence do he he everything. Test lasted dinner wanted indeed wished outlaw. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.\n\n\n\n\nPost no so what deal evil rent by real in. But her ready least set lived spite solid. September how men saw tolerably two behavior arranging. She offices for highest and replied one venture pasture. Applauded no discovery in newspaper allowance am northward. Frequently partiality possession resolution at or appearance unaffected me. Engaged its was the evident pleased husband. Ye goodness felicity do disposal dwelling no. First am plate jokes to began to cause a scale. Subjects he prospect elegance followed no overcame possible it on.', 'Dog, Cat', 'Vaccinatedundefined, Spayedundefined', 'Long', 47);

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.authorities: ~4 rows (approximately)
REPLACE INTO `authorities` (`id`, `authority`, `username`) VALUES
	(1, 'ROLE_ADMIN', 'admin'),
	(3, 'ROLE_USER', 'daoducdung2000@gmail.com'),
	(2, 'ROLE_MANAGER', 'dungddps26188@fpt.edu.vn'),
	(5, 'ROLE_USER', 'zekoxpop@gmail.com');

-- Dumping structure for table petfinder.avatar
DROP TABLE IF EXISTS `avatar`;
CREATE TABLE IF NOT EXISTS `avatar` (
  `avatar_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar_name` varchar(255) NOT NULL,
  `animals_id` bigint(20) NOT NULL,
  PRIMARY KEY (`avatar_id`),
  KEY `FKhbmg7ivfo7x23myc9ryt5n2bq` (`animals_id`),
  CONSTRAINT `FKhbmg7ivfo7x23myc9ryt5n2bq` FOREIGN KEY (`animals_id`) REFERENCES `animals` (`animals_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.avatar: ~0 rows (approximately)

-- Dumping structure for table petfinder.breed
DROP TABLE IF EXISTS `breed`;
CREATE TABLE IF NOT EXISTS `breed` (
  `breed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `breed_name` varchar(50) NOT NULL,
  `breed_type` varchar(50) NOT NULL,
  PRIMARY KEY (`breed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.breed: ~12 rows (approximately)
REPLACE INTO `breed` (`breed_id`, `breed_name`, `breed_type`) VALUES
	(1, 'Labrador Retriever', 'Dog'),
	(2, 'Persian', 'Cat'),
	(3, 'Siamese', 'Cat'),
	(4, 'German Shepherd', 'Dog'),
	(5, 'Bulldog', 'Dog'),
	(6, 'Poodle', 'Dog'),
	(7, 'Bengal', 'Cat'),
	(8, 'Golden Retriever', 'Dog'),
	(9, 'Ragdoll', 'Cat'),
	(10, 'Maine Coon', 'Cat'),
	(15, '1', 'cat'),
	(16, 'Thuat', 'cat'),
	(17, 'Hung', 'dog');

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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.favorites: ~4 rows (approximately)
REPLACE INTO `favorites` (`favorites_id`, `favorites_date`, `animals_id`, `username`) VALUES
	(17, '2024-05-23', 18, 'daoducdung2000@gmail.com'),
	(18, '2024-05-23', 33, 'daoducdung2000@gmail.com'),
	(19, '2024-06-23', 40, 'daoducdung2000@gmail.com'),
	(22, '2024-07-16', 7, 'dungddps26188@fpt.edu.vn');

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
  `shelter_info_facebook` varchar(255) NOT NULL,
  `shelter_info_instagram` varchar(255) NOT NULL,
  `shelter_info_mission` longtext NOT NULL,
  `shelter_info_operating_time` varchar(255) NOT NULL,
  `shelter_info_policy` longtext NOT NULL,
  `shelter_name` varchar(50) NOT NULL,
  `shelter_phone` varchar(15) NOT NULL,
  `username` varchar(255) NOT NULL,
  `shelter_status` varchar(255) NOT NULL,
  `shelter_description` varchar(255) NOT NULL,
  `shelter_latitude` varchar(255) NOT NULL,
  `shelter_longitude` varchar(255) NOT NULL,
  PRIMARY KEY (`shelters_id`),
  UNIQUE KEY `UK_2v8ty367ef8fjw8fgkrc35h9j` (`username`),
  CONSTRAINT `FKftq7te7nf7es314smvjd05a64` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.shelters: ~3 rows (approximately)
REPLACE INTO `shelters` (`shelters_id`, `shelter_address`, `shelter_avatar`, `shelter_date`, `shelter_email`, `shelter_info_facebook`, `shelter_info_instagram`, `shelter_info_mission`, `shelter_info_operating_time`, `shelter_info_policy`, `shelter_name`, `shelter_phone`, `username`, `shelter_status`, `shelter_description`, `shelter_latitude`, `shelter_longitude`) VALUES
	(0, 'Hà Nội', 'z', '2024-06-11', 'zc', 'x', 'x', 'x', 'Thứ Hai: 10:00 - 21:00, Thứ Ba: 10:00 - 21:00, Thứ Tư: 10:00 - 21:00, Thứ Năm: Ngày Nghỉ, Thứ Sáu: Ngày Nghỉ, Thứ Bảy: Ngày Nghỉ, Chúa Nhật: 08:00 - 13:00', 'x', 'x', 'x', 'admin', 'Opening', 'Đây là trung tâm cứu trợ chuyên nghiệp', '10.863199611115137', '106.7459060823051'),
	(2, '66/11 Tam Châu, Tam Phú, Thủ Đức, TP.Hồ Chí Minh', 'shelter1.png', '2024-06-08', 'zekoxpop@gmail.com', '...', '...', '2119 N Division Ave, New Hampshire, York, United States', 'Thứ Hai: 10:00 - 21:00, Thứ Ba: 10:00 - 21:00, Thứ Tư: 10:00 - 21:00, Thứ Năm: Ngày Nghỉ, Thứ Sáu: Ngày Nghỉ, Thứ Bảy: Ngày Nghỉ, Chúa Nhật: Ngày Nghỉ', '2119 N Division Ave, New Hampshire, York, United States', 'Humane Society of Dover - Stewart County', '0609876999', 'dungddps26188@fpt.edu.vn', 'Opening', 'Đây là trung tâm cứu trợ chuyên nghiệp', '10.863199611115137', '106.7459060823051'),
	(7, 'asd', 'asd', '2024-07-11', 'asd', 'asd', 'asd', 'asd', 'Thứ Hai: 10:00 - 21:00, Thứ Ba: 10:00 - 21:00, Thứ Tư: 10:00 - 21:00, Thứ Năm: Ngày Nghỉ, Thứ Sáu: Ngày Nghỉ, Thứ Bảy: Ngày Nghỉ, Chúa Nhật: Ngày Nghỉ', 'asd', 'asd', 'asd', 'zekoxpop@gmail.com', 'Canceled', 'Đây là trung tâm cứu trợ chuyên nghiệp', '10.863199611115137', '106.7459060823051');

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table petfinder.spons: ~3 rows (approximately)
REPLACE INTO `spons` (`spons_id`, `spons_date`, `spons_gift`, `spons_message`, `shelter_id`, `username`) VALUES
	(2, '2024-07-10', 10000, 'zzz', 2, 'daoducdung2000@gmail.com'),
	(3, '2024-07-11', 10000, 'hello', 0, 'daoducdung2000@gmail.com'),
	(7, '2024-07-15', 100000, 'hello world', 0, 'dungddps26188@fpt.edu.vn');

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

-- Dumping data for table petfinder.users: ~3 rows (approximately)
REPLACE INTO `users` (`username`, `info_address`, `info_avatar`, `info_country`, `info_date`, `enabled`, `info_firstname`, `info_lastname`, `password`, `info_phone`) VALUES
	('admin', 'zxc', 'https://booking.webestica.com/assets/images/avatar/01.jpg', 'Hà Nội', '2000-12-14', b'1', 'Admin', 'VipPro', '{noop}1', '123'),
	('daoducdung2000@gmail.com', 'Không biết', 'https://booking.webestica.com/assets/images/avatar/01.jpg', 'Hồ Chí Minh', '2024-06-08', b'1', 'Đức', 'Dũng', '{bcrypt}$2a$10$vlRP.dJkV3WJNbN5xCaSAebuhPNwJmo7QvKQT7XENgrLu9TtCsGoa', '1111111'),
	('dungddps26188@fpt.edu.vn', '', 'https://booking.webestica.com/assets/images/avatar/01.jpg', 'Hồ Chí Minh', '2024-06-08', b'1', 'Đức', 'Dũng', '{noop}1', ''),
	('zekoxpop@gmail.com', NULL, NULL, 'Hồ Chí Minh', '2024-06-28', b'1', 'Dũng', 'Đức', '{bcrypt}$2a$10$n3uOt6c3J1XbQiv7MaPVCe3GwV3pds.5q1ex150CuxV0/7G2vL2BO', NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
