SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `osm` DEFAULT CHARACTER SET utf8 ;
USE `osm` ;

-- -----------------------------------------------------
-- Table `osm`.`nodes`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`nodes` (
  `id` BIGINT UNSIGNED NOT NULL ,
  `latitude` DOUBLE NOT NULL ,
  `longitude` DOUBLE NOT NULL ,
  `tags` TEXT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `osm`.`ways`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`ways` (
  `id` BIGINT UNSIGNED NOT NULL ,
  `tags` TEXT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `osm`.`relations`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`relations` (
  `id` BIGINT UNSIGNED NOT NULL ,
  `tags` TEXT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `osm`.`way_members`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`way_members` (
  `way` BIGINT NOT NULL ,
  `node` BIGINT NOT NULL )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `osm`.`relation_members`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`relation_members` (
  `relation` BIGINT NOT NULL ,
  `way` BIGINT NOT NULL ,
  `type` TEXT NOT NULL )
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
