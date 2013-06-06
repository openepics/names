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
-- Table `category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `category` ;

CREATE  TABLE IF NOT EXISTS `category` (
  `id` VARCHAR(16) NOT NULL ,
  `name` VARCHAR(32) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `version` INT NOT NULL DEFAULT 0 COMMENT 'For optimistic concurrency control. Do not rename/delete.' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'category of a name: system, subsystem, device type etc';


-- -----------------------------------------------------
-- Table `name`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `name` ;

CREATE  TABLE IF NOT EXISTS `name` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `code` VARCHAR(16) NOT NULL ,
  `category_id` VARCHAR(16) NOT NULL ,
  `description` VARCHAR(255) NOT NULL ,
  `comments` VARCHAR(255) NULL ,
  `is_deleted` TINYINT(1) NOT NULL DEFAULT false ,
  `version` INT NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_name_category` (`category_id` ASC) ,
  CONSTRAINT `fk_name_category`
    FOREIGN KEY (`category_id` )
    REFERENCES `category` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Name of a system, subsystem, device type, signal etc. Note t' /* comment truncated */;


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
-- Table `change_request`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `change_request` ;

CREATE  TABLE IF NOT EXISTS `change_request` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name_id` INT NULL COMMENT 'name_id can be null (for \'insert\' requests).' ,
  `operation` CHAR NOT NULL DEFAULT 'I' COMMENT 'i - insert, m - Modify, d - Delete' ,
  `requested_by` VARCHAR(64) NOT NULL ,
  `requestor_comment` VARCHAR(255) NULL ,
  `request_date` DATETIME NOT NULL ,
  `status` CHAR NOT NULL DEFAULT false COMMENT 'i - initiated, a - approved, r - rejected, c - cancelled' ,
  `processed_by` VARCHAR(64) NULL ,
  `processor_comment` VARCHAR(255) NULL ,
  `process_date` DATETIME NULL COMMENT 'Date the request got processed.' ,
  `version` INT NOT NULL DEFAULT 0 COMMENT 'For optimistic concurrency control.' ,
  INDEX `fk_change_request_name1` (`name_id` ASC) ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_change_request_name1`
    FOREIGN KEY (`name_id` )
    REFERENCES `name` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'a request to add,delete,modify a name.';


-- -----------------------------------------------------
-- Table `revision`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `revision` ;

CREATE  TABLE IF NOT EXISTS `revision` (
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
INSERT INTO `configuration` (`name`, `value`) VALUES ('version', '2.0');

COMMIT;
