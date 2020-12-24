delete from user;

insert into user (id, username, password, algorithm) values (1, 'fabricio', '$2y$12$fCynDXUAwqZ9NhG0siUwtO1b1v0R5sv5q11JnQr0VDde.bCwAqdU.', 'BCRYPT');
insert into authority (id, name, user) values (1, 'READ', 1);
insert into authority (id, name, user) values (2, 'WRITE', 1);
insert into product (id, name, price, currency) values (1, 'Leite', '10', 'USD');