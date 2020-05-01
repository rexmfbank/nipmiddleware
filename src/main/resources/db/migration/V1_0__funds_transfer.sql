create table funds_transfer_entity
(
  id                           int auto_increment primary key,
  amount                       decimal(19, 2) not null,
  beneficiary_account_name     varchar(255)   null,
  beneficiary_account_no       varchar(255)   not null,
  beneficiarybvn               varchar(255)   null,
  beneficiarykyclevel          varchar(255)   null,
  channel_code                 varchar(255)   null,
  created_at                   datetime       null,
  destination_institution_code varchar(255)   not null,
  last_updated                 datetime       null,
  name_enquiry_reference       varchar(255)   null,
  narration                    varchar(255)   null,
  originator_account_name      varchar(255)   null,
  originator_account_no        varchar(255)   not null,
  originatorbvn                varchar(255)   null,
  originator_institution_code  varchar(255)   not null,
  originatorkyclevel           varchar(255)   null,
  payment_reference            varchar(255)   null,
  payment_status          varchar(255)   null,
  response_code                varchar(255)   null,
  session_id                   varchar(255)   not null,
  transaction_location         varchar(255)   null
)
  engine = MyISAM;
