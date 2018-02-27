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
INSERT INTO `administrator` VALUES (6,0,NULL,'1997-01-08','administrator@acme.com','Administrator',NULL,'1',5);
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
INSERT INTO `internationalization` VALUES (7,0,'es','term.condition','INFORMACIÓN RELEVANTE  Es requisito necesario para la adquisición de los productos que  se ofrecen en este sitio, que lea y acepte los siguientes Términos y  Condiciones que a continuación se redactan. El uso de nuestros  servicios así como la compra de nuestros productos implicará que usted  ha leído y aceptado los Términos y Condiciones de Uso en el presente  documento. Todas los productos que son ofrecidos por nuestro  sitio web pudieran ser creadas, cobradas, enviadas o presentadas por  una página web tercera y en tal caso estarían sujetas a sus propios  Términos y Condiciones. En algunos casos, para adquirir un producto,  será necesario el registro por parte del usuario, con ingreso de datos  personales fidedignos y definicián de una contraseña. El usuario puede elegir y cambiar la clave para su acceso de  administracián de la cuenta en cualquier momento, en caso de que se  haya registrado y que sea necesario para la compra de alguno de  nuestros productos. www.acme.com no asume la responsabilidad en caso de  que entregue dicha clave a terceros. Todas las compras y transacciones que se lleven a cabo por medio  de este sitio web, están sujetas a un proceso de confirmacián y  verificacián, el cual podría incluir la verificacián del stock y  disponibilidad de producto, validacián de la forma de pago, validacián  de la factura (en caso de existir) y el cumplimiento de las condiciones  requeridas por el medio de pago seleccionado. En algunos casos puede  que se requiera una verificacián por medio de correo electránico. Los precios de los productos ofrecidos en esta Tienda Online es  válido solamente en las compras realizadas en este sitio web.     INFORMACIÓN PERSONAL    Un administrador puede marcar como eliminado una cita si considera que tiene contenido inadecuado. Además, puede borrar un comentario si considera que afecta moralmente a otros usuarios.  Si cualquier usuario quiere borrar algún dato personal y la aplicación no le ofrece la posibilidad de hacerlo, debe ponerse en contacto con aisscreamacas@gmail.com   LICENCIA  Acme-Rendezvous a través de su sitio web concede una  licencia para que los usuarios utilicen los productos que son  vendidos en este sitio web de acuerdo a los Términos y Condiciones que  se describen en este documento.   USO NO AUTORIZADO  En caso de que aplique (para venta de software, templetes, u otro  producto de diseño y programacián) usted no puede colocar uno de  nuestros productos, modificado o sin modificar, en un CD, sitio web o  ningún otro medio y ofrecerlos para la redistribucián o la reventa de  ningún tipo.   PROPIEDAD  Usted no puede declarar propiedad intelectual o exclusiva a  ninguno de nuestros productos, modificado o sin modificar. Todos los  productos son propiedad de los proveedores del contenido. En caso  de que no se especifique lo contrario, nuestros productos se  proporcionan sin ningún tipo de garantía, expresa o implícita. En  ningún esta compañía será responsables de ningún daño incluyendo,  pero no limitado a, daños directos, indirectos, especiales, fortuitos o  consecuentes u otras pérdidas resultantes del uso o de la imposibilidad  de utilizar nuestros productos.   POLíTICA DE REEMBOLSO Y GARANTíA  En el caso de productos que sean mercancías irrevocables  no-tangibles, no realizamos reembolsos después de que se envíe el  producto, usted tiene la responsabilidad de entender antes de  comprarlo. Le pedimos que lea cuidadosamente antes de comprarlo.  Hacemos solamente excepciones con esta regla cuando la descripcián no  se ajusta al producto. Hay algunos productos que pudieran tener  garantía y posibilidad de reembolso pero este será especificado al  comprar el producto. En tales casos la garantía solo cubrirá fallas de  fábrica y sólo se hará efectiva cuando el producto se haya usado  correctamente. La garantía no cubre averías o daños ocasionados por uso  indebido. Los términos de la garantía están asociados a fallas de  fabricación y funcionamiento en condiciones normales de los productos y  sólo se harán efectivos estos términos si el equipo ha sido usado  correctamente. Esto incluye:   - De acuerdo a las especificaciones técnicas indicadas para cada producto.  - En condiciones ambientales acorde con las especificaciones indicadas por el fabricante.  - En uso específico para la función con que fue diseñado de fábrica.  - En condiciones de operación eléctricas acorde con las especificaciones y tolerancias indicadas.    COMPROBACIÓN ANTIFRAUDE  La compra del cliente puede ser aplazada para la comprobación  antifraude. También puede ser suspendida por más tiempo para una  investigación más rigurosa, para evitar transacciones fraudulentas.   PRIVACIDAD  Este sitio web www.acme.com garantiza que la información personal que usted envía  cuenta con la seguridad necesaria. Los datos ingresados por usuario o  en el caso de requerir una validación de los pedidos no serán  entregados a terceros, salvo que deba ser revelada en cumplimiento a  una orden judicial o requerimientos legales.  La suscripción a boletines de correos electrónicos publicitarios  es voluntaria y podría ser seleccionada al momento de crear su cuenta. Acme-Rendezvous reserva los derechos de cambiar o de modificar  estos términos sin previo aviso.    LEGISLACIÓN APLICABLE El Aviso Legal y las Condiciones Generales de Uso se rigen por las siguientes normas:  La Ley 34/2002, de 11 de julio, de Servicios de la Sociedad de la Información y de Comercio Electrónico (LSSI-CE) (particularmente, su artículo 10). La Ley Orgánica 15/1999, de 13 de diciembre, de Protección de Datos de Carácter Personal (LOPD). El Real Decreto 1720/2007, de 21 de diciembre, por el que se aprueba el Reglamento de desarrollo de la Ley Orgánica 15/1999, de 13 de diciembre, de Protección de Datos de Carácter Personal.'),(8,0,'en','term.condition','RELEVANT INFORMATION    It is a necessary requirement for the acquisition of products that are offered on this site, that you read and accept the following Terms and Conditions that are written below. The use of our services as well as the purchase of our products will mean that you You have read and accepted the Terms and Conditions of Use in this document. All products that are offered by our website could be created, collected, sent or presented by a third web page and in that case they would be subject to their own Terms and Conditions. In some cases, to acquire a product, registration by the user will be necessary, with data entry reliable personal and definition of a password.   The user can choose and change the password for their access administration of the account at any time, should it be have registered and that is necessary for the purchase of any of our products. www.acme.com does not assume responsibility in case of that deliver this key to third parties.   All purchases and transactions carried out by means of of this website, are subject to a confirmation process and verification, which could include verification of the stock and Product availability, validation of the payment method, validation of the invoice (if any) and compliance with the conditions required by the selected means of payment. In some cases you can that verification by email is required.   The prices of the products offered in this Online Store is valid only in purchases made on this website.    PERSONAL INFORMATION     An administrator can mark an appointment as deleted if he/she considers it has inappropriate content. In addition, he/she can delete a comment if think that it affects other users morally. If any user wants to delete some personal data and the application does not have the possibility to do that, you should contact with aisscreamacas@gmail.com   LICENSE    Acme-Rendezvous  through its website grants a license for users to use  the products that are sold on this website according to the Terms and Conditions that are described in this document.    UNAUTHORIZED USE    If applicable (for sale of software, templates, or other product design and programming) you can not place one of our products, modified or unmodified, on a CD, website or no other means and offer them for the redistribution or resale of no type.    PROPERTY    You can not declare intellectual or exclusive property to none of our products, modified or unmodified. All the Products are property  of the content providers. In case unless otherwise specified, our products are provide  without any warranty, express or implied. In No this company will be responsible for any damages including, but not limited to, direct, indirect, special, incidental or Consequences or other losses resulting from the use or impossibility to use our products.    REIMBURSEMENT AND GUARANTEE POLICY    In the case of products that are  irrevocable merchandise non-tangible, we do not make refunds after the product, you have the responsibility to understand before buy.  We ask you to read carefully before buying it. We only make exceptions with this rule when the description does not It fits the product. There are some products that could have guarantee and possibility of reimbursement but this will be specified at buy the product In such cases the guarantee will only cover failures of factory and will only be effective when the product has been used correctly. The guarantee does not cover faults or damages caused by use improper. The terms of the guarantee are associated with failures of manufacture and operation under normal conditions of the products and These terms will only be effective if the equipment has been used correctly. This includes:   - According to the technical specifications indicated for each product.   - In environmental conditions in accordance with the specifications indicated by the manufacturer.   - In use specific to the function with which it was designed from the factory.  - In electrical operating conditions in accordance with the specifications and tolerances indicated.    ANTI-FRAUD VERIFICATION    The customer\'s purchase can be postponed for checking anti fraud. It can also be suspended for a longer time for a more rigorous investigation, to avoid fraudulent transactions.    PRIVACY   This site web www.acme.com guarantees that the personal information you send has the necessary security. The data entered by user or In the case of requiring a validation of the orders, they will not be delivered to third parties, unless it must be disclosed in compliance with an order     APPLICABLE LEGISLATION        The  Legal Notice and the General Conditions of Use  are governed by the following rules:   The Law 34/2002, of 11 July, on Services of the Society of Information and Electronic Commerce (LSSI-CE)(particularly, article 10). The Organic Law 15/1999, of December 13, Protection of Personal Data (LOPD). The Royal Decree 1720/2007, of December 21, which approves the Regulation of development of the Organic Law 15/1999, of December 13, Protection of Personal Data.');
/*!40000 ALTER TABLE `internationalization` ENABLE KEYS */;
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
INSERT INTO `useraccount` VALUES (5,0,'21232f297a57a5a743894a0e4a801fc3','admin');
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
INSERT INTO `useraccount_authorities` VALUES (5,'ADMIN');
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

-- Dump completed on 2018-02-27 10:56:05
commit;