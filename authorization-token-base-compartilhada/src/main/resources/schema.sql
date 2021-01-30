CREATE TABLE IF NOT EXISTS oauth_access_token (
token_id varchar(255) NOT NULL,
token blob,
authentication_id varchar(255) default null,
user_name varchar(255) DEFAULT NULL,
client_id varchar(255) DEFAULT NULL,
authentication blob,
refresh_token varchar(255) default null,
primary key (token_id));

create table if not exists oauth_refresh_token (
token_id varchar(255) not null,
token blob,
authentication blob,
primary key (token_id));