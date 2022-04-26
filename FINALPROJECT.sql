-- DROP DATABASE IF EXISTS MARD;
CREATE DATABASE IF NOT EXISTS MARD;
USE MARD;

CREATE TABLE manager (
	managerID INT NOT NULL AUTO_INCREMENT,
	managerName varchar(64) NOT NULL,
	gender varchar(64) NOT NULL,
	employedDate DATE NOT NULL,
	PRIMARY KEY(managerID)
);


CREATE TABLE employee (
	employeeID INT NOT NULL AUTO_INCREMENT ,
	name VARCHAR(64) NOT NULL, 
	gender VARCHAR(64) NOT NULL, 
	jobDescription VARCHAR(64) NOT NULL, 
	employeeDate DATE NOT NULL,
	PRIMARY KEY(employeeID)
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


CREATE TABLE physicalTransaction (
	transacID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    transacDate DATE NOT NULL,
    transacTime TIME NOT NULL,
    CashID INT NOT NULL,
    CardPaymentID INT NOT NULL,
    FOREIGN KEY (CardPaymentID) REFERENCES card(cardID)
    ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (CashID) REFERENCES cash(cashID)
    ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE onlineTransaction (
	transacID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    transacDate DATE NOT NULL,
    transacTime TIME NOT NULL,
	CardPaymentID INT NOT NULL,
--     DeliveryID INT NOT NULL,
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
	onlineItemsID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	transacID INT NOT NULL,
    ItemID INT NOT NULL,
    ItemAmount INT NOT NULL,
    FOREIGN KEY (transacID) REFERENCES onlineTransaction(transacID)
    ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ItemID) REFERENCES item(itemID)
    ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE itemWithOfflineTransac (
	offlineItemsID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	transacID INT NOT NULL,
    ItemID INT NOT NULL,
	ItemAmount INT NOT NULL,
    FOREIGN KEY (transacID) REFERENCES physicalTransaction(transacID)
    ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ItemID) REFERENCES item(itemID)
    ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE coupon (
	couponID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
    couponPWD INT NOT NULL, 
    couponAmount INT NOT NULL,
    managerID INT NOT NULL,
    FOREIGN KEY (managerID) REFERENCES manager(managerID)
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
CREATE PROCEDURE CASHPAYMENT(IN CashPaymentAmountIN INT) 
	BEGIN 
    INSERT INTO cash(cahsAmount) VALUES (CashPaymentAmountIN);
    END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE ADDMANAGER(IN managerNmameIN VARCHAR(64), IN genderIN VARCHAR(64))
	BEGIN 
    IF EXISTS (SELECT managerID FROM manager) THEN
		DELETE FROM manager WHERE managerID = 1;
	END IF;
    IF NOT EXISTS (SELECT managerID FROM manager WHERE managerName = managerNmameIN) THEN
		INSERT INTO manager(managerName, gender, employedDate) VALUES (managerNmameIN, genderIN, CURRENT_DATE());
	END IF;
    END $$
DELIMITER ;



DELIMITER $$
CREATE PROCEDURE ADDEMPLOYEE(IN employeeNameIN VARCHAR(64), IN genderIN VARCHAR(64), IN jobDescriptionIN VARCHAR(64))
	BEGIN 
    IF NOT EXISTS (SELECT employeeID FROM employee WHERE name = employeeNameIN) THEN
		INSERT INTO employee(name, gender, jobDescription, employeeDate) VALUES 
        (employeeNameIN, genderIN, jobDescriptionIN, CURRENT_DATE());
	END IF;
    END $$
DELIMITER ;
    
    
DELIMITER $$
CREATE PROCEDURE DELEMPLOYEE(IN employeeNameIN VARCHAR(64), IN employeeIDIN VARCHAR(64))
	BEGIN 
    IF EXISTS (SELECT employeeID FROM employee WHERE name = employeeNameIN AND employeeID = employeeIDIN) THEN
		DELETE FROM  employee WHERE name = employeeNameIN AND employeeID = employeeIDIN;
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
CREATE PROCEDURE CREATEOnlineTransac(IN cardIDIN INT)
	BEGIN 
    INSERT INTO onlineTransaction(transacDate, transacTime, CardPaymentID) VALUES (CURRENT_DATE(), CURRENT_TIME(), cardIDIN);
    END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE CREATEOnlineTransacItems(IN transacIDIN INT, IN itemIDIN INT, IN itemAmountIN INT)
	BEGIN 
    INSERT INTO itemWithOnlineTransac(transacID, ItemID, ItemAmount) VALUES (transacIDIN, itemIDIN, itemAmountIN);
    UPDATE item
    SET itemCount = itemCount - itemAmountIN
    WHERE itemIDIN = itemID;
    END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE CREATEPhysicalTransac(IN cardIDIN INT, IN cashIDIN INT)
	BEGIN 
    INSERT INTO physicalTransaction(transacDate, transacTime, CashID, CardPaymentID) VALUES (CURRENT_DATE(), CURRENT_TIME(), cashIDIN, cardIDIN);
    END $$
DELIMITER ;
    
DELIMITER $$
CREATE PROCEDURE CREATEOfflineTransacItems(IN transacIDIN INT, IN itemIDIN INT, IN itemAmountIN INT)
	BEGIN 
    INSERT INTO itemWithOfflineTransac(transacID, ItemID, ItemAmount) VALUES (transacIDIN, itemIDIN, itemAmountIN);
    UPDATE item
    SET itemCount = itemCount - itemAmountIN
    WHERE itemIDIN = itemID;
    END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE INSERTITEM(IN itemNameIN VARCHAR(64), IN itemAmountIN INT, IN itemPriceIN INT) 
	BEGIN 
    IF EXISTS (SELECT itemID FROM item WHERE itemName = itemNameIN)  THEN
		UPDATE item 
        SET itemCount = itemCount + itemAmountIN WHERE itemNameIN = itemName;
	END IF;
    IF NOT EXISTS (SELECT itemID FROM item WHERE itemName = itemNameIN) THEN 
		INSERT INTO item(itemName, itemCount, itemPrice) VALUES (itemNameIN, itemAmountIN, itemPriceIN);
	END IF;
    END $$
DELIMITER ;

DELIMITER $$ 
CREATE PROCEDURE DELITEM(IN itemIDIN INT, IN itemAmountDecrease INT) 
BEGIN 
	IF EXISTS (SELECT * FROM item WHERE itemID = itemIDIN) THEN
		UPDATE item
		SET itemCount = itemCount - itemAmountDecrease
		WHERE itemID = itemIDIN;
	END IF;
END $$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE ADDCOUPON(IN couponPWDIN INT, IN couponAmountIN INT, IN managerNameIN VARCHAR(64))
BEGIN 
	IF NOT EXISTS (SELECT * FROM coupon WHERE couponPWDIN = couponPWD) THEN
		INSERT INTO coupon(couponPWD, couponAmount, managerID) VALUES (couponPWDIN, couponAmountIN, managerNameIN);
	END IF;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE DELCOUPON(IN couponIDIN INT) 
BEGIN 
	IF EXISTS (SELECT * FROM coupon WHERE couponIDIN = couponID) THEN
		DELETE FROM coupon WHERE couponIDIN = couponID;
	END IF;
END $$
DELIMITER ;