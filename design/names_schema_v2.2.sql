SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `discs_names2` DEFAULT CHARACTER SET latin1 ;
USE `discs_names2` ;

-- -----------------------------------------------------
-- Table `configuration`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `configuration` ;

CREATE  TABLE IF NOT EXISTS `configuration` (
  `name` VARCHAR(64) NOT NULL ,
  `value` VARCHAR(128) NULL DEFAULT NULL ,
  PRIMARY KEY (`name`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COMMENT = 'Schema configuration information';


-- -----------------------------------------------------
-- Table `privilege`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `privilege` ;

CREATE  TABLE IF NOT EXISTS `privilege` (
  `userid` VARCHAR(64) NOT NULL ,
  `operation` VARCHAR(1) NOT NULL ,
  `timestamp` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`userid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `name_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `name_category` ;

CREATE  TABLE IF NOT EXISTS `name_category` (
  `id` VARCHAR(16) NOT NULL ,
  `name` VARCHAR(32) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `version` INT NOT NULL DEFAULT 0 COMMENT 'For optimistic concurrency control. Do not rename/delete.' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'category of a name: system, subsystem, device type etc';


-- -----------------------------------------------------
-- Table `name_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `name_event` ;

CREATE  TABLE IF NOT EXISTS `name_event` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Event id' ,
  `name_id` VARCHAR(64) NULL COMMENT 'system-generated identifier (uuid) of a name. can be null (non-approved new names)' ,
  `event_type` CHAR NOT NULL DEFAULT 'i' COMMENT 'i - initiation, m - modification, d - deletion' ,
  `requested_by` VARCHAR(64) NOT NULL ,
  `requestor_comment` VARCHAR(255) NULL ,
  `request_date` DATETIME NOT NULL ,
  `status` CHAR NOT NULL DEFAULT 'p' COMMENT 'a - approved,p - being processed,  r - rejected, c - cancelled' ,
  `processed_by` VARCHAR(64) NULL ,
  `processor_comment` VARCHAR(255) NULL ,
  `process_date` DATETIME NULL COMMENT 'Date the request got processed.' ,
  `name_category_id` VARCHAR(16) NOT NULL ,
  `name_code` VARCHAR(16) NOT NULL COMMENT 'new code' ,
  `name_description` VARCHAR(255) NOT NULL COMMENT 'new description' ,
  `version` INT NOT NULL DEFAULT 0 COMMENT 'For optimistic concurrency control.' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_name_event_name_category` (`name_category_id` ASC) ,
  CONSTRAINT `fk_name_event_name_category`
    FOREIGN KEY (`name_category_id` )
    REFERENCES `name_category` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'An event in the lifecycle of a name';


-- -----------------------------------------------------
-- Table `name_release`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `name_release` ;

CREATE  TABLE IF NOT EXISTS `name_release` (
  `id` VARCHAR(16) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `doc_url` VARCHAR(255) NULL ,
  `release_date` DATETIME NULL ,
  `version` INT NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'Naming system version/revision';



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `configuration`
-- -----------------------------------------------------
START TRANSACTION;
USE `discs_names2`;
INSERT INTO `configuration` (`name`, `value`) VALUES ('version', '2.2');

COMMIT;
