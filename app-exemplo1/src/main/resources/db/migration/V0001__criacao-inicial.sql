CREATE TABLE user (
id bigint not null auto_increment,
username varchar(45) not null,
password text not null,
algorithm varchar(45) not null,
primary key (id)
)engine=InnoDB default charset=utf8;

CREATE TABLE authority (
id bigint not null auto_increment,
name varchar(45) not null,
user int not null,
primary key (id)
);

CREATE TABLE product (
id bigint not null auto_increment,
name varchar(45) not null,
price varchar(45) not null,
currency varchar(45) not null,
primary key (id)
);