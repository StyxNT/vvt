create table if not exists vvt.t_menu
(
    id        int auto_increment,
    url       varchar(64)   not null comment '请求的地址',
    menu_name varchar(64)   not null comment '菜单名字',
    enabled   int default 1 null comment '是否启用',
    constraint t_menu_id_uindex
        unique (id)
)
    comment '菜单表';

alter table vvt.t_menu
    add primary key (id);

create table if not exists vvt.t_role
(
    id        int auto_increment comment '角色id',
    role      varchar(64) not null comment '角色',
    role_name varchar(64) not null comment '角色名',
    constraint t_role_id_uindex
        unique (id),
    constraint t_role_role_uindex
        unique (role)
);

alter table vvt.t_role
    add primary key (id);

create table if not exists vvt.t_menu_role
(
    id      int auto_increment comment '主键',
    menu_id int not null comment '菜单表id',
    role_id int not null comment '角色id',
    constraint t_menu_role_id_uindex
        unique (id),
    constraint t_menu_role_t_menu_id_fk
        foreign key (menu_id) references vvt.t_menu (id),
    constraint t_menu_role_t_role_id_fk
        foreign key (role_id) references vvt.t_role (id)
)
    comment '菜单权限表';

alter table vvt.t_menu_role
    add primary key (id);

create table if not exists vvt.t_user
(
    id       int auto_increment comment '用户ID'
        primary key,
    name     varchar(20)           not null comment '用户姓名',
    gender   enum ('男', '女', '未知') null comment '性别',
    username varchar(20)           not null comment '用户登录名',
    password varchar(255)          not null comment '密码',
    phone    char(11)              null comment '手机号',
    email    varchar(64)           null comment '用户邮箱',
    userface mediumtext            null comment '用户头像',
    enabled  int default 1         null comment '账户是否启用'
);

create table if not exists vvt.t_user_role
(
    id      int auto_increment comment '主键',
    user_id int not null comment '用户id',
    role_id int not null comment '角色id',
    constraint t_user_role_id_uindex
        unique (id),
    constraint t_user_role_t_role_id_fk
        foreign key (role_id) references vvt.t_role (id),
    constraint t_user_role_t_user_id_fk
        foreign key (user_id) references vvt.t_user (id)
)
    comment '用户角色表';

alter table vvt.t_user_role
    add primary key (id);


