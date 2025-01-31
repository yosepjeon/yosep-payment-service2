create table wallet
(
    id         bigint primary key auto_increment,
    user_id    bigint         not null,
    balance    decimal(10, 2) not null,
    created_at timestamp      not null,
    updated_at timestamp      not null
);

create index user_id
    on wallet (user_id);

create index user_id_create_at
    on wallet (user_id, created_at);

create table transaction
(
    id          bigint primary key auto_increment,
    wallet_id   bigint         not null,
    order_id    bigint         not null,
    amount      decimal(10, 2) not null,
    type        varchar(255)   not null,
    create_time timestamp      not null
);