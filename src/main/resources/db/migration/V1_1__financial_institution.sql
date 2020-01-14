create table financial_institution_entity
(
    id          int auto_increment primary key,
    institutionCode     varchar(255)   not null,
    institutionName     varchar(255)   not null,
    created_at          datetime      not null
)