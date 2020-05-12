ALTER TABLE client_entity ADD COLUMN latitude varchar(255) null ;

ALTER TABLE client_entity ADD COLUMN longitude varchar(255) null ;

update  client_entity set latitude ="" ,longitude ="" ;