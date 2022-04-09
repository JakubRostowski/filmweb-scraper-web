create table if not exists movie
(
    id                   bigserial
        primary key,
    country_of_origin    varchar(255),
    critics_rate         double precision not null,
    director             varchar(255),
    genre                varchar(255),
    length               varchar(255),
    original_title       varchar(255),
    position             integer          not null,
    rate                 double precision not null,
    screenwriter         varchar(255),
    time_of_creation     timestamp,
    time_of_modification timestamp,
    title                varchar(255),
    year                 integer          not null
);

create table if not exists archived_movie
(
    id                   bigserial
        primary key,
    critics_rate         double precision not null,
    position             integer          not null,
    rate                 double precision not null,
    time_of_creation     timestamp,
    time_of_modification timestamp,
    title                varchar(255),
    movie_id             bigint
        constraint fklm351ufo3i4a17l6vsuu6q4cj
            references movie
);

create table if not exists users
(
    id                   bigserial
        primary key,
    email                varchar(255),
    enabled              boolean not null,
    password             varchar(255),
    role                 varchar(255),
    time_of_creation     timestamp,
    time_of_modification timestamp,
    username             varchar(255)
);