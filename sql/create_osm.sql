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
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `osm`.`ways`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`ways` (
  `id` BIGINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `osm`.`relations`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`relations` (
  `id` BIGINT UNSIGNED NOT NULL ,
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


-- -----------------------------------------------------
-- Table `osm`.`node_tags`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`node_tags` (
  `node` BIGINT UNSIGNED NOT NULL ,
  `key` VARCHAR(255) NOT NULL ,
  `value` VARCHAR(255) NOT NULL ,
  INDEX `fk_node_keys_1` (`node` ASC) ,
  CONSTRAINT `fk_node_keys_1`
    FOREIGN KEY (`node` )
    REFERENCES `osm`.`nodes` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `osm`.`way_tags`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`way_tags` (
  `way` BIGINT UNSIGNED NOT NULL ,
  `key` VARCHAR(255) NOT NULL ,
  `value` VARCHAR(255) NOT NULL ,
  INDEX `fk_way_keys_1` (`way` ASC) ,
  CONSTRAINT `fk_way_keys_1`
    FOREIGN KEY (`way` )
    REFERENCES `osm`.`ways` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `osm`.`relation_tags`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `osm`.`relation_tags` (
  `relation` BIGINT UNSIGNED NOT NULL ,
  `key` VARCHAR(255) NOT NULL ,
  `value` VARCHAR(255) NOT NULL ,
  INDEX `fk_relation_keys_1` (`relation` ASC) ,
  CONSTRAINT `fk_relation_keys_1`
    FOREIGN KEY (`relation` )
    REFERENCES `osm`.`relations` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
