
create sequence hibernate_sequence start with 1 increment by 1;

create table taddressdataset
(
    id bigint not null
        constraint taddressdataset_pkey
            primary key,
    address varchar(255)
);

create table tclient
(
    id bigint not null
        constraint tclient_pkey
            primary key,
    age integer not null,
    name varchar(255),
    address_id bigint
        constraint fkqtcp98lnh3kvy92ta7dsqnh4v
            references taddressdataset
);

create table tphonedataset
(
    id bigint not null
        constraint tphonedataset_pkey
            primary key,
    phone_number varchar(255) not null,
    client_id bigint not null
        constraint fk8u1j44t0rd2ma9lgy2pw98xjc
            references tclient
);
