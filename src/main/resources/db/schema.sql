create schema if not exists client;
create schema if not exists post;

create table if not exists client.refresh_token
(
    id           bigserial not null,
    created_date timestamp,
    token        varchar(255),
    primary key (id)
);
create table if not exists client.token
(
    id          bigserial not null,
    expiry_date timestamp,
    token       varchar(255),
    user_id     int8,
    primary key (id)
);
create table if not exists client.users
(
    id       bigserial not null,
    created  timestamp,
    email    varchar(255),
    enabled  boolean,
    password varchar(255),
    username varchar(255),
    primary key (id)
);

create table if not exists post.comment
(
    id           bigserial not null,
    created_date timestamp,
    text         varchar(255),
    post_id      int8,
    user_id      int8,
    primary key (id)
);
create table if not exists post.post
(
    post_id      bigserial not null,
    created_date timestamp,
    description  text,
    post_name    varchar(255),
    vote_count   int4,
    subreddit_id int8,
    user_id      int8,
    primary key (post_id)
);
create table if not exists post.subreddit
(
    id           bigserial not null,
    created_date timestamp,
    description  varchar(255),
    name         varchar(255),
    user_id      int8,
    primary key (id)
);
create table if not exists post.vote
(
    vote_id   bigserial not null,
    vote_type int4,
    post_id   int8,
    user_id   int8,
    primary key (vote_id)
);
create table if not exists subreddit_posts
(
    subreddit_id  int8 not null,
    posts_post_id int8 not null
);

alter table client.token
    drop constraint if exists FKj8rfw4x0wjjyibfqq566j4qng;

alter table client.token
    add constraint FKj8rfw4x0wjjyibfqq566j4qng
        foreign key (user_id)
            references client.users;

alter table post.comment
    drop constraint if exists FKs1slvnkuemjsq2kj4h3vhx7i1;

alter table if exists post.comment
    add constraint FKs1slvnkuemjsq2kj4h3vhx7i1
        foreign key (post_id)
            references post.post;

alter table post.comment
    drop constraint if exists FKqm52p1v3o13hy268he0wcngr5;

alter table if exists post.comment
    add constraint FKqm52p1v3o13hy268he0wcngr5
        foreign key (user_id)
            references client.users;


alter table post.post
    drop constraint if exists FKmlnoks6ujgl9ynt53af0bx4pj;

alter table if exists post.post
    add constraint FKmlnoks6ujgl9ynt53af0bx4pj
        foreign key (subreddit_id)
            references post.subreddit;

alter table post.post
    drop constraint if exists FK7ky67sgi7k0ayf22652f7763r;

alter table if exists post.post
    add constraint FK7ky67sgi7k0ayf22652f7763r
        foreign key (user_id)
            references client.users;

alter table post.subreddit
    drop constraint if exists FK1umuh48cq77u6i52atb21shci;

alter table if exists post.subreddit
    add constraint FK1umuh48cq77u6i52atb21shci
        foreign key (user_id)
            references client.users;

alter table post.vote
    drop constraint if exists FKl3c067ewaw5xktl5cjvniv3e9;

alter table if exists post.vote
    add constraint FKl3c067ewaw5xktl5cjvniv3e9
        foreign key (post_id)
            references post.post;

alter table post.vote
    drop constraint if exists FKkmvvqilx49120p47nr9t56omf;

alter table if exists post.vote
    add constraint FKkmvvqilx49120p47nr9t56omf
        foreign key (user_id)
            references client.users;


alter table if exists subreddit_posts
    drop constraint if exists UK_ih17w4fa2em7w3u1tt8gqv2wh;

alter table if exists subreddit_posts
    add constraint UK_ih17w4fa2em7w3u1tt8gqv2wh unique (posts_post_id);


alter table subreddit_posts
    drop constraint if exists FKl27wc8sin3rt45ayge7fanx10;


alter table if exists subreddit_posts
    add constraint FKl27wc8sin3rt45ayge7fanx10
        foreign key (posts_post_id)
            references post.post;

alter table subreddit_posts
    drop constraint if exists FK1plpyiqs72shw84g90q0fes5r;


alter table subreddit_posts
    add constraint FK1plpyiqs72shw84g90q0fes5r
        foreign key (subreddit_id)
            references post.subreddit;
