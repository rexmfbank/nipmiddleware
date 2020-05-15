create table service_status(
   id    int auto_increment primary key,
   name           varchar(255)   not null,
   value           varchar(255)   not null,
   last_updated_at     datetime       null
);

insert into service_status (id, name,value, last_updated_at) VALUES (1,"CALL_NIBSS_API","UP", NOW());

