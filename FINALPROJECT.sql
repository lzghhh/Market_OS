DROP DATABASE IF EXISTS MARD;
CREATE DATABASE IF NOT EXISTS MARD;
USE MARD;

CREATE TABLE manager (
 managerID INT NOT NULL AUTO_INCREMENT ,
    managerNmame varchar(45) NOT NULL,
    gender varchar(6) NOT NULL,
    employedDate DATE NOT NULL,
    PRIMARY KEY(managerID)
);


CREATE TABLE employee (
 employeeID INT NOT NULL AUTO_INCREMENT ,
    name VARCHAR(64) NOT NULL, 
    gender VARCHAR(64) NOT NULL, 
    jobDescription VARCHAR(64) NOT NULL, 
    employeeDate VARCHAR(64) NOT NULL,
    PRIMARY KEY(employeeID)
);


CREATE TABLE physicalTransaction (
	transacID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    transacDate DATE NOT NULL,
    transacTime TIME NOT NULL,
    CashID INT NOT NULL,
    CardPaymentID INT NOT NULL
);


CREATE TABLE onlineTransaction (
	transacID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    transacDate DATE NOT NULL,
    transacTime TIME NOT NULL,
	CardPaymentID INT NOT NULL,
    DeliveryID INT NOT NULL,
	FOREIGN KEY (CardPaymentID) REFERENCES card(cardID)
    ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE item (
	itemID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    itemName VARCHAR(64) NOT NULL, 
    itemCount INT NOT NULL, 
    itemPrice INT NOT NULL
);


CREATE TABLE itemWithOnlineTransac (
	transacID INT NOT NULL,
    ItemID INT NOT NULL,
    ItemAmount INT NOT NULL,
    PRIMARY KEY(transacID, ItemID),
    FOREIGN KEY (transacID) REFERENCES onlineTransaction(transacID)
    ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ItemID) REFERENCES item(itemID)
    ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE itemWithOfflineTransac (
	transacID INT NOT NULL,
    ItemID INT NOT NULL,
	ItemAmount INT NOT NULL,
    PRIMARY KEY(transacID, ItemID),
    FOREIGN KEY (transacID) REFERENCES physicalTransaction(transacID)
    ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ItemID) REFERENCES item(itemID)
    ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE card (
	cardID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cardSerNum INT NOT NULL,
    cardBank VARCHAR(64) NOT NULL,
    cardAmount INT NOT NULL
);


CREATE TABLE cash (
	cashID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	cahsAmount INT NOT NULL
);


CREATE TABLE transportation (
	packageID INT NOT NULL PRIMARY KEY, 
    destination VARCHAR(64) NOT NULL,
    receiverName VARCHAR(64) NOT NULL
);


CREATE TABLE coupon (
	couponID INT NOT NULL PRIMARY KEY, 
    couponPWD INT NOT NULL, 
    couponAmount INT NOT NULL,
    managerName VARCHAR(64) NOT NULL,
    FOREIGN KEY (managerName) REFERENCES manager(managerName)
    ON UPDATE CASCADE ON DELETE CASCADE
);

DELIMITER $$
CREATE PROCEDURE VALIDCOUPON(IN couponPWDIN INT)
	BEGIN
    SELECT IFNULL(couponAmount, -1) FROM COUPON WHERE couponPWDIN = couponPWD;
    END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE CARDPAYMENT(IN CardPaymentSerNumIN INT, IN CardPaymentAmountIN INT, IN CardBankIn VARCHAR(64))
	BEGIN 
    INSERT INTO card(cardSerNum, cardBank, cardAmount) VALUES (CardPaymentSerNumIN, CardBankIn, CardPaymentAmountIN);
    END $$
DELIMITER ;



DELIMITER $$
CREATE PROCEDURE ADDMANAGER(IN managerNmameIN VARCHAR(64), IN genderIN VARCHAR(64), IN employedDateIN DATE)
	BEGIN 
    IF EXISTS (SELECT managerID FROM manager) THEN
		DELETE FROM manager WHERE managerID = (SELECT managerID FROM manager);
	END IF;
    IF NOT EXISTS (SELECT managerID FROM manager WHERE managerNmame = managerNmameIN) THEN
		INSERT INTO manager(managerNmame, gender, employedDate) VALUES (managerNmameIN, genderIN, employedDateIN);
	END IF;
    END $$
DELIMITER;


DELIMITER $$
CREATE PROCEDURE ADDEMPLOYEE(IN employeeNameIN VARCHAR(64), IN genderIN VARCHAR(64), IN jobDescriptionIN VARCHAR(64))
	BEGIN 
    IF NOT EXISTS (SELECT employeeID FROM employee WHERE name = employeeNameIN AND employedDate = employedDateIN) THEN
		INSERT INTO employee(name, gender, jobDescription, employedDate) VALUES 
        (employeeNameIN, genderIN, jobDescriptionIN, CURRENT_DATE());
	END IF;
    END $$
DELIMITER ;
    
    
DELIMITER $$
CREATE PROCEDURE DELEMPLOYEE(IN employeeNameIN VARCHAR(64), IN genderIN VARCHAR(64), IN employedDateIN DATE)
	BEGIN 
    IF EXISTS (SELECT employeeID FROM employee WHERE name = employeeNameIN AND employedDate = employedDateIN) THEN
		DELETE FROM  employee WHERE name = employeeNameIN AND employedDate = employedDateIN AND gender = genderIN;
	END IF;
    END $$
DELIMITER ;
    

DELIMITER $$
CREATE PROCEDURE GETSTORAGE(IN item_idIN VARCHAR(64))
	BEGIN
    SELECT itemCount, itemPrice FROM item WHERE itemID = item_idIN;
    END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE CREATEPhysicalTransac()
	BEGIN 
    INSERT INTO physicalTransaction(transacDate, transacTime) VALUES (CURRENT_DATE(), CURRENT_TIME());
    END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE CREATEOnlineTransac(IN cardIDIN INT)
	BEGIN 
    INSERT INTO onlineTransaction(transacDate, transacTime, CardPaymentID) VALUES (CURRENT_DATE(), CURRENT_TIME(), cardIDIN);
    END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE CREATEOnlineTransacItems(IN transacIDIN INT, IN itemNameIN VARCHAR(64), IN itemAmountIN INT)
	BEGIN 
    INSERT INTO 
    

-- DROP PROCEDURE IF EXISTS PUSHTOPhysical;
-- DELIMITER $$
-- CREATE PROCEDURE PUSHTOPhysical(IN 
