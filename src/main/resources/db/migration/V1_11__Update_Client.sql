ALTER TABLE client_entity ADD COLUMN client_status varchar(255)  null AFTER originator_bank_code;

update  client_entity set client_status ="Active";

ALTER TABLE client_entity modify column  client_status varchar(255) not null;