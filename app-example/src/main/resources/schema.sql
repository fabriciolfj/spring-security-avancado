create table if not exists users (
id INT NOT NULL AUTO_INCREMENT,
username VARCHAR(45) NOT NULL,
password VARCHAR(45) NOT NULL,
enabled INT NOT NULL,
PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS authorities (
id INT NOT NULL AUTO_INCREMENT,
username VARCHAR(45) NOT NULL,
authority VARCHAR(45) NOT NULL,
PRIMARY KEY (id)
);

create table if not exists token(
id int not null auto_increment,
identifier varchar(45) null,
token text null,
primary key (id)
);