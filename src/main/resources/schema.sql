-- auto-generated definition

-- auto-generated definition
create table notice
(
    id       LONG auto_increment
        primary key,
    content  text         not null,
    file_id  LONG       null,
    mod_date DATETIME  null,
    reg_date DATETIME  null,
    title    varchar(150) not null,
    user_id  LONG       null,
    username varchar(255) null
);



create table file
(
    id            LONG auto_increment
        primary key,
    file_path     varchar(255) null,
    filename      varchar(255) null,
    notice_id     LONG       null,
    orig_filename varchar(255) null,
    reg_date      DATETIME  null
);

create index idx_notice_id
    on file (notice_id);

-- auto-generated definition
create table user
(
    id          LONG auto_increment
        primary key,
    email       varchar(255) null,
    password    varchar(255) null,
    provider    varchar(255) null,
    provider_id varchar(255) null,
    reg_date    datetime  null,
    role        varchar(255) null,
    username    varchar(255) null
);

