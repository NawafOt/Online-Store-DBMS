-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema onlinestore
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema onlinestore
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `onlinestore` ;
USE `onlinestore` ;

-- -----------------------------------------------------
-- Table `onlinestore`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinestore`.`Customer` (
  `Cid` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(100) NOT NULL,
  `Email` VARCHAR(100) NOT NULL,
  `Password` VARCHAR(255) NOT NULL,
  `PhoneNumber` VARCHAR(15) NULL DEFAULT NULL,
  `Address` VARCHAR(50) NULL,
  PRIMARY KEY (`Cid`),
  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `onlinestore`.`Product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinestore`.`Product` (
  `Pid` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(100) NOT NULL,
  `UnitPrice` DECIMAL(10,2) NOT NULL,
  `Category` VARCHAR(50) NULL DEFAULT NULL,
  `Stock` INT NULL DEFAULT 0,
  `Hide` TINYINT(1) NOT NULL,
  PRIMARY KEY (`Pid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `onlinestore`.`ShippingCompany`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinestore`.`ShippingCompany` (
  `Sid` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(100) NOT NULL,
  `PhoneNumber` VARCHAR(15) NULL DEFAULT NULL,
  `Cost` DECIMAL(10,2) NULL,
  PRIMARY KEY (`Sid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `onlinestore`.`Order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinestore`.`Order` (
  `Oid` INT NOT NULL AUTO_INCREMENT,
  `Status` ENUM('PENDING', 'SHIPPING', 'DELIVERED', 'CANCELLED') NULL DEFAULT 'PENDING',
  `Date` DATE NULL,
  `CustomerID` INT NOT NULL,
  `ShippingID` INT NULL,
  `ShippingCost` DECIMAL(10,2) NULL,
  PRIMARY KEY (`Oid`),
  INDEX `fk_Order_Customer_idx` (`CustomerID` ASC) ,
  INDEX `fk_Order_ShippingCompany1_idx` (`ShippingID` ASC) ,
  CONSTRAINT `fk_Order_Customer`
    FOREIGN KEY (`CustomerID`)
    REFERENCES `OnlineStore`.`Customer` (`Cid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Order_ShippingCompany1`
    FOREIGN KEY (`ShippingID`)
    REFERENCES `OnlineStore`.`ShippingCompany` (`Sid`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `onlinestore`.`OrderProduct`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinestore`.`OrderProduct` (
  `OrderID` INT NOT NULL,
  `ProductID` INT NOT NULL,
  `Quantity` INT NULL DEFAULT 1,
  `PriceAtPurchase` DECIMAL(10,2) NULL,
  PRIMARY KEY (`OrderID`, `ProductID`),
  INDEX `fk_OrderProduct_Product_idx` (`ProductID` ASC) ,
  CONSTRAINT `fk_OrderProduct_Order`
    FOREIGN KEY (`OrderID`)
    REFERENCES `OnlineStore`.`Order` (`Oid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_OrderProduct_Product`
    FOREIGN KEY (`ProductID`)
    REFERENCES `OnlineStore`.`Product` (`Pid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `onlinestore`.`Payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinestore`.`Payment` (
  `OrderID` INT NOT NULL,
  `Method` ENUM('CREDIT_CARD', 'BANK_TRANSFER', 'CASH') NOT NULL,
  `CustomerID` INT NOT NULL,
  `TotalAmount` DECIMAL(10,2) NULL,
  PRIMARY KEY (`OrderID`),
  INDEX `fk_Payment_Customer1_idx` (`CustomerID` ASC) ,
  CONSTRAINT `fk_Payment_Customer1`
    FOREIGN KEY (`CustomerID`)
    REFERENCES `OnlineStore`.`Customer` (`Cid`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Payment_Order1`
    FOREIGN KEY (`OrderID`)
    REFERENCES `OnlineStore`.`Order` (`Oid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `onlinestore`.`Wishlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinestore`.`Wishlist` (
  `CustomerID` INT NOT NULL,
  `ProductID` INT NOT NULL,
  PRIMARY KEY (`CustomerID`, `ProductID`),
  INDEX `fk_Wishlist_Product_idx` (`ProductID` ASC) ,
  CONSTRAINT `fk_Wishlist_Customer`
    FOREIGN KEY (`CustomerID`)
    REFERENCES `OnlineStore`.`Customer` (`Cid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Wishlist_Product`
    FOREIGN KEY (`ProductID`)
    REFERENCES `OnlineStore`.`Product` (`Pid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
