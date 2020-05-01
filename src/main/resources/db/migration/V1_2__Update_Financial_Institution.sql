Alter TABLE financial_institution_entity add  category_code varchar(255)  null ;

Alter TABLE financial_institution_entity change  institutionCode institution_code varchar(255)  not null ;

Alter TABLE financial_institution_entity change  institutionName institution_name varchar(255)  not null ;

#RENAME TABLE financial_institution_entity to financial_institution;

#RENAME TABLE funds_transfer_entity to funds_transfer;