# -- Added by Oleksiy Grechnyev
CREATE DATABASE IF NOT EXISTS home2ee;
USE home2ee;

# Purge the database
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS ORDERS;
DROP TABLE IF EXISTS CUSTOMERS;
DROP TABLE IF EXISTS SALESREPS;
DROP TABLE IF EXISTS OFFICES;
DROP TABLE IF EXISTS PRODUCTS;
SET FOREIGN_KEY_CHECKS = 1;
# -- /Added by Oleksiy Grechnyev

CREATE TABLE PRODUCTS
     (MFR_ID CHAR(3) NOT NULL,
  PRODUCT_ID CHAR(5) NOT NULL,
 DESCRIPTION VARCHAR(20) NOT NULL,
       PRICE DECIMAL(9,2) NOT NULL,
 QTY_ON_HAND INTEGER NOT NULL,
 PRIMARY KEY (MFR_ID, PRODUCT_ID));


CREATE TABLE OFFICES
     (OFFICE INTEGER NOT NULL,
        CITY VARCHAR(15) NOT NULL,
      REGION VARCHAR(10) NOT NULL,
         MGR INTEGER,
      TARGET DECIMAL(9,2),
       SALES DECIMAL(9,2) NOT NULL,
 PRIMARY KEY (OFFICE));


CREATE TABLE SALESREPS
   (EMPL_NUM INTEGER NOT NULL,
             CHECK (EMPL_NUM BETWEEN 101 AND 199),
        NAME VARCHAR(15) NOT NULL,
         AGE INTEGER,
  REP_OFFICE INTEGER,
       TITLE VARCHAR(10),
   HIRE_DATE DATE NOT NULL,
     MANAGER INTEGER,
       QUOTA DECIMAL(9,2),
       SALES DECIMAL(9,2) NOT NULL,
 PRIMARY KEY (EMPL_NUM),
 FOREIGN KEY (MANAGER)
  REFERENCES SALESREPS(EMPL_NUM)
  ON DELETE SET NULL,
 FOREIGN KEY WORKSIN (REP_OFFICE)  
  REFERENCES OFFICES(OFFICE)
  ON DELETE SET NULL);


CREATE TABLE CUSTOMERS
   (CUST_NUM INTEGER    NOT NULL,
    COMPANY  VARCHAR(20) NOT NULL,
    CUST_REP INTEGER,
    CREDIT_LIMIT DECIMAL(9,2),
 PRIMARY KEY (CUST_NUM),
 FOREIGN KEY HASREP (CUST_REP)
  REFERENCES SALESREPS(EMPL_NUM)
   ON DELETE SET NULL);


CREATE TABLE ORDERS
  (ORDER_NUM INTEGER NOT NULL,
             CHECK (ORDER_NUM > 100000),
  ORDER_DATE DATE NOT NULL,
        CUST INTEGER NOT NULL,
         REP INTEGER,
         MFR CHAR(3) NOT NULL,
     PRODUCT CHAR(5) NOT NULL,
         QTY INTEGER NOT NULL,
      AMOUNT DECIMAL(9,2) NOT NULL,
 PRIMARY KEY (ORDER_NUM),
 FOREIGN KEY PLACEDBY (CUST)
  REFERENCES CUSTOMERS(CUST_NUM)
   ON DELETE CASCADE,
 FOREIGN KEY TAKENBY (REP)
  REFERENCES SALESREPS(EMPL_NUM)
   ON DELETE SET NULL,
 FOREIGN KEY ISFOR (MFR, PRODUCT)
  REFERENCES PRODUCTS(MFR_ID, PRODUCT_ID)
   ON DELETE RESTRICT);


ALTER TABLE OFFICES
  ADD CONSTRAINT HASMGR
  FOREIGN KEY (MGR) REFERENCES SALESREPS(EMPL_NUM)
  ON DELETE SET NULL;

  ALTER TABLE OFFICES 
  DROP FOREIGN KEY HASMGR;
DROP TABLE ORDERS;
DROP TABLE CUSTOMERS;
DROP TABLE SALESREPS;
DROP TABLE OFFICES;
DROP TABLE PRODUCTS;


CREATE TABLE PRODUCTS
     (MFR_ID CHAR(3) NOT NULL,
  PRODUCT_ID CHAR(5) NOT NULL,
 DESCRIPTION VARCHAR(20) NOT NULL,
       PRICE DECIMAL(9,2) NOT NULL,
 QTY_ON_HAND INTEGER NOT NULL,
 PRIMARY KEY (MFR_ID, PRODUCT_ID));


CREATE TABLE OFFICES
     (OFFICE INTEGER NOT NULL,
        CITY VARCHAR(15) NOT NULL,
      REGION VARCHAR(10) NOT NULL,
         MGR INTEGER,
      TARGET DECIMAL(9,2),
       SALES DECIMAL(9,2) NOT NULL,
 PRIMARY KEY (OFFICE));


CREATE TABLE SALESREPS
   (EMPL_NUM INTEGER NOT NULL,
             CHECK (EMPL_NUM BETWEEN 101 AND 199),
        NAME VARCHAR(15) NOT NULL,
         AGE INTEGER,
  REP_OFFICE INTEGER,
       TITLE VARCHAR(10),
   HIRE_DATE DATE NOT NULL,
     MANAGER INTEGER,
       QUOTA DECIMAL(9,2),
       SALES DECIMAL(9,2) NOT NULL,
 PRIMARY KEY (EMPL_NUM),
 FOREIGN KEY (MANAGER)
  REFERENCES SALESREPS(EMPL_NUM)
  ON DELETE SET NULL,
 FOREIGN KEY WORKSIN (REP_OFFICE)  
  REFERENCES OFFICES(OFFICE)
  ON DELETE SET NULL);


CREATE TABLE CUSTOMERS
   (CUST_NUM INTEGER    NOT NULL,
    COMPANY  VARCHAR(20) NOT NULL,
    CUST_REP INTEGER,
    CREDIT_LIMIT DECIMAL(9,2),
 PRIMARY KEY (CUST_NUM),
 FOREIGN KEY HASREP (CUST_REP)
  REFERENCES SALESREPS(EMPL_NUM)
   ON DELETE SET NULL);


CREATE TABLE ORDERS
  (ORDER_NUM INTEGER NOT NULL,
             CHECK (ORDER_NUM > 100000),
  ORDER_DATE DATE NOT NULL,
        CUST INTEGER NOT NULL,
         REP INTEGER,
         MFR CHAR(3) NOT NULL,
     PRODUCT CHAR(5) NOT NULL,
         QTY INTEGER NOT NULL,
      AMOUNT DECIMAL(9,2) NOT NULL,
 PRIMARY KEY (ORDER_NUM),
 FOREIGN KEY PLACEDBY (CUST)
  REFERENCES CUSTOMERS(CUST_NUM)
   ON DELETE CASCADE,
 FOREIGN KEY TAKENBY (REP)
  REFERENCES SALESREPS(EMPL_NUM)
   ON DELETE SET NULL,
 FOREIGN KEY ISFOR (MFR, PRODUCT)
  REFERENCES PRODUCTS(MFR_ID, PRODUCT_ID)
   ON DELETE RESTRICT);


ALTER TABLE OFFICES
  ADD CONSTRAINT HASMGR
  FOREIGN KEY (MGR) REFERENCES SALESREPS(EMPL_NUM)
  ON DELETE SET NULL;

