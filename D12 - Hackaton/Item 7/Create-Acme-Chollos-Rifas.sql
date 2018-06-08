start transaction;

create database `Acme-Chollos-Rifas`;

use `Acme-Chollos-Rifas`;

create user 'acme-user'@'%' identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';

create user 'acme-manager'@'%' identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';


grant select, insert, update, delete 
	on `Acme-Chollos-Rifas`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-Chollos-Rifas`.* to 'acme-manager'@'%';

-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Chollos-Rifas
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
  `email` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
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
INSERT INTO `administrator` VALUES (11861,0,NULL,'administrator@acme.com','20063918-Y','Administrator',NULL,'1',11836);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
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
  `counter` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `question_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_10g8xii7lw9kq0kcsobgmtw72` (`question_id`),
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
-- Table structure for table `bargain`
--

DROP TABLE IF EXISTS `bargain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bargain` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discountCode` varchar(255) DEFAULT NULL,
  `estimatedSells` int(11) NOT NULL,
  `isPublished` bit(1) NOT NULL,
  `minimumPrice` double NOT NULL,
  `originalPrice` double NOT NULL,
  `price` double NOT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `productUrl` varchar(255) DEFAULT NULL,
  `company_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_g67alkfftckxd9rkjt9a4n0as` (`isPublished`),
  KEY `FK_8p74s0am5l0jmjy08dalau7on` (`company_id`),
  CONSTRAINT `FK_8p74s0am5l0jmjy08dalau7on` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bargain`
--

LOCK TABLES `bargain` WRITE;
/*!40000 ALTER TABLE `bargain` DISABLE KEYS */;
/*!40000 ALTER TABLE `bargain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bargain_productimages`
--

DROP TABLE IF EXISTS `bargain_productimages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bargain_productimages` (
  `Bargain_id` int(11) NOT NULL,
  `productImages` varchar(255) DEFAULT NULL,
  KEY `FK_1v1e7fa1dl174kfudq45x0p8i` (`Bargain_id`),
  CONSTRAINT `FK_1v1e7fa1dl174kfudq45x0p8i` FOREIGN KEY (`Bargain_id`) REFERENCES `bargain` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bargain_productimages`
--

