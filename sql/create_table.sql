-- 创建库
create database if not exists y_ai_code;

-- 使用数据库
use y_ai_code;
-- 用户表
create table if not exists user(
    id bigint auto_increment comment 'id' primary key ,
    userAccount varchar(256) not null comment '账号',
    userPassword varchar(512) not null  comment '密码',
    userName varchar(256)  null  comment '用户昵称',
    userAvatar varchar(1024) null comment '用户头像',
    userProfile varchar(256) null comment '用户简介',
    userRole varchar(256) default 'user' not null comment '用户角色：user/admin',
    editTime datetime default CURRENT_TIMESTAMP not null  comment '编辑时间',
    createTime datetime default current_timestamp not null comment '创建时间',
    updateTime datetime default current_timestamp not null  on update current_timestamp comment '更新时间',
    isDelete tinyint default  0 not null  comment '是否删除',
    unique  key uk_userAccount(userAccount),
    index idx_userName(userName)

)comment '用户表' collate =utf8mb4_unicode_ci;