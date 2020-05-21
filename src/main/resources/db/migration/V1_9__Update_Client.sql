
ALTER TABLE client_entity ADD COLUMN originator_bank_code varchar(255)  ;

update  client_entity set originator_bank_code ="";

ALTER TABLE client_entity modify column  originator_bank_code varchar(255) not null;

