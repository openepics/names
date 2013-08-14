--
-- Domain: Naming System
-- Description: Script to upgrade the database schema from v2.1 to v2.4.0
-- Date: 7 Aug 2013
-- Author: Vasu V

ALTER TABLE `privilege` DROP COLUMN `timestamp` , ADD COLUMN `version` INT NOT NULL  AFTER `operation` ;
ALTER TABLE `name_release` ADD COLUMN `released_by` VARCHAR(64) NOT NULL  AFTER `release_date` ;

--

UPDATE configuration SET value = '2.4.0' WHERE name = 'version';
COMMIT;
