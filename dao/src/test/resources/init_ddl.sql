--todo use the schema

CREATE TABLE IF NOT EXISTS `currency_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL,
  `code` VARCHAR(5) NULL,
  `symbol` VARCHAR(5) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `category_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `parent` INT NULL,
  PRIMARY KEY (`id`),
--  INDEX `parent_category_fk_idx` (`parent` ASC),
  CONSTRAINT `parent_category_fk`
    FOREIGN KEY (`parent`)
    REFERENCES `category_tbl` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL);


CREATE TABLE IF NOT EXISTS `account_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `account_type` VARCHAR(45) NOT NULL,
  `currency` INT NULL,
  `amount` DECIMAL(12,2) NULL,
  PRIMARY KEY (`id`),
--  INDEX `currency_idx` (`currency` ASC),
  CONSTRAINT `account_currency_fk`
    FOREIGN KEY (`currency`)
    REFERENCES `currency_tbl` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL);

CREATE TABLE IF NOT EXISTS `transaction_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `amount` DECIMAL NULL,
  `datetime` DATETIME NULL,
  `account` INT NULL,
  `category` INT NULL,
  `transaction_type` VARCHAR(45) NOT NULL,
  `tags` VARCHAR(1024) NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
--  INDEX `accId_idx` (`account` ASC),
--  INDEX `transaction_category_fk_idx` (`category` ASC),
  CONSTRAINT `transaction_account_fk`
    FOREIGN KEY (`account`)
    REFERENCES `account_tbl` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL,
  CONSTRAINT `transaction_category_fk`
    FOREIGN KEY (`category`)
    REFERENCES `category_tbl` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL);

CREATE TABLE IF NOT EXISTS `tag_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `count` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `tag_relation_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tag` INT NOT NULL,
  `transaction` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `relation_tag_fk`
    FOREIGN KEY (`tag`)
    REFERENCES `tag_tbl` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL,
  CONSTRAINT `relation_transaction_fk`
    FOREIGN KEY (`transaction`)
    REFERENCES `transaction_tbl` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL);