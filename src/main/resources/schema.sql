create table if not exists movie
(
    id                   integer generated always as identity
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
    year                 integer          not null,
    poster               varchar(255)
);

create table if not exists archived_movie
(
    id                   integer generated always as identity
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
    id                   integer generated always as identity
        primary key,
    email                varchar(255),
    enabled              boolean not null,
    password             varchar(255),
    role                 varchar(255),
    time_of_creation     timestamp,
    time_of_modification timestamp,
    username             varchar(255)
);

create table if not exists post
(
    id                   integer generated always as identity
        primary key,
    content              varchar(255),
    time_of_creation     timestamp,
    time_of_modification timestamp,
    title                varchar(255),
    author_id            bigint
        constraint fk1mpebp1ayl0twrwm7ruiof778
            references users
);

create table if not exists post_likes
(
    post_id  bigint not null
        constraint fkmxmoc9p5ndijnsqtvsjcuoxm3
            references post,
    likes_id bigint not null
        constraint fkkts60vjpkentl81j7jkvy7adt
            references users
);

create table if not exists bug_report
(
    id                   integer generated always as identity
        primary key,
    description          varchar(255),
    status               varchar(15),
    time_of_creation     timestamp,
    time_of_modification timestamp
);
