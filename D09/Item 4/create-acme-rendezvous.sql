start transaction;

create database `Acme-Rendezvous`;

use `Acme-Rendezvous`;

create user 'acme-user'@'%' identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';

create user 'acme-manager'@'%' identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';


grant select, insert, update, delete 
	on `Acme-Rendezvous`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-Rendezvous`.* to 'acme-manager'@'%';

-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Rendezvous
-- ------------------------------------------------------
-- Server version	5.5.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_idt4b4u259p6vs4pyr9lax4eg` (`userAccount_id`),
  CONSTRAINT `FK_idt4b4u259p6vs4pyr9lax4eg` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (10,0,NULL,'1997-01-08','administrator@acme.com','Administrator',NULL,'1',9);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `announcement`
--

DROP TABLE IF EXISTS `announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `announcement` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `moment` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `rendezvous_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_ft302xb1b7mw4x0ilisk99iba` (`moment`),
  KEY `FK_ff49x8lk2mmqi9noe8xwqg0ah` (`rendezvous_id`),
  CONSTRAINT `FK_ff49x8lk2mmqi9noe8xwqg0ah` FOREIGN KEY (`rendezvous_id`) REFERENCES `rendezvous` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announcement`
--

LOCK TABLES `announcement` WRITE;
/*!40000 ALTER TABLE `announcement` DISABLE KEYS */;
/*!40000 ALTER TABLE `announcement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answer` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `question_id` int(11) NOT NULL,
  `rsvp_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kdq2rkpr9nv0clogujwmsphap` (`rsvp_id`,`question_id`),
  KEY `FK_10g8xii7lw9kq0kcsobgmtw72` (`question_id`),
  CONSTRAINT `FK_91ncee5xlnh457hxbnru0qkjx` FOREIGN KEY (`rsvp_id`) REFERENCES `rsvp` (`id`),
  CONSTRAINT `FK_10g8xii7lw9kq0kcsobgmtw72` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `defaultCategory` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `fatherCategory_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_eay5cd0x7r2uc5j5k14mysxru` (`defaultCategory`),
  KEY `FK_8dbpkngc2chtdg1xbd67fu6s0` (`fatherCategory_id`),
  CONSTRAINT `FK_8dbpkngc2chtdg1xbd67fu6s0` FOREIGN KEY (`fatherCategory_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (16,0,'','Esta es una categoría por defecto. Puedes eliminarla si asocias más categorías a tu servicio.','Categoría por defecto',NULL);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `moment` datetime DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `rendezvous_id` int(11) NOT NULL,
  `repliedComment_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_cub35s8xr5ip2yamdkqky5dxf` (`rendezvous_id`),
  KEY `FK_ox7ful6sr9xw6y8afljmapbbj` (`repliedComment_id`),
  KEY `FK_jhvt6d9ap8gxv67ftrmshdfhj` (`user_id`),
  CONSTRAINT `FK_jhvt6d9ap8gxv67ftrmshdfhj` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_cub35s8xr5ip2yamdkqky5dxf` FOREIGN KEY (`rendezvous_id`) REFERENCES `rendezvous` (`id`),
  CONSTRAINT `FK_ox7ful6sr9xw6y8afljmapbbj` FOREIGN KEY (`repliedComment_id`) REFERENCES `comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuration`
--

DROP TABLE IF EXISTS `configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `welcomeMessage` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration`
--

LOCK TABLES `configuration` WRITE;
/*!40000 ALTER TABLE `configuration` DISABLE KEYS */;
INSERT INTO `configuration` VALUES (15,0,'https://tinyurl.com/adventure-meetup','Adventure meetups','welcomeMessage');
/*!40000 ALTER TABLE `configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creditcard`
--

DROP TABLE IF EXISTS `creditcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creditcard` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `brandName` varchar(255) DEFAULT NULL,
  `cvvcode` int(11) NOT NULL,
  `expirationMonth` int(11) NOT NULL,
  `expirationYear` int(11) NOT NULL,
  `holderName` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2h62gb07aah6rtc8hgu3jgm94` (`user_id`),
  CONSTRAINT `FK_2h62gb07aah6rtc8hgu3jgm94` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditcard`
--

LOCK TABLES `creditcard` WRITE;
/*!40000 ALTER TABLE `creditcard` DISABLE KEYS */;
/*!40000 ALTER TABLE `creditcard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('DomainEntity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `internationalization`
--

DROP TABLE IF EXISTS `internationalization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `internationalization` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `countryCode` varchar(255) DEFAULT NULL,
  `messageCode` varchar(255) DEFAULT NULL,
  `value` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c7moguj2ub0wulf545v3l2ydy` (`countryCode`,`messageCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `internationalization`
--

LOCK TABLES `internationalization` WRITE;
/*!40000 ALTER TABLE `internationalization` DISABLE KEYS */;
INSERT INTO `internationalization` VALUES (11,0,'es','term.condition','INFORMACIÓN RELEVANTE  Es requisito necesario para el uso de la web que lea y acepte los siguientes Términos y  Condiciones que a continuación se redactan. El uso de nuestros  servicios implicará que usted  ha leído y aceptado los Términos y Condiciones de Uso en el presente  documento.     INFORMACIÓN PERSONAL    Un administrador puede marcar como eliminado una cita o cancelar un servicio si considera que tiene contenido inadecuado. Además, puede borrar un comentario o anuncio si considera que afecta moralmente a otros usuarios.  Si cualquier usuario quiere borrar algún dato personal y la aplicación no le ofrece la posibilidad de hacerlo, debe ponerse en contacto con aisscreamacas@gmail.com    COMPROBACIÓN ANTIFRAUDE  La compra del usuario puede ser aplazada para la comprobación  antifraude. También puede ser suspendida por más tiempo para una  investigación más rigurosa, para evitar transacciones fraudulentas.   PRIVACIDAD  Este sitio web www.acme.com garantiza que la información personal que usted envía  cuenta con la seguridad necesaria. Los datos ingresados por usuario  no serán  entregados a terceros, salvo que deba ser revelada en cumplimiento a  una orden judicial o requerimientos legales.  Acme-Rendezvous reserva los derechos de cambiar o de modificar  estos términos sin previo aviso.    LEGISLACIÓN APLICABLE El Aviso Legal y las Condiciones Generales de Uso se rigen por las siguientes normas:  La Ley 34/2002, de 11 de julio, de Services de la Sociedad de la Información y de Comercio Electrónico (LSSI-CE) (particularmente, su artículo 10). La Ley Orgánica 15/1999, de 13 de diciembre, de Protección de Datos de Carácter Personal (LOPD). El Real Decreto 1720/2007, de 21 de diciembre, por el que se aprueba el Reglamento de desarrollo de la Ley Orgánica 15/1999, de 13 de diciembre, de Protección de Datos de Carácter Personal.'),(12,0,'en','term.condition','RELEVANT INFORMATION    It is a necessary requirement for the use of this web, that you read and accept the following Terms and Conditions that are written below. The use of our services will mean that you have read and accepted the Terms and Conditions of Use in this document.    PERSONAL INFORMATION     An administrator can mark an rendezvous or service as deleted if he/she considers it has inappropriate content. In addition, he/she can delete a comment or announcement if think that it affects other users morally. If any user wants to delete some personal data and the application does not have the possibility to do that, you should contact with aisscreamacas@gmail.com    ANTI-FRAUD VERIFICATION    The user\'s purchase can be postponed for checking anti fraud. It can also be suspended for a longer time for a more rigorous investigation, to avoid fraudulent transactions.    PRIVACY   This site web www.acme.com guarantees that the personal information you send has the necessary security. The data entered by user will not be delivered to third parties, unless it must be disclosed in compliance with an order  Acme-Rendezvous reserves the rights to change or modify these terms without prior notice.     APPLICABLE LEGISLATION        The  Legal Notice and the General Conditions of Use  are governed by the following rules:   The Law 34/2002, of 11 July, on Services of the Society of Information and Electronic Commerce (LSSI-CE)(particularly, article 10). The Organic Law 15/1999, of December 13, Protection of Personal Data (LOPD). The Royal Decree 1720/2007, of December 21, which approves the Regulation of development of the Organic Law 15/1999, of December 13, Protection of Personal Data.'),(13,0,'es','welcomeMessage','Tu sitio para organizar quedadas de aventura'),(14,0,'en','welcomeMessage','Your place to organise your adventure meetups!');
/*!40000 ALTER TABLE `internationalization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manager` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  `vat` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_84bmmxlq61tiaoc7dy7kdcghh` (`userAccount_id`),
  UNIQUE KEY `UK_8rfs4s1g2ky7ht8t1j34c9ant` (`vat`),
  CONSTRAINT `FK_84bmmxlq61tiaoc7dy7kdcghh` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `rendezvous_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_ggq8kq8ggqkrhtiqbow5t5xfm` (`number`),
  KEY `FK_ejk14ged2t2jaquwya6f97awi` (`rendezvous_id`),
  CONSTRAINT `FK_ejk14ged2t2jaquwya6f97awi` FOREIGN KEY (`rendezvous_id`) REFERENCES `rendezvous` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rendezvous`
--

DROP TABLE IF EXISTS `rendezvous`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rendezvous` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `adultOnly` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `draft` bit(1) NOT NULL,
  `isDeleted` bit(1) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `moment` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `creator_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_nciqoyne25kluegpes2sy2k6d` (`adultOnly`,`isDeleted`),
  KEY `FK_4cru16jpqbsxd0g6runtbwqlt` (`creator_id`),
  CONSTRAINT `FK_4cru16jpqbsxd0g6runtbwqlt` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rendezvous`
--

LOCK TABLES `rendezvous` WRITE;
/*!40000 ALTER TABLE `rendezvous` DISABLE KEYS */;
/*!40000 ALTER TABLE `rendezvous` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rendezvous_rendezvous`
--

DROP TABLE IF EXISTS `rendezvous_rendezvous`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rendezvous_rendezvous` (
  `Rendezvous_id` int(11) NOT NULL,
  `linkerRendezvouses_id` int(11) NOT NULL,
  KEY `FK_kxqlq8kpvccti40v2m5wqrcro` (`linkerRendezvouses_id`),
  KEY `FK_fuhs4y1oqtlba5sencebgmosa` (`Rendezvous_id`),
  CONSTRAINT `FK_fuhs4y1oqtlba5sencebgmosa` FOREIGN KEY (`Rendezvous_id`) REFERENCES `rendezvous` (`id`),
  CONSTRAINT `FK_kxqlq8kpvccti40v2m5wqrcro` FOREIGN KEY (`linkerRendezvouses_id`) REFERENCES `rendezvous` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rendezvous_rendezvous`
--

LOCK TABLES `rendezvous_rendezvous` WRITE;
/*!40000 ALTER TABLE `rendezvous_rendezvous` DISABLE KEYS */;
/*!40000 ALTER TABLE `rendezvous_rendezvous` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `creditCard_id` int(11) NOT NULL,
  `rendezvous_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_b3tqctbaua2ldjbffnyedms1n` (`service_id`,`rendezvous_id`),
  KEY `FK_fjur187j9ek17ad4iss31xyne` (`creditCard_id`),
  KEY `FK_fqncf826cbbt5fber93wnxxwd` (`rendezvous_id`),
  CONSTRAINT `FK_l1v2qq3n315obw8m24obhalup` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`),
  CONSTRAINT `FK_fjur187j9ek17ad4iss31xyne` FOREIGN KEY (`creditCard_id`) REFERENCES `creditcard` (`id`),
  CONSTRAINT `FK_fqncf826cbbt5fber93wnxxwd` FOREIGN KEY (`rendezvous_id`) REFERENCES `rendezvous` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rsvp`
--

DROP TABLE IF EXISTS `rsvp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rsvp` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `attendant_id` int(11) NOT NULL,
  `rendezvous_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_e5ygxcrv2919gek69rr9137cg` (`attendant_id`,`rendezvous_id`),
  KEY `FK_mhgbdanbchgcpfdgj5gfh7iy5` (`rendezvous_id`),
  CONSTRAINT `FK_mhgbdanbchgcpfdgj5gfh7iy5` FOREIGN KEY (`rendezvous_id`) REFERENCES `rendezvous` (`id`),
  CONSTRAINT `FK_9x6drqll5fyq8n636rhks7l4c` FOREIGN KEY (`attendant_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rsvp`
--

LOCK TABLES `rsvp` WRITE;
/*!40000 ALTER TABLE `rsvp` DISABLE KEYS */;
/*!40000 ALTER TABLE `rsvp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `manager_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_hx4i1ryxochp62biuhh9rarx2` (`status`),
  KEY `FK_87be48k3rjrg7rsthpl9t55wd` (`manager_id`),
  CONSTRAINT `FK_87be48k3rjrg7rsthpl9t55wd` FOREIGN KEY (`manager_id`) REFERENCES `manager` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_category`
--

DROP TABLE IF EXISTS `service_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_category` (
  `Service_id` int(11) NOT NULL,
  `categories_id` int(11) NOT NULL,
  KEY `FK_bvkul8qihuhdw6f87fgchurb1` (`categories_id`),
  KEY `FK_t89nu5acirn60yag5bacogxpv` (`Service_id`),
  CONSTRAINT `FK_t89nu5acirn60yag5bacogxpv` FOREIGN KEY (`Service_id`) REFERENCES `service` (`id`),
  CONSTRAINT `FK_bvkul8qihuhdw6f87fgchurb1` FOREIGN KEY (`categories_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_category`
--

LOCK TABLES `service_category` WRITE;
/*!40000 ALTER TABLE `service_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `service_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o6s94d43co03sx067ili5760c` (`userAccount_id`),
  CONSTRAINT `FK_o6s94d43co03sx067ili5760c` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_csivo9yqa08nrbkog71ycilh5` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount`
--

LOCK TABLES `useraccount` WRITE;
/*!40000 ALTER TABLE `useraccount` DISABLE KEYS */;
INSERT INTO `useraccount` VALUES (9,0,'21232f297a57a5a743894a0e4a801fc3','admin');
/*!40000 ALTER TABLE `useraccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount_authorities`
--

DROP TABLE IF EXISTS `useraccount_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount_authorities` (
  `UserAccount_id` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_b63ua47r0u1m7ccc9lte2ui4r` (`UserAccount_id`),
  CONSTRAINT `FK_b63ua47r0u1m7ccc9lte2ui4r` FOREIGN KEY (`UserAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount_authorities`
--

LOCK TABLES `useraccount_authorities` WRITE;
/*!40000 ALTER TABLE `useraccount_authorities` DISABLE KEYS */;
INSERT INTO `useraccount_authorities` VALUES (9,'ADMIN');
/*!40000 ALTER TABLE `useraccount_authorities` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-22 15:15:08
commit;