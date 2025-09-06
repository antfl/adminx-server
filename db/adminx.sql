create table article_category
(
    category_id   bigint unsigned auto_increment comment '分类 ID'
        primary key,
    category_name varchar(50)                        not null comment '分类名称',
    remark        varchar(500)                       null comment '备注',
    create_user   bigint                             not null comment '创建人',
    update_user   bigint                             null comment '更新人',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted    tinyint  default 0                 not null comment '删除标记（0：未删除，1：已删除）',
    constraint uniq_category_name
        unique (category_name)
)
    row_format = DYNAMIC;

create table sys_dept
(
    dept_id     bigint auto_increment comment '部门 ID'
        primary key,
    parent_id   bigint     default 0                 not null comment '父部门 ID',
    dept_name   varchar(50)                          not null comment '部门名称',
    sort        int        default 0                 null comment '显示顺序',
    leader      varchar(50)                          null comment '负责人',
    phone       varchar(20)                          null comment '联系电话',
    email       varchar(50)                          null comment '邮箱',
    status      tinyint(1) default 0                 not null comment '状态（0：正常，1：停用）',
    remark      varchar(500)                         null comment '备注',
    create_user bigint                               null comment '创建人',
    update_user bigint                               null comment '更新人',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime                             null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint(1) default 0                 not null comment '删除标记（0：未删除，1：已删除）'
)
    comment '部门表' row_format = DYNAMIC;

create index idx_parent_id
    on sys_dept (parent_id);

create table sys_dict
(
    id          bigint auto_increment comment '主键 ID'
        primary key,
    dict_name   varchar(100)                       not null comment '字典名称',
    dict_code   varchar(100)                       not null comment '字典编码',
    status      tinyint  default 1                 not null comment '状态（0：禁用，1：启用）',
    remark      varchar(500)                       null comment '备注',
    create_user bigint                             null comment '创建人',
    update_user bigint                             null comment '更新人',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint  default 0                 not null comment '删除标记（0：未删除，1：已删除）',
    constraint uniq_dict_code
        unique (dict_code)
)
    comment '系统字典表' row_format = DYNAMIC;

create table sys_dict_item
(
    id          bigint auto_increment comment '主键 ID'
        primary key,
    dict_id     bigint                             not null comment '字典 ID',
    item_label  varchar(100)                       not null comment '字典项标签',
    item_value  varchar(100)                       not null comment '字典项值',
    sort        int      default 0                 null comment '排序',
    status      tinyint  default 1                 not null comment '状态（0：禁用，1：启用）',
    remark      varchar(500)                       null comment '备注',
    create_user bigint                             null comment '创建人',
    update_user bigint                             null comment '更新人',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint  default 0                 not null comment '删除标记（0：未删除，1：已删除）',
    constraint fk_dict_id
        foreign key (dict_id) references sys_dict (id)
            on delete cascade
)
    comment '字典值表' row_format = DYNAMIC;

create index idx_dict_id
    on sys_dict_item (dict_id);

create table sys_file_record
(
    id          bigint unsigned auto_increment comment '主键ID'
        primary key,
    file_name   varchar(500)  not null comment '文件名',
    file_path   varchar(1000) not null comment '文件路径',
    file_size   bigint        not null comment '文件大小（字节）',
    create_user bigint        not null comment '创建人',
    update_user bigint        not null comment '修改人',
    create_time datetime(6)   not null comment '创建时间',
    update_time datetime(6)   not null comment '更新时间',
    is_deleted  int default 0 not null comment '删除标志（0-未删除，1-已删除）'
)
    comment '文件记录表';

create table sys_menu
(
    id          bigint auto_increment comment '菜单 ID'
        primary key,
    parent_id   bigint     default 0                 not null comment '父菜单 ID',
    name        varchar(64)                          not null comment '菜单名（路由名称）',
    title_zh    varchar(64)                          not null comment '菜单中文名',
    title_eh    varchar(64)                          not null comment '菜单英文名',
    path        varchar(255)                         null comment '路由路径',
    component   varchar(255)                         null comment '组件路径',
    icon        varchar(64)                          null comment '图标类名',
    is_cache    tinyint(1) default 0                 not null comment '是否缓存（0：否，1：是）',
    is_visible  tinyint(1) default 1                 not null comment '是否显示（0：否，1：是）',
    redirect    varchar(255)                         null comment '重定向路径',
    menu_type   tinyint    default 0                 not null comment '菜单类型（0：目录，1：菜单，2：按钮）',
    permission  varchar(100)                         null comment '权限标识',
    sort_order  int        default 0                 not null comment '排序',
    status      tinyint    default 1                 not null comment '状态（0：停用，1：正常）',
    remark      varchar(500)                         null comment '备注',
    create_user bigint                               null comment '创建人',
    update_user bigint                               null comment '更新人',
    create_time datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint    default 0                 not null comment '删除标记（0：未删除，1：已删除）'
)
    comment '菜单管理表' row_format = DYNAMIC;

create index idx_parent_id
    on sys_menu (parent_id);

