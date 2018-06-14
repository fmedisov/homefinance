CREATE TABLE IF NOT EXISTS `currency_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NULL,
  `code` VARCHAR(5) NULL,
  `symbol` VARCHAR(5) NULL);

 CREATE TABLE IF NOT EXISTS `category_tbl` (
   `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `name` VARCHAR(45) NULL,
   `parent` INT NULL);

 CREATE TABLE IF NOT EXISTS `account_tbl` (
   `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `name` VARCHAR(45) NULL,
   `account_type` VARCHAR(45) NOT NULL,
   `currency` INT NULL,
   `amount` DECIMAL(12,2) NULL);

 CREATE TABLE IF NOT EXISTS `transaction_tbl` (
   `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `amount` DECIMAL NULL,
   `datetime` DATETIME NULL,
   `account` INT NULL,
   `category` INT NULL,
   `transaction_type` VARCHAR(45) NOT NULL,
   `tags` VARCHAR(1024) NULL,
   `name` VARCHAR(45) NULL);