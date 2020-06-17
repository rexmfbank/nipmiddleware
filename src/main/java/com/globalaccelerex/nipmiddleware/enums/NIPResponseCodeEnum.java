package com.globalaccelerex.nipmiddleware.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

import static com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum.*;

@Slf4j
@AllArgsConstructor
public enum NIPResponseCodeEnum {

    NIP_00("00","Approved or Completed Successfully" , SUCCESS),

    NIP_01("01","Status unknown, please wait for settlement report" , PENDING),

    NIP_03("03","Invalid Sender" , FAILED),

    NIP_05("05","Do not honor" ,FAILED),

    NIP_06("06","Dormant Account",FAILED),

    NIP_07("07","Invalid Account",FAILED),

    NIP_08("08","Account Name Mismatch",FAILED),

    NIP_09("09","Request processing in progress" , PENDING),

    NIP_12("12","Invalid transaction",FAILED),

    NIP_13("13","Invalid Amount",FAILED),

    NIP_14("14","Invalid Batch Number",FAILED),

    NIP_15("15","Invalid Session or Record ID",PENDING),

    NIP_16("16","Unknown Bank Code",FAILED),

    NIP_17("17","Invalid Channel",FAILED),

    NIP_18("18","Wrong Method Call",FAILED),

    NIP_21("21","No action taken" , PENDING),

    NIP_25("25","Unable to locate record",PENDING),

    NIP_26("26","Duplicate record",FAILED),

    NIP_30("30","Format error",FAILED),

    NIP_34("34","Suspected fraud",FAILED),

    NIP_35("35","Contact sending bank",PENDING),

    NIP_51("51","No sufficient funds",FAILED),

    NIP_57("57","Transaction not permitted to sender",FAILED),

    NIP_58("58","Transaction not permitted on channel",FAILED),

    NIP_61("61","Transfer limit Exceeded",FAILED),

    NIP_63("63","Security violation",FAILED),

    NIP_65("65","Exceeds withdrawal frequency",FAILED),

    NIP_68("68","Response received too late",PENDING),

    NIP_69("69","Unsuccessful Account/Amount block",FAILED),

    NIP_70("70","Unsuccessful Account/Amount unblock",FAILED),

    NIP_71("71","Empty Mandate Reference Number",FAILED),

    NIP_91("91","Beneficiary Bank not available",FAILED),

    NIP_92("92","Routing error",FAILED),

    NIP_94("94","Duplicate transaction",FAILED),

    NIP_96("96","System malfunction",FAILED),

    NIP_97("97","Timeout waiting for response from destination" , FAILED),

    NIP_201("201","",FAILED),

    NIP_202("202","",PENDING),

    NIP_103("103","Unknown Response Code  , Kindly contact the administrator" , PENDING);

    @Getter
    private final String code;

    @Getter
    private final String description;

    @Getter
    private final PaymentStatusEnum paymentStatusEnum;


    public static NIPResponseCodeEnum getResponseCodeEnum(String code){
        return Stream.of(NIPResponseCodeEnum.values())
                .filter(nipResponseCodeEnum -> nipResponseCodeEnum.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(NIP_103);
    }

    public static boolean isSuccess(String code){
        return StringUtils.equalsIgnoreCase(NIP_00.code, code);
    }

    public static PaymentStatusEnum getPaymentStatusEnum(String code){
        return Stream.of(NIPResponseCodeEnum.values())
                .filter(nipResponseCodeEnum -> nipResponseCodeEnum.getCode().equalsIgnoreCase(code))
                .map(nipResponseCodeEnum -> nipResponseCodeEnum.getPaymentStatusEnum())
                .findFirst()
                .orElse(NIP_103.getPaymentStatusEnum());
    }
}
