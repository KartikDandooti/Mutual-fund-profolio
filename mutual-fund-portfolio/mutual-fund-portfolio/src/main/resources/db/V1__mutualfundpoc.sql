CREATE TABLE mutual_fund (
  id int NOT NULL AUTO_INCREMENT,
  type varchar(20) NOT NULL,
  description varchar(200) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE mutual_fund_scheme (
  id int NOT NULL AUTO_INCREMENT,
  net_asset_value decimal(10,2) NOT NULL,
  type_id int,
  scheme_name varchar(20) NOT NULL,
  description varchar(200) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (type_id) REFERENCES mutual_fund (id)
);

CREATE TABLE user (
  age int NOT NULL,
  enabled bit(1) NOT NULL,
  id int NOT NULL AUTO_INCREMENT,
  modified_date datetime DEFAULT NULL,
  phone_number bigint NOT NULL,
  registered_date datetime NOT NULL,
  portfolio_number varchar(10) NOT NULL UNIQUE,
  role varchar(10) NOT NULL,
  first_name varchar(20) NOT NULL,
  last_name varchar(20) NOT NULL,
  email varchar(50) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE transaction (
  amount decimal(10,2) NOT NULL,
  id int NOT NULL AUTO_INCREMENT,
  nav_units decimal(10,2) NOT NULL,
  net_asset_value decimal(10,2) NOT NULL,
  scheme_id int,
  user_id int,
  transaction_date datetime NOT NULL,
  transaction_type enum('INVEST','REDEEM') NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (scheme_id) REFERENCES mutual_fund_scheme (id),
  FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE user_scheme (
  current_amount decimal(10,2) NOT NULL,
  invested_amount decimal(10,2) NOT NULL,
  scheme_id int NOT NULL,
  scheme_units decimal(10,2) NOT NULL,
  user_id int NOT NULL,
  PRIMARY KEY (scheme_id,user_id),
  FOREIGN KEY (user_id) REFERENCES user (id),
  FOREIGN KEY (scheme_id) REFERENCES mutual_fund_scheme (id)
);

INSERT INTO mutual_fund VALUES (1,'Equity','This is Equity type Mutual Fund'),
(2,'Debt','This is Debt type Mutual Fund'),
(3,'Hybrid','This is Hybrid type Mutual Fund');

INSERT INTO mutual_fund_scheme VALUES (1,200.00,1,'SBI Equity','This is SBI Equity  Mutual Fund Scheme'),
(2,200.00,1,'Axis Equity','This is Axis Equity Mutual Fund Scheme'),
(3,200.00,2,'SBI Debt','This is SBI Debt type Mutual Fund Scheme'),
(4,200.00,2,'Axis Debt','This is Axis Debt type Mutual Fund Scheme'),
(5,200.00,3,'SBI Hybrid','This is SBI Hybrid  Mutual Fund Scheme'),
(6,200.00,3,'Axis Hybrid','This is Axis Hybrid Mutual Fund Scheme'),
(7,200.00,3,'HDFC Hybrid','This is HDFC Hybrid Mutual Fund Scheme'),
(8,200.00,1,'HDFC Equity','This is HDFC Equity Mutual Fund Scheme'),
(9,200.00,2,'HDFC Debt','This is HDFC Debt Mutual Fund Scheme');

INSERT INTO user VALUES 
(23,_binary '',1,NULL,9999999999,'2024-01-10 12:18:44','admin','ADMIN','ADMIN','ADMIN','admin22@gmail.com','$2a$10$804D1Gy0dFTxBJ7g3T4zf.h7E7cbJclwbDFJ21gzC4Z/tLE6OqDE6'),
(23,_binary '',2,NULL,9843456789,'2024-01-10 12:18:44','user','USER','User','User','user@gmail.com','$2a$10$CeOyG/X8NRtjnIvR46trPuVAjw73jp0rYoON/1/vnD1Jc.djfgUBO');