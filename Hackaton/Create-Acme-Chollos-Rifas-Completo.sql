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
INSERT INTO `answer` VALUES (12063,0,10,'No.',12029),(12064,0,21,'No.',12030),(12065,0,33,'Vegano.',12031),(12066,0,16,'Normal.',12031),(12067,0,47,'Sí, pero no recuerdo.',12029),(12068,0,33,'No, que yo sepa.',12030),(12069,0,31,'Sí, a las amapolas.',12029),(12070,0,25,'No, ninguna.',12030),(12071,0,56,'Sí, por favor.',12032),(12072,0,19,'Sí, mucho.',12033),(12073,0,24,'Los macarrones.',12034),(12074,0,10,'Porque quiero precios bajos.',12035),(12075,0,11,'El cocodrilo.',12036),(12076,0,19,'1,80 metros.',12037),(12077,0,25,'De Sevilla.',12038),(12080,0,14,'Ninguno.',12078),(12081,0,18,'No.',12079);
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
INSERT INTO `bargain` VALUES (12013,0,'Últimas promociones','FL6g5ty234',50,'',100,300,200,'PlayStation 4','https://www.game.es/videojuegos/playstation-4',11903),(12014,0,'Último modelo.','0LjshY61920',10,'',100,300,200,'XBOX','https://www.xbox.com/es-ES',11903),(12015,0,'Consigue ahora este magnifico portátil a un precio regalado.',NULL,5,'',100,450,400,'MSI','https://www.pccomponentes.com/msi-gl62m-7rdx-2203xes-intel-core-i5-7300hq-8gb-1tb-gtx-1050-156?gclid=EAIaIQobChMI24yIqv-R2wIVgcCyCh0-dwhbEAQYASABEgIW2PD_BwE',11903),(12016,0,'Consigue ahora este Ford Mondeo pagando lo mínimo.',NULL,25,'',1000,2000,2000,'Ford Mondeo','https://www.ford.es/turismos/mondeo',11903),(12017,0,'Escápate un fin de semana y vive una experiencia inolvidable.','883ddu2JSJ22',2,'',0,30,20,'Dos entradas Disneyland Paris','http://www.disneylandparis.es/',11903),(12018,0,'Vive la próxima temporada al mejor precio.','IUEncd7822',5,'',30,30,30,'Abono Sevilla FC','http://sevillafc.es/',11903),(12019,0,'Lo mejor para estar por casa.','Isdofos09PP822',50,'',0,15,15,'Batamanta','http://batamanta.es/',11903),(12020,0,'Disfruta del smartphone que siempre quisiste tener.','IUEncdspf7PP822',20,'',50,100,95,'Samsung S9','http://www.samsung.com/es/smartphones/galaxy-s9/shop/',11903),(12021,0,'Disfruta del viaje que más te guste','dsj7PP687T',100,'',50,100,100,'Viajes baratos','https://www.skyscanner.es/',11903),(12022,0,'Ya pronto llegan estas fechas, no lo dejes escapar...','9876yui87T',25,'',0,5,2,'Cesta de Navidad','https://www.lotesycestasdenavidad.es/',11903),(12023,0,'Renueva tu televisor',NULL,25,'\0',0,50,50,'Televisores','https://www.pccomponentes.com/televisores',11903),(12024,0,'Renueva tu teléfono',NULL,20,'\0',0,10,9,'Teléfonos','https://www.phonehouse.es/moviles/moviles-libres.html',11904);
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
INSERT INTO `bargain_productimages` VALUES (12013,'https://http2.mlstatic.com/D_Q_NP_389521-MLA20811128213_072016-Q.jpg'),(12013,'https://http2.mlstatic.com/rosario-playstation-4-sony-ps4-500gb-play4-garantia-nuevas-D_NQ_NP_18094-MLA20149500299_082014-F.jpg'),(12014,'https://compass-ssl.xbox.com/assets/3a/f0/3af0da7b-f5d0-418f-88ab-8ae631aafe9d.jpg?n=X1-Wireless-Controller-White_gallery_1056x594_01.jpg'),(12014,'https://compass-ssl.xbox.com/assets/e7/d4/e7d46aea-90d0-4bf1-9544-cc85d59f6d18.jpg?n=Xbox-Family_Home-Hero-0_FY-18-X1X-Lead_1067x667_02.jpg'),(12015,'https://images.anandtech.com/doci/12307/carouselgt75vrtitproklr1550_678x452.jpg'),(12015,'https://www.profesionalreview.com/wp-content/uploads/2018/01/MSI-GE63-73-Raider-RGB.png'),(12016,'https://media3.ocu.org//images/BF6ED83321B467BC3F27AA737670783F6669F373/h300-c4/ic0900854460000.jpg'),(12016,'https://d1eip2zddc2vdv.cloudfront.net/dphotos/750x400/13869-1443635803.jpg'),(12017,'http://weloversize.com/wp-content/uploads/2018/03/Disney-header.jpg'),(12017,'http://cdn4.dlp-media.com/resize/mwImage/1/640/360/75/wdpromedia.disney.go.com/media/wdpro-dlp-assets/prod/es-es/system/images/n013051_2019may13_menagerie-du-royaume_16-9.jpg'),(12018,'http://e00-marca.uecdn.es/assets/multimedia/imagenes/2018/02/08/15180917158846.jpg'),(12018,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuZzky7Xbg_On-8oJHA8ljr_Rm0zw-NHM5gai0KxOmFZgQOAbM'),(12019,'http://www.lavanguardia.com/r/GODO/LV/p4/WebSite/2017/02/20/Recortada/img_jsilvestre_20170220-194423_imagenes_lv_terceros_batamanta_dos-469-kZ2D-U42182488553QHE-992x558@LaVanguardia-Web.png'),(12019,'http://www.lavanguardia.com/r/GODO/LV/p4/WebSite/2017/02/20/img_jsilvestre_20170220-194423_imagenes_lv_terceros_batamanta_regalopedia.jpg'),(12020,'http://www.directd.com.my/content/images/thumbs/0020896_samsung-galaxy-s9-ready-stock-freebies-worth-rm200.png'),(12021,'http://pic.vpackage.net/viaje_a_Zanzibar/image-003_24900.jpg'),(12021,'https://www.felicesvacaciones.es/img/2016/02/26/fv_dubai_mauricio-05a.jpg'),(12022,'https://www.sadival.com/189-thickbox_default/cesta-de-navidad-65.jpg'),(12022,'https://www.shbarcelona.es/blog/es/wp-content/uploads/2015/11/34ooe49.png');
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
INSERT INTO `category_bargain` VALUES (11994,12016),(11994,12017),(11994,12018),(11994,12021),(11995,12013),(11995,12014),(11995,12015),(11995,12020),(11995,12023),(11995,12024),(11996,12015),(11997,12013),(11997,12014),(11998,12020),(11998,12024),(11999,12019),(11999,12022),(12000,12022);
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
INSERT INTO `comment` VALUES (12040,0,'2018-02-01 15:30:00','Buen producto!',12014,NULL,11857),(12041,0,'2018-02-16 12:43:00','¿Cuál es la página del chollo loco?',12013,NULL,11857),(12042,0,'2018-02-16 15:21:00','Es tuchollito.es',12013,12041,11855),(12043,0,'2018-02-01 15:30:00','Ha sido muy divertido',12015,NULL,11855),(12044,0,'2018-02-02 15:30:00','Ya falta menos',12016,NULL,11855),(12045,0,'2018-02-03 15:30:00','Gracias por apuntarse',12016,NULL,11855),(12046,0,'2018-02-04 15:30:00','Bienvenidos a todos',12023,NULL,11856),(12047,0,'2018-02-05 15:30:00','Que buen día hace hoy',12024,NULL,11857),(12048,0,'2018-02-06 15:30:00','Qué ganas que llegue el día',12013,NULL,11855),(12049,0,'2018-02-16 15:21:00','Muchas gracias nano!',12013,12042,11857);
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
INSERT INTO `comment_images` VALUES (12042,'https://vignette.wikia.nocookie.net/simpsons/images/c/ca/742_Evergreen_Terrace.png/revision/latest/scale-to-width-down/640?cb=20130421182211'),(12042,'https://falsa.com');
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
INSERT INTO `company` VALUES (11903,0,'Calle El Atributo, 123','company1@acme.com','20063918X','company1','+34618396001','García Noriega',11845,'Working4Enjoyment','SL'),(11904,0,'Calle Hojaldre, 4606','company2@acme.com','20123918Y','company2','+34618392011','Del Alba Martín',11846,'Sukafe','SL'),(11905,0,'Calle De La Pizza, 13','company3@acme.com','20123918Y','company3','+34621338011','Ruiz López',11847,'AISS Cream','COOPERATIVA'),(11906,0,'Calle Castillo, 01','company4@acme.com','20123918Z','company4','+34691238598','De la Fuente',11848,'Delafu and Sons','SL'),(11907,0,'Calle Castillo, 01','company4@acme.com','20123918Z','company5','+34691238598','Romero García',11849,'El Bar de los Amigos','SL'),(11908,0,'Calle Plaza, 99','company6@acme.com','20112318H','company6','+34622456112','Clemente De la Fuente',11850,'El Rincón de Antonio','SL');
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
INSERT INTO `configuration` VALUES (11866,0,'https://tinyurl.com/yabcto3u','http://localhost:8080/Acme-Chollos-Rifas/images/avatar.jpg','http://localhost:8080/Acme-Chollos-Rifas/images/link_broken.png','aisscreamacas@gmail.com','welcome.message','Buy different');
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
INSERT INTO `creditcard` VALUES (11891,0,'MasterCard',998,9,19,'Antonio','5471664286416252',11855),(11892,0,'Visa',258,10,18,'Paco','5229783098148518',11856),(11893,0,'MasterCard',618,10,18,'Antonio','5306320664868959',11855),(11894,0,'MasterCard',608,11,19,'David','4350515568299247',11857),(11895,0,'MasterCard',618,10,19,'Paco','4367834676464317',11858),(11896,0,'Visa',147,2,20,'Alejandro','345035739479236',11858),(11897,0,'MasterCard',522,10,20,'Antonio','5378931146110399',11855),(11898,0,'Visa',145,5,19,'Antonio','4532252568451427',11855),(11899,0,'Visa',145,5,16,'Antonio','4532252568451427',11855),(11900,0,'Visa',145,5,17,'Antonio','4532252568451427',11855),(11901,0,'Visa',147,5,20,'Francisco','4024007187090518',11859),(11902,0,'Visa',148,5,17,'Francisca','4556484350052417',11859);
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
INSERT INTO `evaluation` VALUES (11964,0,'Buen producto','',5,11903,11855),(11965,0,'No me gustó la empresa','\0',1,11904,11856),(11966,0,'Regu regu','',4,11905,11856),(11967,0,'Ni bien ni mal','',3,11904,11857),(11968,0,'So good','',5,11906,11858),(11969,0,'Excelente servicio','\0',1,11907,11859),(11970,0,'Regular la verdad','',4,11907,11860),(11971,0,'Regulin Regulag','\0',3,11908,11860);
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
INSERT INTO `groupon` VALUES (11869,0,'Televisión a un precio reducido si conseguimos las suficientes personas interesadas','345180318AAAAA','2018-09-15 15:00:00',10,210,180,'Televisión','https://tiendas.mediamarkt.es/p/tv-led-28-lg-28mt49s-pz-hd-smart-tv-wifi-1360736','Conjunta1',11855),(11870,0,'Ordenador a un precio excelente y solo depende de ustedes',NULL,'2018-10-15 16:00:00',30,800,650,'Ordenador','https://tiendas.mediamarkt.es/p/pc-gaming-microstar-p443-pcc452-gtx-1070-1364996','Conjunta2',11855),(11871,0,'Teléfono movil a un gran precio','331120218AAAAA','2018-03-20 16:00:00',15,200,150,'SmartPhone','https://tiendas.mediamarkt.es/p/movil-xiaomi-redmi-note-4-5.5-full-hd-3gb-1383690','Conjunta3',11855),(11872,0,'Videoconsola de Sony rebajada si se consigue el apoyo de todos',NULL,'2018-04-20 17:00:00',40,400,250,'Playstation','https://www.game.es/hardware/consola/playstation-4/playstation-4-pro-1tb-blanca-chassis-b/149705','Conjunta4',11855),(11873,0,'Conjunta para conseguir una radio a un mejor precio',NULL,'2018-10-20 17:00:00',30,46,20,'Radio','https://tiendas.mediamarkt.es/p/radio-cd-daewoo-dbu-50-rosa-puerto-usb-1228477','Conjunta5',11855),(11874,0,'Aspiradora para el hogar a un gran precio, depende de ti',NULL,'2018-10-21 17:00:00',1,29,10,'Aspiradora','https://tiendas.mediamarkt.es/p/aspirador-escoba-cecotec-duo-stick-easy-1383667','Conjunta6',11855),(11875,0,'Vuelve a lo clásico con este MP4 rebajado si se consigue un buen apoyo',NULL,'2018-11-21 17:00:00',35,79,59,'MP4','https://tiendas.mediamarkt.es/p/reproductor-mp4-sony-walkman-nwze394r-1312621','Conjunta7',11856);
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
INSERT INTO `moderator` VALUES (11889,0,'Calle El Río, 2','moderator1@acme.com','20063918-Y','Mario','+34602245841','Macías Reyes',11843),(11890,0,'Calle García de la Jara, 35','moderator2@acme.com','00234559-M','José María','+34752354214','López Menacho',11844);
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
INSERT INTO `notification` VALUES (11983,0,'¡Enhorabuena, usted ha sido el ganador de la rifa: Jamón del bueno para tu casa: jamón ibérico!',NULL,'\0',11855),(11984,0,'¡Enhorabuena, usted ha sido el ganador de la rifa: Rifa benéfica y llévate una PS4!',NULL,'\0',11855),(11985,0,'¡Enhorabuena, usted ha sido el ganador de la rifa: Tarjeta regalo de Amazon!',NULL,'',11855),(11986,0,'¡Enhorabuena, usted ha sido el ganador de la rifa: Entrada para ver el betis o el sevilla!',NULL,'',11855),(11987,0,'¡Enhorabuena, usted ha sido el ganador de la rifa: Pantalla ultrawide por la patilla!',NULL,'',11855),(11988,0,'¡Enhorabuena, usted ha sido el ganador de la rifa: LG SmartTV, la envidia de tu cuñado!',NULL,'',11855),(11989,0,'¡Enhorabuena, usted ha sido el ganador de la rifa: Video cámara FullHD 4K de Sony!',NULL,'',11856);
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
INSERT INTO `participation` VALUES (11876,0,3,11869,11856),(11877,0,2,11869,11857),(11878,0,4,11869,11858),(11879,0,3,11869,11859),(11880,0,1,11869,11860),(11881,0,7,11871,11856),(11882,0,7,11871,11857),(11883,0,4,11871,11858),(11884,0,4,11872,11856),(11885,0,4,11872,11858),(11886,0,2,11870,11856),(11887,0,2,11873,11856),(11888,0,2,11874,11856);
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
INSERT INTO `question` VALUES (12029,0,1,'¿Tienes alergia a algo?',11960),(12030,0,2,'¿Sufres alguna enfermedad cardiaca?',11960),(12031,0,1,'¿Menú especial?',11961),(12032,0,1,'¿Prefieres que haya menos anuncios?',11962),(12033,0,1,'¿Te gusta la electrónica?',11963),(12034,0,2,'¿Cuál es tu comida favorita?',11963),(12035,0,3,'¿Por qué quieres comprar con nosotros?',11963),(12036,0,4,'¿Cuál es tu animal favorito?',11963),(12037,0,5,'¿Cuánto mides?',11963),(12038,0,6,'¿De donde eres?',11963),(12078,0,1,'¿Tienes alguna problema en que te haga el chequeo alguien del sexo opuesto?',12039),(12079,0,2,'¿En el anterior chequeo se descubrió algun problema?',12039);
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
INSERT INTO `raffle` VALUES (11909,0,'En este caso traemos desde nuestras oficinas en Sevilla una rifa bien bien solidaria. Del importe total de tiques que se vendan, el 50% de los beneficios irán destindo a un colegio en Togo.','2018-05-10',1,'PlayStation 4',NULL,'Rifa benéfica y llévate una PS4',11903,11855),(11910,0,'Sí sí sí! ya está aquí el nuevo sorteo que llevamos un tiempo preparando. En este caso se trata de un estupendo lavavajillas de última generación marca Samsung valorado en 750€.','2018-08-30',2,'Samsung In Wash','https://www.samsung.com/us/home-appliances/washers/','Sorteamos un lavavajillas',11903,NULL),(11911,0,'Si te gustan las emociones y las sorpresas esta es tu oportunidad... realizamos desde W4E un sorteo sorpresa. ¿Qué será? Uhhh, solo te podemos adelantar que su precio está entre los 300 y 350 euros. Anímate!','2018-12-15',1,'Regalo Sorpresa',NULL,'Sorteo sorpresa',11903,NULL),(11912,0,'Pequeño sorteo para el próximo mes de mayo por parte de nosotros, de Sukafe, tu empresa de confianza. En este caso vamos a sortear una tarjeta regalo de 50€ para gastar en Amazon, así de sumple. ¡Participa ya!','2018-05-04',1,'Tarjeta regalo de Amazon','https://www.amazon.es/Tarjeta-Regalo-Amazon-es-correo-postal/dp/B007M8RJ3E','Tarjeta regalo de Amazon',11904,11855),(11913,0,'Lo tiramos todo por la ventana, ¡¡nos hemos vuelto locos!! Para celebrar nuestros 10 años en pie, en AISS Cream hemos querido sorprenderemos con este lujoso sorteo de un apartamento en Torrevieja, Alicante.','2019-09-09',20,'Apartamento en Torrevieja',NULL,'Sorteo de un piso en Torrevieja, Alicante',11905,NULL),(11914,0,'Estas navidades no dejes a tu familia sin una buena paletilla de jamón der gueno. Participa en este sorteo que seguro que no te decepcionará. Valorado en 240 euros!!','2017-12-15',5,'Paletilla de jamón ibérico','https://tinyurl.com/y8pl74ek','Jamón del bueno para tu casa: jamón ibérico',11903,11855),(11915,0,'Sorteamos una entrada para ver a uno de los dos gloriosos equipos de la capital hispalense. ¡Tu eliges quién quieres ver!','2018-03-10',1,'Entradas fútbol',NULL,'Entrada para ver el betis o el sevilla',11906,11855),(11916,0,'Valorada en 230 euros te traemos una pantalla de 27 pulgadas ultrawide que te hará la boca agua. Una delicia. Compra ya!','2018-04-25',1,'LG 25UM58-P','https://www.amazon.es/LG-25UM58-P-Monitor-UltraWide-pulgadas/dp/B01AWG4S4K','Pantalla ultrawide por la patilla',11903,11855),(11917,0,'Gloria bendita esta televisión que te trae la compañía de referencia de la gente que sabe desarrollar bien, W4E. 52 pulgadas de pura ultra mega high definition.','2018-01-06',2,'LG 43 pulgadas 43LJ594V','https://tinyurl.com/ybb5e77c','LG SmartTV, la envidia de tu cuñado',11903,11855),(11918,0,'Graba tooodo lo que quieras con esta cámara de última generación que te traemos en un sorteo TOTALMENTE GRATUITO.','2017-12-11',0,'Video cámara Sony',NULL,'Video cámara FullHD 4K de Sony',11904,11856),(11919,0,'Una cartera de últimas calidades cocida por los mejores tejedores de Murcia ofrecida por la marca Billabong de forma totalmente GRATUITA.','2017-12-11',0,'Cartera Billabong',NULL,'Cartera Billabong',11905,NULL),(11920,0,'Sukafe te trae el móvil de moda, el móvil que lo está petando en todo el mercado, por solo un euro! Unete a esta locuuuuura.','2018-10-10',1,'Xiaomi Redmi Note 4 64GB',NULL,'Xiaomi Redmi Note 4, el móvil de moda',11904,NULL),(11921,0,'Una maqueta no solo da diversión sin fin sino que será un bonito tema de conversación para las visitas cuando pregunten por esa cosa que tienes en la estantería. Compra ya un tique, es gratis ;)','2018-12-12',0,'Maqueta de avión de la 1º Guerra Mundial',NULL,'Maqueta de avión única!',11905,NULL),(11922,0,'El anillo único fue forjado por Saurom en las calderas del Monte del Destino y entregado a los elfos, pero ahora... TE ELIJE A TI! Compra un tique para esta apasionante rifa en la Tierra Media.','2020-01-01',1,'Anillo único',NULL,'El anillo único de Saurom',11905,NULL);
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
INSERT INTO `raffle_productimages` VALUES (11909,'https://psmedia.playstation.com/is/image/psmedia/ps4-overview-screen-03-eu-06sep16'),(11909,'https://media.redadn.es/imagenes/playstation4_314481.jpg'),(11910,'http://img.global.news.samsung.com/global/wp-content/uploads/2015/09/Addwash_2.jpg'),(11910,'https://img.us.news.samsung.com/us/wp-content/uploads/2016/12/31205307/Image1-Flex-Wash-Flex-Dry-1.jpg'),(11911,'http://carolscaribbeancorner.com/wp-content/uploads/2013/03/Mysterygift11.jpg'),(11912,'https://www.peru-retail.com/wp-content/uploads/170915-amazon-mn-1135_c850ab65307569ac0a1ddaa9ef16187c.jpg'),(11912,'https://images-eu.ssl-images-amazon.com/images/G/30/gc/designs/livepreview/a_generic_10_v2016_es_noto_email_v2016_es-main._CB525592981_.png'),(11913,'http://www.turisbox.com/lugares/wp-content/uploads/2016/05/El-Acequion-Torrevieja.jpg'),(11913,'http://www.inmorealty.es/wp-content/uploads/2017/02/piso-venta-alicante-torrevieja-inmorealty-3011-5.jpg'),(11913,'http://www.diarioinformacion.com/noticias-hoy/img/piso-alicante-1.jpg'),(11913,'http://www.inmorealty.es/wp-content/uploads/2015/11/apartamento-en-venta-vista-bella-torrevieja-alicante-inmorealty-4.jpg'),(11913,'http://www.inmorealty.es/wp-content/uploads/2017/02/piso-venta-alicante-torrevieja-inmorealty-3011-7.jpg'),(11914,'https://images-na.ssl-images-amazon.com/images/I/91cLKkOqIIL._SL1500_.jpg'),(11914,'https://images-na.ssl-images-amazon.com/images/I/714aLdXMoeL._SL1500_.jpg'),(11915,'https://www.tenvinilo.com/vinilos-decorativos/img/preview/vinilo-decorativo-escudo-real-betis-7478.png'),(11915,'http://sevilla.abc.es/deportes/orgullodenervion/wp-content/uploads/2017/10/Escudo_sevillafc.jpg'),(11916,'https://images-na.ssl-images-amazon.com/images/I/31MSkky6Y-L._SX466_.jpg'),(11917,'https://images-na.ssl-images-amazon.com/images/I/81QOsztPuLL._SL1500_.jpg'),(11922,'http://de10.com.mx/sites/default/files/styles/detalle_nota/public/field/image/destacada_senior-anuillos.jpg'),(11922,'http://static.alfabetajuega.com/abj_public_files/multimedia/imagenes/201502/98514.alfabetajuega-anillo-unico-020215.jpg'),(11922,'https://supercurioso.com/wp-content/uploads/2014/10/elanillo-unico.jpg');
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
INSERT INTO `sponsor` VALUES (11990,0,'Calle Del Rocio, 134','sponsor1@acme.com','20063918-Y','sponsor1','+34603162355','De Montón',11851),(11991,0,'Calle Sicab, 87','sponsor2@acme.com','35512291A','sponsor2','+34602245841','Gyllenhal',11852),(11992,0,'Calle El Pintor 23','sponsor1@acme.com','31251361C','sponsor3','+34602245841','Eguibar',11853),(11993,0,'Calle Robledo, 12','sponsor1@acme.com','50063918-Z','sponsor4','+34602245841','Ripoll',11854);
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
INSERT INTO `sponsorship` VALUES (12050,0,20,'http://www.cocacolaespana.es/content/dam/journey/es/es/private/historia/love-coca-cola/2015/logo-coca-cola-lead.png','https://www.cocacola.es/es/home/',12013,11990),(12051,0,30,'https://i0.wp.com/creatividadenblanco.com/wp-content/uploads/2017/05/fanta_2017_logo_colors.png','https://www.cocacola.es/fanta/es/home/',12013,11991),(12052,0,25,'http://www.brandemia.org/sites/default/files/inline/images/logo_nestea_antes_0.jpg','https://www.cocacola.es/nestea/es/home/',12013,11992),(12053,0,25,'http://www.ripollfemenia.com/wp-content/uploads/2013/05/lanjaron-LOGO.jpg','https://www.lanjaron.com/',12013,11993),(12054,0,25,'http://s3-eu-west-1.amazonaws.com/pruebapd.esy.es/wp-content/uploads/2011/10/bic_logo1.png','https://www.bicworld.com/es',12014,11990),(12055,0,25,'https://dlcdnimgs.asus.com/20160129_cosmo/cosmo/images/asus_logo.jpg','https://www.asus.com/es/',12014,11991),(12056,0,50,'https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/HP_New_Logo_2D.svg/1200px-HP_New_Logo_2D.svg.png','http://www8.hp.com/es/es/home.html',12014,11992),(12057,0,40,'https://i2.wp.com/www.expertosenmarca.com/wp-content/uploads/2015/10/hd-wallpaper-ralph-lauren-logo.jpeg','https://www.ralphlauren.com/',12015,11990),(12058,0,10,'https://logosmarcas.com/wp-content/uploads/2018/03/Adidas-logo.png','https://www.agenciatelling.com/el-origen-del-logo-de-adidas/',12015,11991),(12059,0,10,'http://www.ferniplastmayorista.com/wp-content/uploads/2014/06/Nivea-logo.jpg','https://www.nivea.es/',12017,11990),(12060,0,3,'http://logonoid.com/images/gigaset-logo.png','http://www.gigaset.com/es_es/',12022,11990),(12061,0,5,'https://d2jljza7x0a5yy.cloudfront.net/media/k2/items/cache/897299971c45604fa1bdefb646b1c4db_XL.jpg?t=1458131205','https://www.factoriadeficcion.com/',12020,11990),(12062,0,1,'https://andro2id.com/wp-content/uploads/2018/02/logo-xiaomi-2.png','http://www.mi.com/es/',12024,11990);
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
INSERT INTO `subscription` VALUES (12025,0,'Monthly',11891,11867,11855),(12026,0,'Anually',11892,11868,11856),(12027,0,'Anually',11894,11868,11857),(12028,0,'Quarterly',11895,11867,11858);
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
INSERT INTO `survey` VALUES (11960,0,'La encuesta que te puede salvar la vida.',11903),(11961,0,'¿Cuánto de importante es para ti la comida?',11890),(11962,0,'Nos preocupamos por ti',11890),(11963,0,'¿Te gustaría mejoras?',11904),(12039,0,'¿Te sientes cómodo?',11990);
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
INSERT INTO `tag` VALUES (11972,0,'electronica'),(11973,0,'chollazo'),(11974,0,'favorito'),(11975,0,'navidad'),(11976,0,'coches'),(11977,0,'entradas'),(11978,0,'envidia'),(11979,0,'sé el primero'),(11980,0,'calentito'),(11981,0,'buen precio'),(11982,0,'viaja');
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
INSERT INTO `tag_bargain` VALUES (11972,12013),(11972,12014),(11972,12015),(11973,12016),(11973,12017),(11973,12018),(11973,12024),(11973,12019),(11973,12013),(11973,12014),(11974,12018),(11975,12022),(11976,12016),(11977,12017),(11977,12018),(11978,12018),(11978,12020),(11979,12013),(11979,12014),(11980,12019),(11981,12021),(11982,12021);
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
INSERT INTO `ticket` VALUES (11923,0,'14180315AAAAA','\0',11891,11909,11855),(11924,0,'14180315AAAAb','\0',11891,11909,11855),(11925,0,'14180315AAAAC','\0',11891,11909,11855),(11926,0,'14180315AAAAd','\0',11892,11909,11856),(11927,0,'14180315AAAAd','\0',11894,11909,11857),(11928,0,'14180315AAAAE','\0',11893,11910,11855),(11929,0,'14180315AAAAF','\0',11895,11910,11858),(11930,0,'14180315AAAAG','\0',11895,11910,11858),(11931,0,'14180315AAAAz','\0',11895,11910,11858),(11932,0,'14180315AAAAI','\0',11891,11912,11855),(11933,0,'14180315AAAAJ','\0',11891,11912,11855),(11934,0,'14180315AAAAK','\0',11891,11912,11855),(11935,0,'14180315AAAA8','\0',11891,11912,11855),(11936,0,'14180315AAAAM','\0',11894,11913,11857),(11937,0,'14180315AAAAN','\0',11894,11913,11857),(11938,0,'14180315AAAAk','\0',11891,11914,11855),(11939,0,'14180315AAAA3','\0',11892,11914,11856),(11940,0,'14180315AAAAQ','\0',11892,11914,11856),(11941,0,'14180315AAAAW','\0',11891,11915,11855),(11942,0,'14180315AAAAS','\0',11891,11915,11855),(11943,0,'14180315AAAAs','\0',11891,11916,11855),(11944,0,'14180315AAAAU','\0',11891,11917,11855),(11945,0,'14180315AAAAu','\0',11892,11918,11856),(11946,0,'14180315AAAAW','\0',11892,11919,11856),(11947,0,'14180315AAAAO','\0',11892,11919,11856),(11948,0,'14180315AAAAZ','\0',11894,11919,11857),(11949,0,'14180315AAAAa','\0',11894,11919,11857),(11950,0,'14180315AAAAB','\0',11894,11919,11857),(11951,0,'14180315AAAAc','\0',11894,11919,11857),(11952,0,'14180315AAAAD','\0',11895,11919,11857),(11953,0,'14180315AAAAe','\0',11895,11919,11858);
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
INSERT INTO `user` VALUES (11855,0,'Calle Sin Número, 123','user1@acme.com','20063918-Y','User1','+34618396001','Martínez ruiz',11837,'https://www.w3schools.com/howto/img_avatar.png','',620),(11856,0,'Calle Falsa, 123','user2@acme.com','20063918-Y','User2',NULL,'López González',11838,'https://www.sahanet.net/wp-content/uploads/2018/02/avatar-2-300x300.png','',50),(11857,0,NULL,'user3@acme.com','20063918-Y','User3','+3461738000','García Trinidad',11839,'http://partnertekst.dk/wp-content/uploads/avatar-7.png','\0',20),(11858,0,NULL,'user4@acme.com','20063918-Y','User4',NULL,'Sánchez Ortiz',11840,'http://partnertekst.dk/wp-content/uploads/avatar-7.png','\0',300),(11859,0,NULL,'user5@acme.com','20063918-Y','User5','+3461738010','Casillas Martín',11841,'http://partnertekst.dk/wp-content/uploads/avatar-7.png','\0',100),(11860,0,NULL,'user6@acme.com','20063918-Y','User6','+3461738011','Crespo Sánchez',11842,'http://partnertekst.dk/wp-content/uploads/avatar-7.png','\0',-100);
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
INSERT INTO `user_bargain` VALUES (11855,12013),(11855,12014),(11855,12015),(11855,12016),(11855,12017),(11855,12018),(11856,12013),(11856,12014),(11856,12015),(11857,12016),(11857,12017),(11857,12018),(11858,12017),(11858,12018),(11859,12016),(11859,12017),(11860,12017);
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
INSERT INTO `useraccount` VALUES (11836,0,'','21232f297a57a5a743894a0e4a801fc3','admin'),(11837,0,'','24c9e15e52afc47c225b757e7bee1f9d','user1'),(11838,0,'','7e58d63b60197ceb55a1c487989a3720','user2'),(11839,0,'','92877af70a45fd6a2ed7fe81e1236b78','user3'),(11840,0,'','3f02ebe3d7929b091e3d8ccfde2f3bc6','user4'),(11841,0,'','0a791842f52a0acfbb3a783378c066b8','user5'),(11842,0,'','affec3b64cf90492377a8114c86fc093','user6'),(11843,0,'','38caf4a470117125b995f7ce53e6e6b9','moderator1'),(11844,0,'','95d88ad73653fc7ad4fec3bc56677c3c','moderator2'),(11845,0,'','df655f976f7c9d3263815bd981225cd9','company1'),(11846,0,'','d196a28097115067fefd73d25b0c0be8','company2'),(11847,0,'','e828ae3339b8d80b3902c1564578804e','company3'),(11848,0,'','856dfd9045541fa727ef6ad392835eb0','company4'),(11849,0,'','efa5a30f11d85b93518ba6cc3f229b14','company5'),(11850,0,'','65ea58759487b11278c234e510c34817','company6'),(11851,0,'','42c63ad66d4dc07ed17753772bef96d6','sponsor1'),(11852,0,'','3dc67f80a03324e01b1640f45d107485','sponsor2'),(11853,0,'','857a54956061fdc1b88d7722cafe6519','sponsor3'),(11854,0,'','a8be034fe44a453e912feadc414dc803','sponsor4');
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
INSERT INTO `useraccount_authorities` VALUES (11836,'ADMIN'),(11837,'USER'),(11838,'USER'),(11839,'USER'),(11840,'USER'),(11841,'USER'),(11842,'USER'),(11843,'MODERATOR'),(11844,'MODERATOR'),(11845,'COMPANY'),(11846,'COMPANY'),(11847,'COMPANY'),(11848,'COMPANY'),(11849,'COMPANY'),(11850,'COMPANY'),(11851,'SPONSOR'),(11852,'SPONSOR'),(11853,'SPONSOR'),(11854,'SPONSOR');
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