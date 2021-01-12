CREATE TABLE IF NOT EXISTS user (
username varchar(45) not null,
password TEXT null,
primary key (username));

CREATE TABLE IF NOT EXISTS otp (
username varchar(45) not null,
code varchar(45) null,
primary key (username)
);