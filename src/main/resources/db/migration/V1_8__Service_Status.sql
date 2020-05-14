create table service_status(
   id    int auto_increment primary key,
   status           varchar(255)   not null,
   last_updated_at     datetime       null
);

insert into service_status (id, status, last_updated_at) VALUES (1,"UP", NOW());

