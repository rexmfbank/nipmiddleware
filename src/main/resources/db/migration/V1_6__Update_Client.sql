ALTER TABLE client_entity ADD COLUMN account_name varchar(255)  ;

ALTER TABLE client_entity ADD COLUMN bvn varchar(255)  ;

ALTER TABLE client_entity ADD COLUMN kyc_level varchar(255)  ;

ALTER TABLE client_entity ADD COLUMN account_no varchar(255)  ;

ALTER TABLE client_entity ADD COLUMN bank_code varchar(255)  ;

update  client_entity set account_name ="" , bvn ="" ,kyc_level = "" ,account_no= "", bank_code = "";

ALTER TABLE client_entity modify column  account_name varchar(255) not null;

ALTER TABLE client_entity modify column bvn varchar(255) not null  ;

ALTER TABLE client_entity modify column kyc_level varchar(255) not null ;

ALTER TABLE client_entity modify column account_no varchar(255) not null  ;

ALTER TABLE client_entity modify column bank_code varchar(255) not null  ;