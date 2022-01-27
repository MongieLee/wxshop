create table USER
(
    ID         bigint primary key AUTO_INCREMENT,
    NAME       varchar(100),
    TEL        varchar(20) unique,
    AVATAR     varchar(1024),
    CREATED_AT datetime,
    UPDATED_AT datetime
)