LOCK TABLES `bargain_productimages` WRITE;
/*!40000 ALTER TABLE `bargain_productimages` DISABLE KEYS */;
/*!40000 ALTER TABLE `bargain_productimages` ENABLE KEYS */;
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
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `fatherCategory_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_poxohwr34sms8rvvgb0ropwx6` (`defaultCategory`,`name`),
  KEY `FK_8dbpkngc2chtdg1xbd67fu6s0` (`fatherCategory_id`),
  CONSTRAINT `FK_8dbpkngc2chtdg1xbd67fu6s0` FOREIGN KEY (`fatherCategory_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (11994,0,'','http://www.finanzzas.com/wp-content/uploads/DefaultLogo.png','Esto es una categoría por defecto',NULL),(11995,0,'\0','http://ciinova.com/wp-content/uploads/2016/09/casa-central-ingenieria-civil-electronica.w700.jpg','Electrónica',NULL),(11996,0,'\0','https://lifeinformatica.com/wp-content/uploads/Life/Familias/family-portatiles-y-accesorios-01_300x300.jpg','Portátiles',11995),(11997,0,'\0','http://www.avproyectores.es/images/videoconsolas.png','Videoconsolas',11995),(11998,0,'\0','https://www.movilzona.es/app/uploads/2016/10/moviles-intex.jpg?x=720','Móviles',11995),(11999,0,'\0','https://www.verti.es/media/images/seguros-hogar.png','Hogar',NULL),(12000,0,'\0','http://www.leroymerlin.es/dam/jcr:5b9cbf1c-d045-44a6-84da-7dc67ad42097/cocina5.jpg','Cocina y comedor',11999),(12001,0,'\0','https://decoracion2.com/imagenes/2014/12/cuarto-de-bano-1.jpg','Baño',11999),(12002,0,'\0','https://revistacitylife.com/wp-content/uploads/2018/03/guia-basica-iluminacion.jpg','Iluminación',11999),(12003,0,'\0','http://sanitum.com/wp-content/uploads/2015/12/limpieza-y-desinfeccion-hogar-germenes-virus-bacterias-hongos-sanitum.jpg','Limpieza',11999),(12004,0,'\0','http://tinyur1.co/wp-content/uploads/2017/01/moda.jpg','Moda',NULL),(12005,0,'\0','https://i.ytimg.com/vi/HfgEQ7cmjq8/maxresdefault.jpg','Mujer',12004),(12006,0,'\0','http://abcrevista.com/wp-content/uploads2/2017/05/consejos-de-moda-para-hombres-jeans.jpg','Hombre',12004),(12007,0,'\0','https://i.pinimg.com/originals/6d/46/75/6d4675afabdf3d086d4e83d8617a9b43.jpg','Niña',12004),(12008,0,'\0','http://lamodaatualcance.com/wp-content/uploads/cheeky-coleccion-oton%CC%83o-invierno-2012-moda1.jpg','Niño',12004),(12009,0,'\0','http://www.blogmodabebe.com/wp-content/uploads/2016/08/nueva-coleccion-de-moda-bebe-de-pili-carrera-blogmodabebe-116.jpg','Bebé',12004),(12010,0,'\0','https://blogs.hogarmania.com/wp-content/uploads/2017/01/bolsos-personalizables-Justo.jpg','Bolsos',12004),(12011,0,'\0','https://i.ytimg.com/vi/RI0v8-3XmiY/maxresdefault.jpg','Joyería',12004),(12012,0,'\0','https://images-na.ssl-images-amazon.com/images/I/81PAdiIjRzL._UX569_.jpg','Relojes',12004);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_bargain`
--

DROP TABLE IF EXISTS `category_bargain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_bargain` (
  `Category_id` int(11) NOT NULL,
  `bargains_id` int(11) NOT NULL,
  KEY `FK_68etj3wa2wk6nmwgmwxpkip5i` (`bargains_id`),
  KEY `FK_y5evkoqajt41hl20e53a4tlb` (`Category_id`),
  CONSTRAINT `FK_y5evkoqajt41hl20e53a4tlb` FOREIGN KEY (`Category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FK_68etj3wa2wk6nmwgmwxpkip5i` FOREIGN KEY (`bargains_id`) REFERENCES `bargain` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_bargain`
--

LOCK TABLES `category_bargain` WRITE;
/*!40000 ALTER TABLE `category_bargain` DISABLE KEYS */;
/*!40000 ALTER TABLE `category_bargain` ENABLE KEYS */;
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
  `text` varchar(255) DEFAULT NULL,
  `bargain_id` int(11) NOT NULL,
  `repliedComment_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ralt94hh9hxdhp4imdajns3s` (`bargain_id`),
  KEY `FK_ox7ful6sr9xw6y8afljmapbbj` (`repliedComment_id`),
  KEY `FK_jhvt6d9ap8gxv67ftrmshdfhj` (`user_id`),
  CONSTRAINT `FK_jhvt6d9ap8gxv67ftrmshdfhj` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_ox7ful6sr9xw6y8afljmapbbj` FOREIGN KEY (`repliedComment_id`) REFERENCES `comment` (`id`),
  CONSTRAINT `FK_ralt94hh9hxdhp4imdajns3s` FOREIGN KEY (`bargain_id`) REFERENCES `bargain` (`id`)
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
-- Table structure for table `comment_images`
--

DROP TABLE IF EXISTS `comment_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_images` (
  `Comment_id` int(11) NOT NULL,
  `images` varchar(255) DEFAULT NULL,
  KEY `FK_rixmy8yqk5oh1trb9vq8w44sg` (`Comment_id`),
  CONSTRAINT `FK_rixmy8yqk5oh1trb9vq8w44sg` FOREIGN KEY (`Comment_id`) REFERENCES `comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_images`
--

LOCK TABLES `comment_images` WRITE;
/*!40000 ALTER TABLE `comment_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  `companyName` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ryofib0fqdaxa17n2qeqqf6am` (`userAccount_id`),
  CONSTRAINT `FK_ryofib0fqdaxa17n2qeqqf6am` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
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
  `defaultAvatar` varchar(255) DEFAULT NULL,
  `defaultImage` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `slogan` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration`
--

LOCK TABLES `configuration` WRITE;
/*!40000 ALTER TABLE `configuration` DISABLE KEYS */;
INSERT INTO `configuration` VALUES (11866,0,'https://www.acme.com:8443/images/logo.jpg','https://www.acme.com:8443/images/avatar.jpg','https://www.acme.com:8443/images/link_broken.jpg','aisscreamacas@gmail.com','welcome.message','Buy different');
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
  KEY `UK_k0yy0i1pnw0d0cmkwtp1sdghk` (`expirationYear`,`expirationMonth`),
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
-- Table structure for table `evaluation`
--

DROP TABLE IF EXISTS `evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evaluation` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `isAnonymous` bit(1) DEFAULT NULL,
  `puntuation` int(11) DEFAULT NULL,
  `company_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_h4668mdrqjsuxfyq3feihr47f` (`company_id`),
  KEY `FK_7bh6so8gr4bye81iujv69p9qs` (`user_id`),
  CONSTRAINT `FK_7bh6so8gr4bye81iujv69p9qs` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_h4668mdrqjsuxfyq3feihr47f` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation`
--

LOCK TABLES `evaluation` WRITE;
/*!40000 ALTER TABLE `evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupon`
--

DROP TABLE IF EXISTS `groupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupon` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discountCode` varchar(255) DEFAULT NULL,
  `maxDate` datetime DEFAULT NULL,
  `minAmountProduct` int(11) NOT NULL,
  `originalPrice` double NOT NULL,
  `price` double NOT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `productUrl` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `creator_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_acuml9pul9dmgcjsaq6ph2hho` (`maxDate`),
  KEY `FK_pq2a02i8b0usqcn97qgkg5nfd` (`creator_id`),
  CONSTRAINT `FK_pq2a02i8b0usqcn97qgkg5nfd` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupon`
--

LOCK TABLES `groupon` WRITE;
/*!40000 ALTER TABLE `groupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupon` ENABLE KEYS */;
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
-- Table structure for table `identifier`
--

DROP TABLE IF EXISTS `identifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `identifier` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `firstCounter` int(11) NOT NULL,
  `fivethCounter` int(11) NOT NULL,
  `fourthCounter` int(11) NOT NULL,
  `secondCounter` int(11) NOT NULL,
  `thirdCounter` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `identifier`
--

LOCK TABLES `identifier` WRITE;
/*!40000 ALTER TABLE `identifier` DISABLE KEYS */;
INSERT INTO `identifier` VALUES (11954,0,32,0,0,0,0);
/*!40000 ALTER TABLE `identifier` ENABLE KEYS */;
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
INSERT INTO `internationalization` VALUES (11862,0,'es','term.condition','INFORMACIÓN RELEVANTE  Es requisito necesario para el uso de la web que lea y acepte los siguientes Términos y  Condiciones que a continuación se redactan. El uso de nuestros  servicios implicará que usted  ha leído y aceptado los Términos y Condiciones de Uso en el presente  documento.     INFORMACIÓN PERSONAL    Un moderador puede puede borrar un comentario, chollo... si considera que afecta moralmente a otros usuarios.  Si cualquier usuario quiere borrar algún dato personal y la aplicación no le ofrece la posibilidad de hacerlo, debe ponerse en contacto con aisscreamacas@gmail.com    COMPROBACIÓN ANTIFRAUDE  La compra del usuario puede ser aplazada para la comprobación  antifraude. También puede ser suspendida por más tiempo para una  investigación más rigurosa, para evitar transacciones fraudulentas.   PRIVACIDAD  Este sitio web www.acme.com garantiza que la información personal que usted envía  cuenta con la seguridad necesaria. Los datos ingresados por usuario  no serán  entregados a terceros, salvo que deba ser revelada en cumplimiento a  una orden judicial o requerimientos legales.  Acme Bargains and Raffles reserva los derechos de cambiar o de modificar  estos términos sin previo aviso.    LEGISLACIÓN APLICABLE El Aviso Legal y las Condiciones Generales de Uso se rigen por las siguientes normas:  La Ley 34/2002, de 11 de julio, de Services de la Sociedad de la Información y de Comercio Electrónico (LSSI-CE) (particularmente, su artículo 10). La Ley Orgánica 15/1999, de 13 de diciembre, de Protección de Datos de Carácter Personal (LOPD). El Real Decreto 1720/2007, de 21 de diciembre, por el que se aprueba el Reglamento de desarrollo de la Ley Orgánica 15/1999, de 13 de diciembre, de Protección de Datos de Carácter Personal.'),(11863,0,'en','term.condition','RELEVANT INFORMATION    It is a necessary requirement for the use of this web, that you read and accept the following Terms and Conditions that are written below. The use of our services will mean that you have read and accepted the Terms and Conditions of Use in this document.    PERSONAL INFORMATION     An moderator can delete a comment, bargain... if he/she think that it affects other users morally. If any user wants to delete some personal data and the application does not have the possibility to do that, you should contact with aisscreamacas@gmail.com    ANTI-FRAUD VERIFICATION    The user\'s purchase can be postponed for checking anti fraud. It can also be suspended for a longer time for a more rigorous investigation, to avoid fraudulent transactions.    PRIVACY   This site web www.acme.com guarantees that the personal information you send has the necessary security. The data entered by user will not be delivered to third parties, unless it must be disclosed in compliance with an order  Acme Chollos y Rifas reserves the rights to change or modify these terms without prior notice.     APPLICABLE LEGISLATION        The  Legal Notice and the General Conditions of Use  are governed by the following rules:   The Law 34/2002, of 11 July, on Services of the Society of Information and Electronic Commerce (LSSI-CE)(particularly, article 10). The Organic Law 15/1999, of December 13, Protection of Personal Data (LOPD). The Royal Decree 1720/2007, of December 21, which approves the Regulation of development of the Organic Law 15/1999, of December 13, Protection of Personal Data.'),(11864,0,'en','welcome.message','Acme Bargains and Raffles'),(11865,0,'es','welcome.message','Acme Chollos y Rifas');
/*!40000 ALTER TABLE `internationalization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level`
--

DROP TABLE IF EXISTS `level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `maxPoints` int(11) NOT NULL,
  `minPoints` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_c4o9pgu0ipk90yrhrt9dmocv1` (`minPoints`,`maxPoints`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` VALUES (11955,0,'https://tinyurl.com/yd7wmdzq',-1,-1000,'Criminal'),(11956,0,'https://tinyurl.com/yb3pwvsy',100,0,'Bronce'),(11957,0,'https://tinyurl.com/y7rxsfd9',500,101,'Plata'),(11958,0,'https://tinyurl.com/y8mbeq8a',999,501,'Oro'),(11959,0,'https://tinyurl.com/yal9k4gj',9999,1000,'Diamante');
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `moderator`
--

DROP TABLE IF EXISTS `moderator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moderator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dvoe3msx5ofgj5qm5tyj6hnm9` (`userAccount_id`),
  CONSTRAINT `FK_dvoe3msx5ofgj5qm5tyj6hnm9` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moderator`
--

LOCK TABLES `moderator` WRITE;
/*!40000 ALTER TABLE `moderator` DISABLE KEYS */;
/*!40000 ALTER TABLE `moderator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `visited` bit(1) NOT NULL,
  `actor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_c12g5kj8g6tvqxmwrsupahw0j` (`visited`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participation`
--

DROP TABLE IF EXISTS `participation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participation` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `amountProduct` int(11) NOT NULL,
  `groupon_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_h4lj1vymwkrlpqxlq5yfp3ktv` (`user_id`,`groupon_id`),
  KEY `FK_mp7f1htmglshu0bsvgbl5is87` (`groupon_id`),
  CONSTRAINT `FK_no7gdv9eusksfc5dcvju2il4w` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_mp7f1htmglshu0bsvgbl5is87` FOREIGN KEY (`groupon_id`) REFERENCES `groupon` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participation`
--

LOCK TABLES `participation` WRITE;
/*!40000 ALTER TABLE `participation` DISABLE KEYS */;
/*!40000 ALTER TABLE `participation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plan`
--

DROP TABLE IF EXISTS `plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plan` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `cost` double NOT NULL,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_ple4x3gxnbny0ny0p67u8lprb` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plan`
--

LOCK TABLES `plan` WRITE;
/*!40000 ALTER TABLE `plan` DISABLE KEYS */;
INSERT INTO `plan` VALUES (11867,0,5,'El plan Basic Premium contará con las siguientes ventajas:    Sus comentarios tendrán más relevancia que los del resto al mostrarse con un fondo color coral,   tendrán la opción de añadir imágenes en los comentarios,   obtendrán un 2.5% más de descuento en los chollos,   derecho a participar en una conjunta incluso tras ser cerrada.     ','Basic Premium'),(11868,0,10,'El plan Gold Premium cuenta con las siguientes ventajas adicionales:    El descuento en los chollos será mayor, de un 7.5%,   tendrá la opción de acceder los chollos desde el momento de la creación y no de su publicación al público general,   obtendrá automáticamente un tique de regalo para cada rifa.    ','Gold Premium');
/*!40000 ALTER TABLE `plan` ENABLE KEYS */;
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
  `number` int(11) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `survey_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_ggq8kq8ggqkrhtiqbow5t5xfm` (`number`),
  KEY `FK_4ge80eigofevs17w0sbdbiidh` (`survey_id`),
  CONSTRAINT `FK_4ge80eigofevs17w0sbdbiidh` FOREIGN KEY (`survey_id`) REFERENCES `survey` (`id`)
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
-- Table structure for table `raffle`
--

DROP TABLE IF EXISTS `raffle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `raffle` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `maxDate` date DEFAULT NULL,
  `price` double NOT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `productUrl` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `company_id` int(11) NOT NULL,
  `winner_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_l50likurqxvkyf1qjekabrfgo` (`maxDate`),
  KEY `FK_h9gex3u9gfsnlsadkjmnvtfnb` (`company_id`),
  KEY `FK_oks0sxhkjqot9gy2jwm8kc1rg` (`winner_id`),
  CONSTRAINT `FK_oks0sxhkjqot9gy2jwm8kc1rg` FOREIGN KEY (`winner_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_h9gex3u9gfsnlsadkjmnvtfnb` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `raffle`
--

LOCK TABLES `raffle` WRITE;
/*!40000 ALTER TABLE `raffle` DISABLE KEYS */;
/*!40000 ALTER TABLE `raffle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `raffle_productimages`
--

DROP TABLE IF EXISTS `raffle_productimages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `raffle_productimages` (
  `Raffle_id` int(11) NOT NULL,
  `productImages` varchar(255) DEFAULT NULL,
  KEY `FK_s7pwhqgpg1bets5eopdmyw11b` (`Raffle_id`),
  CONSTRAINT `FK_s7pwhqgpg1bets5eopdmyw11b` FOREIGN KEY (`Raffle_id`) REFERENCES `raffle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `raffle_productimages`
--

LOCK TABLES `raffle_productimages` WRITE;
/*!40000 ALTER TABLE `raffle_productimages` DISABLE KEYS */;
/*!40000 ALTER TABLE `raffle_productimages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sponsor`
--

DROP TABLE IF EXISTS `sponsor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsor` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_okfx8h0cn4eidh8ng563vowjc` (`userAccount_id`),
  CONSTRAINT `FK_okfx8h0cn4eidh8ng563vowjc` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sponsor`
--

LOCK TABLES `sponsor` WRITE;
/*!40000 ALTER TABLE `sponsor` DISABLE KEYS */;
/*!40000 ALTER TABLE `sponsor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sponsorship`
--

DROP TABLE IF EXISTS `sponsorship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsorship` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `amount` double NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `bargain_id` int(11) NOT NULL,
  `sponsor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2khcyvq53v4n0ekg5jb8ni3gg` (`sponsor_id`,`bargain_id`),
  KEY `UK_d9re045nfehvpcv2abk0cfn9y` (`amount`),
  KEY `FK_4wqe9t081qbltxrk4pu527atd` (`bargain_id`),
  CONSTRAINT `FK_e3idyfyffpufog3sjl3c2ulun` FOREIGN KEY (`sponsor_id`) REFERENCES `sponsor` (`id`),
  CONSTRAINT `FK_4wqe9t081qbltxrk4pu527atd` FOREIGN KEY (`bargain_id`) REFERENCES `bargain` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sponsorship`
--

LOCK TABLES `sponsorship` WRITE;
/*!40000 ALTER TABLE `sponsorship` DISABLE KEYS */;
/*!40000 ALTER TABLE `sponsorship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `payFrecuency` varchar(255) DEFAULT NULL,
  `creditCard_id` int(11) NOT NULL,
  `plan_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iwmhiweloa3dnrvfpdl47tf49` (`user_id`),
  UNIQUE KEY `UK_no5o2dcu5r28i17hdvj7o4n7s` (`user_id`,`plan_id`),
  KEY `FK_cak1wsbmm9pxfybdcvdnao3ga` (`creditCard_id`),
  KEY `FK_7miyirmgvn9utcq6fsy7w1s2f` (`plan_id`),
  CONSTRAINT `FK_iwmhiweloa3dnrvfpdl47tf49` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_7miyirmgvn9utcq6fsy7w1s2f` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`),
  CONSTRAINT `FK_cak1wsbmm9pxfybdcvdnao3ga` FOREIGN KEY (`creditCard_id`) REFERENCES `creditcard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription`
--

LOCK TABLES `subscription` WRITE;
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey`
--

DROP TABLE IF EXISTS `survey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `survey` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `surveyer_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey`
--

LOCK TABLES `survey` WRITE;
/*!40000 ALTER TABLE `survey` DISABLE KEYS */;
/*!40000 ALTER TABLE `survey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_24642shpebunaq3ggshotv9hk` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_bargain`
--

DROP TABLE IF EXISTS `tag_bargain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_bargain` (
  `Tag_id` int(11) NOT NULL,
  `bargains_id` int(11) NOT NULL,
  KEY `FK_tdei3wtnlustemoy919gs1lg4` (`bargains_id`),
  KEY `FK_odlruf58tn0ax3a1n4etnpea3` (`Tag_id`),
  CONSTRAINT `FK_odlruf58tn0ax3a1n4etnpea3` FOREIGN KEY (`Tag_id`) REFERENCES `tag` (`id`),
  CONSTRAINT `FK_tdei3wtnlustemoy919gs1lg4` FOREIGN KEY (`bargains_id`) REFERENCES `bargain` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_bargain`
--

LOCK TABLES `tag_bargain` WRITE;
/*!40000 ALTER TABLE `tag_bargain` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag_bargain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `isGift` bit(1) NOT NULL,
  `creditCard_id` int(11) DEFAULT NULL,
  `raffle_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_r73ikob0xvm5va7wrsxjc08o5` (`creditCard_id`),
  KEY `FK_17870yt5j0ex1qsf91uquo349` (`raffle_id`),
  KEY `FK_1nne0brl6rr604w9ng3to3rom` (`user_id`),
  CONSTRAINT `FK_1nne0brl6rr604w9ng3to3rom` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_17870yt5j0ex1qsf91uquo349` FOREIGN KEY (`raffle_id`) REFERENCES `raffle` (`id`),
  CONSTRAINT `FK_r73ikob0xvm5va7wrsxjc08o5` FOREIGN KEY (`creditCard_id`) REFERENCES `creditcard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
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
  `email` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `isPublicWishList` bit(1) NOT NULL,
  `points` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o6s94d43co03sx067ili5760c` (`userAccount_id`),
  KEY `UK_27pbbgtw6fb6o0bsam603bvek` (`points`),
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
-- Table structure for table `user_bargain`
--

DROP TABLE IF EXISTS `user_bargain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_bargain` (
  `User_id` int(11) NOT NULL,
  `wishList_id` int(11) NOT NULL,
  KEY `FK_m3917cs6ehsnsljvcnvlvou80` (`wishList_id`),
  KEY `FK_3dl4f9kpkpyhsw9h5orjv6yb1` (`User_id`),
  CONSTRAINT `FK_3dl4f9kpkpyhsw9h5orjv6yb1` FOREIGN KEY (`User_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_m3917cs6ehsnsljvcnvlvou80` FOREIGN KEY (`wishList_id`) REFERENCES `bargain` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_bargain`
--

LOCK TABLES `user_bargain` WRITE;
/*!40000 ALTER TABLE `user_bargain` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_bargain` ENABLE KEYS */;
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
  `enabled` bit(1) NOT NULL,
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
INSERT INTO `useraccount` VALUES (11836,0,'','21232f297a57a5a743894a0e4a801fc3','admin');
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
INSERT INTO `useraccount_authorities` VALUES (11836,'ADMIN');
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

-- Dump completed on 2018-05-29 21:24:15
commit;