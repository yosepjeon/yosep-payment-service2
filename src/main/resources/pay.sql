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
    user_id     bigint              not null,
    wallet_id   bigint,
    order_id    varchar(100)        not null,
    transaction_type varchar(30)    not null,
    amount      decimal(10, 2),
    description varchar(200),
    created_at  timestamp           not null,
    updated_at  timestamp           not null
);

create index user_id
    on transaction (user_id);

create index wallet_id
    on transaction (wallet_id);

create index order_id
    on transaction (order_id);

create index created_at
    on transaction (created_at);