create table client
(
    id   bigserial not null primary key,
    name varchar(50) ,
    login varchar(255) ,
    password varchar(255)
);