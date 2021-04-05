
create sequence hibernate_sequence;

create table tuser
(
    id bigint not null
        constraint tuser_pkey
            primary key,
    login varchar(255),
    name varchar(255),
    password varchar(255)
);
