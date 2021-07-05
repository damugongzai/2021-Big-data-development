drop table if exists data_yys.store;
create table data_yys.store
(
  code VARCHAR(4) PRIMARY KEY
);

drop table if exists data_yys.client;
create table data_yys.client
(
  code VARCHAR(10) PRIMARY KEY,
  cli_name VARCHAR(10),
  phone_number   VARCHAR(11),
  email VARCHAR(100),
  cli_age INT,
  sex INT,
  reg_time TIMESTAMP
);


drop table if exists data_yys.commodity;
create table data_yys.commodity
(
  code VARCHAR(4) PRIMARY KEY,
  cate VARCHAR(4),
  price INT
);

drop table if exists data_yys.sale;
create table data_yys.sale
(
  sale_code VARCHAR(10) PRIMARY KEY,
  sale_city VARCHAR(10),
  com_code VARCHAR(4),
  par_time TIMESTAMP,
  sale_sex INT,
  sale_price INT,
  foreign key(com_code) references data_yys.commodity(code)
);
