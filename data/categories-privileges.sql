-- 


INSERT INTO `configuration` VALUES ('version','2.3');

--
--  data for table `name_category`
--


INSERT INTO `name_category` VALUES 
        ('device-type','Device-Type','Device-Type',0),
        ('signal-domain','Signal Domain','Signal Domain',0),
        ('signal-suffix','Signal Suffix','Signal Suffix',0),
        ('signal-type','Signal Type','Signal Type',0),
        ('subsystem','Subsystem','Subsystem',0),
        ('system','System','System',0);


--
--  data for table `privilege`
--
--  Change the first column to the user's user id


INSERT INTO `privilege` VALUES 
       ('paul','E','0'),
       ('john','E','0'),
       ('mary','E','0');


-- Dump completed on 2013-05-22 17:40:38