create table sys_operation_log
(
    id             bigint auto_increment comment '主键 ID'
        primary key,
    operator       varchar(50)                not null comment '操作人',
    module         varchar(50)                not null comment '操作模块',
    type           varchar(20)                not null comment '操作类型',
    description    varchar(200)               not null comment '操作描述',
    request_method varchar(10)                null comment '请求方法',
    operation_time datetime   default (now()) null comment '操作时间',
    duration       bigint                     null comment '执行时长（ms）',
    ip             varchar(50)                null comment '操作IP',
    params         text                       null comment '请求参数',
    result         text                       null comment '返回结果',
    status         tinyint(1) default 1       not null comment '操作状态（1：成功，0：失败）',
    error_msg      text                       null comment '错误信息'
)
    comment '操作日志表' row_format = DYNAMIC;

create index idx_module
    on sys_operation_log (module);

create index idx_operator
    on sys_operation_log (operator);

create table sys_role
(
    role_id     bigint auto_increment comment '角色 ID'
        primary key,
    role_name   varchar(50)                          not null comment '角色名称',
    role_key    varchar(100)                         not null comment '角色权限字符串',
    sort        int        default 0                 null comment '显示顺序',
    data_scope  tinyint    default 1                 null comment '数据范围（1：全部数据，2：自定义数据）',
    status      tinyint(1) default 0                 not null comment '状态（0：正常，1：停用）',
    remark      varchar(500)                         null comment '备注',
    create_user bigint                               null comment '创建人',
    update_user bigint                               null comment '更新人',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime                             null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint(1) default 0                 not null comment '删除标记（0：未删除，1：已删除）',
    constraint uniq_role_key
        unique (role_key)
)
    comment '角色表' row_format = DYNAMIC;

create table sys_role_menu
(
    id          bigint auto_increment comment '主键'
        primary key,
    role_id     bigint                             not null comment '角色 ID',
    menu_id     bigint                             not null comment '菜单 ID',
    create_user bigint                             null comment '创建人',
    update_user bigint                             null comment '更新人',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uniq_role_menu
        unique (role_id, menu_id)
)
    comment '角色和菜单关联表' row_format = DYNAMIC;

create table sys_third_party_auth
(
    id            bigint unsigned auto_increment comment '主键 ID'
        primary key,
    user_id       bigint unsigned                    null comment '系统用户 ID（未绑定时为空）',
    provider      varchar(20)                        not null comment '三方平台标识(wechat/github/qq)',
    open_id       varchar(128)                       not null comment '三方平台用户唯一 ID',
    union_id      varchar(128)                       null comment '跨平台统一ID(微信生态专用)',
    access_token  varchar(512)                       null comment '访问令牌',
    refresh_token varchar(512)                       null comment '刷新令牌',
    expire_time   datetime                           null comment '令牌过期时间',
    bind_time     datetime default CURRENT_TIMESTAMP not null comment '绑定时间',
    avatar_url    varchar(512)                       null comment '三方账号头像URL',
    nickname      varchar(100)                       null comment '三方账号昵称',
    constraint uniq_provider_openid
        unique (provider, open_id),
    constraint uniq_user_provider
        unique (user_id, provider)
)
    comment '用户三方登录绑定关系表' collate = utf8mb4_unicode_ci;

create index idx_user_id
    on sys_third_party_auth (user_id);

create table sys_user
(
    user_id         bigint auto_increment comment '用户 ID'
        primary key,
    dept_id         bigint                                 null comment '部门 ID',
    username        varchar(50)                            null comment '用户名',
    password        varchar(100)                           null comment '密码',
    nickname        varchar(50)                            not null comment '用户昵称',
    email           varchar(50)                            null comment '用户邮箱',
    phone           varchar(20)                            null comment '手机号码',
    avatar          varchar(200)                           null comment '头像地址',
    gender          tinyint(1) default 0                   null comment '性别（0：未知，1：男，2：女）',
    status          tinyint(1) default 0                   not null comment '状态（0：正常，1：停用）',
    last_login_ip   varchar(50)                            null comment '最后登录IP',
    last_login_time datetime                               null comment '最后登录时间',
    create_user     bigint                                 null comment '创建人',
    update_user     bigint                                 null comment '更新人',
    create_time     datetime   default CURRENT_TIMESTAMP   null comment '创建时间',
    update_time     datetime                               null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted      tinyint(1) default 0                   not null comment '删除标记（0：未删除，1：已删除）',
    open_id         varchar(64) collate utf8mb4_unicode_ci null comment 'QQ开放平台唯一标识',
    constraint idx_user_openId
        unique (open_id),
    constraint uniq_username
        unique (username),
    constraint fk_user_dept
        foreign key (dept_id) references sys_dept (dept_id)
            on delete set null
)
    comment '用户表' row_format = DYNAMIC;

