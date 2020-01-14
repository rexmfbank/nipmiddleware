package com.globalaccelerex.nipmiddleware.enums;

public enum PaymentStatusEnum {

    SUCCESS , //payment was successful
    PENDING , //awaiting response , requires tsq
    FAILED  , //Transaction Not successfull
    ACTIVE    //Initial Name Enquiry
}
