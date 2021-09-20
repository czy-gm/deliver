drop database if exists trans;
create database trans;
use trans;

drop table if exists t_collect;
create table t_collect
(
    src_id          int    not null comment '始发地id',
    dst_id          int    not null comment '目的地id',
    col_end_time    time   not null comment '揽收截至时间',
    col_time        int    not null comment '揽收所需时间',
    effective_time  bigint not null comment '生效时间',
    expiration_time bigint not null comment '时效时间',

    index address_id (src_id, dst_id)
);

drop table if exists t_transit;
create table t_transit
(
    src_id          int    not null comment '始发地id',
    dst_id          int    not null comment '目的地id',
    trans_days      int    not null comment '中转所需天数',
    trans_time      time   not null comment '中转到达时间',
    effective_time  bigint not null comment '生效时间',
    expiration_time bigint not null comment '时效时间',

    index address_id (src_id, dst_id)
);

drop table if exists t_deliver;
create table t_deliver
(
    dst_id          int    not null comment '目的地id',
    deliver_time    int    not null comment '派送所需时间',
    effective_time  bigint not null comment '生效时间',
    expiration_time bigint not null comment '时效时间',

    index address_id (dst_id)
);

drop table if exists t_upper_addr;
create table t_upper_addr
(
    id       int not null comment '地址id',
    upper_id int not null comment '上级地址id',

    index address_id (id)
);