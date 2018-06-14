
DROP SCHEMA IF EXISTS `home_finance` ;

-- -----------------------------------------------------
-- Schema home_finance
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `home_finance` DEFAULT CHARACTER SET utf8 ;
USE `home_finance` ;

-- -----------------------------------------------------
-- Table `home_finance`.`currency_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home_finance`.`currency_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL,
  `code` VARCHAR(5) NULL,
  `symbol` VARCHAR(5) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `home_finance`.`category_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home_finance`.`category_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `parent` INT NULL,
  PRIMARY KEY (`id`));

-- -----------------------------------------------------
-- Table `home_finance`.`account_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home_finance`.`account_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `account_type` VARCHAR(45) NOT NULL,
  `currency` INT NULL,
  `amount` DECIMAL(12,2) NULL,
  PRIMARY KEY (`id`),
  INDEX `currency_idx` (`currency` ASC),
  CONSTRAINT `account_currency_fk`
    FOREIGN KEY (`currency`)
    REFERENCES `home_finance`.`currency_tbl` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL);


-- -----------------------------------------------------
-- Table `home_finance`.`transaction_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home_finance`.`transaction_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `amount` DECIMAL NULL,
  `datetime` DATETIME NULL,
  `account` INT NULL,
  `category` INT NULL,
  `transaction_type` VARCHAR(45) NOT NULL,
  `tags` VARCHAR(1024) NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `accId_idx` (`account` ASC),
  INDEX `transaction_category_fk_idx` (`category` ASC),
  CONSTRAINT `transaction_account_fk`
    FOREIGN KEY (`account`)
    REFERENCES `home_finance`.`account_tbl` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL,
  CONSTRAINT `transaction_category_fk`
    FOREIGN KEY (`category`)
    REFERENCES `home_finance`.`category_tbl` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL);


-- -----------------------------------------------------
-- Data for table `home_finance`.`currency_tbl`
-- -----------------------------------------------------
START TRANSACTION;
USE `home_finance`;
INSERT INTO `home_finance`.`currency_tbl` (`id`, `name`, `code`, `symbol`) VALUES (1, 'Российский рубль', 'RUB', 'руб.');
INSERT INTO `home_finance`.`currency_tbl` (`id`, `name`, `code`, `symbol`) VALUES (2, 'Доллар США', 'USD', '$');

COMMIT;


-- -----------------------------------------------------
-- Data for table `home_finance`.`category_tbl`
-- -----------------------------------------------------
START TRANSACTION;
USE `home_finance`;
INSERT INTO `home_finance`.`category_tbl` (`id`, `name`, `parent`) VALUES (1, 'Зарплата', NULL);
INSERT INTO `home_finance`.`category_tbl` (`id`, `name`, `parent`) VALUES (2, 'Дом', NULL);
INSERT INTO `home_finance`.`category_tbl` (`id`, `name`, `parent`) VALUES (3, 'Транспорт', NULL);
INSERT INTO `home_finance`.`category_tbl` (`id`, `name`, `parent`) VALUES (4, 'Еда', NULL);
INSERT INTO `home_finance`.`category_tbl` (`id`, `name`, `parent`) VALUES (5, 'Хобби', NULL);
INSERT INTO `home_finance`.`category_tbl` (`id`, `name`, `parent`) VALUES (6, 'Авто', 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `home_finance`.`account_tbl`
-- -----------------------------------------------------
START TRANSACTION;
USE `home_finance`;
INSERT INTO `home_finance`.`account_tbl` (`id`, `name`, `account_type`, `currency`, `amount`) VALUES (1, 'Sberbank', 'DEBIT_CARD', 1, 50000);
INSERT INTO `home_finance`.`account_tbl` (`id`, `name`, `account_type`, `currency`, `amount`) VALUES (2, 'Cash', 'CASH', 1, 30000);

COMMIT;


-- -----------------------------------------------------
-- Data for table `home_finance`.`transaction_tbl`
-- -----------------------------------------------------
START TRANSACTION;
USE `home_finance`;
INSERT INTO `home_finance`.`transaction_tbl` (`id`, `amount`, `datetime`, `account`, `category`, `transaction_type`, `tags`, `name`) VALUES (1, 50000, '2018-02-02', 1, 1, 'INCOME', '#зарплата, #январь', NULL);
INSERT INTO `home_finance`.`transaction_tbl` (`id`, `amount`, `datetime`, `account`, `category`, `transaction_type`, `tags`, `name`) VALUES (2, 35000, '2018-02-05', 1, 1, 'INCOME', NULL, NULL);
INSERT INTO `home_finance`.`transaction_tbl` (`id`, `amount`, `datetime`, `account`, `category`, `transaction_type`, `tags`, `name`) VALUES (3, 25000, '2018-04-03', 2, 1, 'INCOME', NULL, NULL);
INSERT INTO `home_finance`.`transaction_tbl` (`id`, `amount`, `datetime`, `account`, `category`, `transaction_type`, `tags`, `name`) VALUES (4, 15000, '2018-04-10', 2, 1, 'INCOME', NULL, NULL);
INSERT INTO `home_finance`.`transaction_tbl` (`id`, `amount`, `datetime`, `account`, `category`, `transaction_type`, `tags`, `name`) VALUES (5, 4200, '2018-02-10', 1, 2, 'EXPENSE', NULL, NULL);
INSERT INTO `home_finance`.`transaction_tbl` (`id`, `amount`, `datetime`, `account`, `category`, `transaction_type`, `tags`, `name`) VALUES (6, 1500, '2018-04-10', 2, 3, 'EXPENSE', NULL, NULL);

COMMIT;

