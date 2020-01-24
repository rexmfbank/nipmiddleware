CREATE TABLE `Client`
(
   id        int auto_increment primary key,
  `client_id` VARCHAR(255) NOT NULL ,
  `client_name` VARCHAR(255) NOT NULL ,
   `password` VARCHAR(255) NOT NULL ,
  `active` BOOLEAN NOT NULL DEFAULT FALSE,
   `business_Desc` VARCHAR(255) NULL ,
   `callback_url` VARCHAR(255)  NULL ,
   `contact_email` VARCHAR(255)  NULL ,
   `contact_phone` VARCHAR(255)  NULL ,

  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `last_updated_at` TIMESTAMP on update CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,

  UNIQUE (`client_id`,`client_name` )
)
    ENGINE = InnoDB;