create table article
(
    article_id  bigint unsigned auto_increment comment '文章 ID'
        primary key,
    title       varchar(100)                           not null comment '标题',
    content     text                                   not null comment '内容',
    category_id bigint unsigned                        not null comment '分类 ID',
    status      tinyint(1)   default 1                 not null comment '文章状态（0：仅自己查看，1：公开）',
    like_count  int unsigned default '0'               not null comment '点赞数',
    remark      varchar(500)                           null comment '备注',
    create_user bigint                                 not null comment '创建人',
    update_user bigint                                 not null comment '更新人',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint      default 0                 not null comment '删除标记（0：未删除，1：已删除）',
    constraint fk_article_category
        foreign key (category_id) references article_category (category_id)
            on update cascade,
    constraint fk_article_user
        foreign key (create_user) references sys_user (user_id)
            on update cascade on delete cascade
)
    comment '文章表' row_format = DYNAMIC;

create index idx_category
    on article (category_id);

create index idx_user
    on article (create_user);

create table comment
(
    comment_id       bigint unsigned auto_increment comment '评论 ID'
        primary key,
    article_id       bigint unsigned                           not null comment '文章 ID',
    content          text                                      not null comment '评论内容',
    parent_id        bigint unsigned default '0'               not null comment '父评论 ID（0：顶级评论）',
    reply_to_user_id bigint                                    null comment '被回复的用户 ID',
    create_user      bigint                                    not null comment '创建人',
    update_user      bigint                                    null comment '更新人',
    create_time      datetime        default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime        default CURRENT_TIMESTAMP not null comment '更新时间',
    constraint fk_comment_article
        foreign key (article_id) references article (article_id)
            on update cascade on delete cascade,
    constraint fk_comment_reply_user
        foreign key (reply_to_user_id) references sys_user (user_id)
            on update cascade on delete set null,
    constraint fk_comment_user
        foreign key (create_user) references sys_user (user_id)
            on update cascade on delete cascade
)
    comment '评论表' row_format = DYNAMIC;

create index idx_article
    on comment (article_id);

create index idx_parent
    on comment (parent_id);

create index idx_user
    on comment (create_user);

create table interaction
(
    interaction_id bigint unsigned auto_increment comment '互动 ID'
        primary key,
    article_id     bigint unsigned                    not null comment '文章 ID',
    type           enum ('like', 'favorite')          not null comment '类型',
    create_user    bigint                             not null comment '创建人',
    update_user    bigint                             null comment '更新人',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP null comment '创建时间',
    constraint uniq_user_article_type
        unique (create_user, article_id, type),
    constraint fk_interaction_article
        foreign key (article_id) references article (article_id)
            on update cascade on delete cascade,
    constraint fk_interaction_user
        foreign key (create_user) references sys_user (user_id)
            on update cascade on delete cascade
)
    comment '互动记录表' row_format = DYNAMIC;

create index idx_article
    on interaction (article_id);

create index idx_dept_id
    on sys_user (dept_id);

create table sys_user_login_log
(
    log_id         bigint unsigned auto_increment comment '主键ID'
        primary key,
    user_id        bigint                                   not null comment '用户 ID（关联用户表）',
    login_time     datetime(6) default CURRENT_TIMESTAMP(6) not null comment '登录时间（精确到毫秒）',
    login_ip       varchar(45)                              not null comment '登录IP（支持IPv6）',
    user_agent     varchar(500)                             null comment '用户代理（浏览器/设备信息）',
    login_result   tinyint                                  not null comment '登录结果（1成功 0失败）',
    failure_reason varchar(100)                             null comment '失败原因（密码错误等）',
    session_id     varchar(100)                             null comment '会话ID（用于追踪）'
)
    comment '用户登录日志表';

create index idx_login_time
    on sys_user_login_log (login_time);

create index idx_user_id
    on sys_user_login_log (user_id);

create table sys_user_preference
(
    preference_id    bigint unsigned auto_increment comment '偏好设置ID'
        primary key,
    user_id          bigint                                                                    not null comment '用户ID（关联用户表）',
    preference_key   varchar(100)                                                              not null comment '偏好键名（如: theme, language）',
    preference_value json                                                                      not null comment '偏好值（JSON格式存储）',
    preference_type  enum ('boolean', 'number', 'string', 'json') default 'string'             not null comment '偏好值类型',
    created_at       datetime(6)                                  default CURRENT_TIMESTAMP(6) not null comment '创建时间',
    updated_at       datetime(6)                                  default CURRENT_TIMESTAMP(6) not null on update CURRENT_TIMESTAMP(6) comment '更新时间',
    constraint uk_user_preference
        unique (user_id, preference_key)
)
    comment '用户偏好设置表';

create index idx_user_id
    on sys_user_preference (user_id);

create table sys_user_role
(
    id          bigint unsigned auto_increment comment '主键 ID'
        primary key,
    user_id     bigint unsigned                     not null comment '用户 ID',
    role_id     bigint unsigned                     not null comment '角色 ID',
    create_user bigint                              null comment '创建人',
    update_user bigint                              null comment '更新人',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_user_role
        unique (user_id, role_id) comment '用户角色唯一约束'
)
    comment '用户角色关联表' row_format = DYNAMIC;

create index idx_role_id
    on sys_user_role (role_id);

create index idx_user_id
    on sys_user_role (user_id);


