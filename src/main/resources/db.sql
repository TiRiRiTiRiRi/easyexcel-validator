-- auto-generated definition
create table personnel_information
(
    id                   bigint unsigned auto_increment comment '主键ID'
        primary key,
    name                 char(32)                               not null comment '姓名',
    name_spelling        varchar(200)                           not null comment '姓名全拼',
    gender               varchar(10)                            not null comment '性别',
    identity_card_type   varchar(200)                           not null comment '身份证件类型：居民身份证、士官证、学生证、驾驶证、护照、港澳通行证',
    identity_card_number varchar(200)                           not null comment '身份证件号码',
    birthday             date                                   not null comment '出生日期',
    phone                varchar(200) default ''                not null comment '手机号码',
    email                varchar(200) default ''                not null comment '电子邮箱',
    create_time          datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time          datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '人员基本信息表';


