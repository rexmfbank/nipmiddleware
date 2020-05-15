package com.globalaccelerex.nipmiddleware.enums;

public enum PaymentStatusEnum {

    SUCCESS , //payment was successful
    PENDING , //awaiting response , requires tsq
    FAILED  ; //Transaction Not successful


    public static boolean isPending(PaymentStatusEnum paymentStatusEnum){
        return PENDING.compareTo(paymentStatusEnum)==0;
    }
}
