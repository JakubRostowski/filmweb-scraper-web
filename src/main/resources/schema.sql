create table if not exists movie
(
    id                   bigint auto_increment
        primary key,
    country_of_origin    varchar(255) null,
    critics_rate         double not null,
    director             varchar(255) null,
    genre                varchar(255) null,
    length               varchar(255) null,
    original_title       varchar(255) null,
    position             int    not null,
    rate                 double not null,
    screenwriter         varchar(255) null,
    time_of_creation     datetime(6) null,
    time_of_modification datetime(6) null,
    title                varchar(255) null,
    year                 int    not null
);

create table if not exists archived_movie
(
    id                   bigint auto_increment
        primary key,
    critics_rate         double not null,
    position             int    not null,
    rate                 double not null,
    time_of_creation     datetime(6) null,
    time_of_modification datetime(6) null,
    title                varchar(255) null,
    movie_id             bigint null,
    constraint FKlm351ufo3i4a17l6vsuu6q4cj
        foreign key (movie_id) references movie (id)
);

create table if not exists users
(
    id                   bigint auto_increment
        primary key,
    email                varchar(255) null,
    enabled              int not null,
    password             varchar(255) null,
    role                 varchar(255) null,
    time_of_creation     datetime(6) null,
    time_of_modification datetime(6) null,
    username             varchar(255) null
);

