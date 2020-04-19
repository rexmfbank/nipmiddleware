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

    NIP_00("NIP_00","Approved or Completed Successfully" , SUCCESS),

    NIP_01("NIP_01","Status unknown, please wait for settlement report" , PENDING),

    NIP_03("NIP_03","Invalid Sender" , FAILED),

    NIP_05("NIP_05","Do not honor" ,FAILED),

    NIP_06("NIP_06","Dormant Account",FAILED),

    NIP_07("NIP_07","Invalid Account",FAILED),

    NIP_08("NIP_08","Account Name Mismatch",FAILED),

    NIP_09("NIP_09","Request processing in progress" , PENDING),

    NIP_12("NIP_12","Invalid transaction",FAILED),

    NIP_13("NIP_13","Invalid Amount",FAILED),

    NIP_14("NIP_14","Invalid Batch Number",FAILED),

    NIP_15("NIP_15","Invalid Session or Record ID",FAILED),

    NIP_16("NIP_16","Unknown Bank Code",FAILED),

    NIP_17("NIP_17","Invalid Channel",FAILED),

    NIP_18("NIP_18","Wrong Method Call",FAILED),

    NIP_21("NIP_21","No action taken" , PENDING),

    NIP_25("NIP_25","Unable to locate record",FAILED),

    NIP_26("NIP_26","Duplicate record",FAILED),

    NIP_30("NIP_30","Format error",FAILED),

    NIP_34("NIP_34","Suspected fraud",FAILED),

    NIP_35("NIP_35","Contact sending bank",PENDING),

    NIP_51("NIP_51","No sufficient funds",FAILED),

    NIP_57("NIP_57","Transaction not permitted to sender",FAILED),

    NIP_58("NIP_58","Transaction not permitted on channel",FAILED),

    NIP_61("NIP_61","Transfer limit Exceeded",FAILED),

    NIP_63("NIP_63","Security violation",FAILED),

    NIP_65("NIP_65","Exceeds withdrawal frequency",FAILED),

    NIP_68("NIP_68","Response received too late",PENDING),

    NIP_69("NIP_69","Unsuccessful Account/Amount block",FAILED),

    NIP_70("NIP_70","Unsuccessful Account/Amount unblock",FAILED),

    NIP_71("NIP_71","Empty Mandate Reference Number",FAILED),

    NIP_91("NIP_91","Beneficiary Bank not available",FAILED),

    NIP_92("NIP_92","Routing error",FAILED),

    NIP_94("NIP_94","Duplicate transaction",FAILED),

    NIP_96("NIP_96","System malfunction",FAILED),

    NIP_97("NIP_97","Timeout waiting for response from destination" , PENDING),

    NIP_98("NIP_98"," Invalid Http Client Error ",FAILED),

    NIP_99("NIP_99","Service Timeout Error " , PENDING),

    NIP_100("NIP_100","Application Error : One or More Fields is empty or has an incorrect value ",FAILED),

    NIP_101("NIP_101","Application Error : A database constraint has been violated ",FAILED),

    NIP_102("NIP_102","Application Error : Invalid Json Payload ",FAILED),

    NIP_103("NIP_103","Unknown Response Code  , Kindly contact the administrator" , PENDING),

    NIP_104("NIP_104","Application Error : Incorrect Destination Account BVN",FAILED),

    NIP_105("NIP_105","Application Error : Name Enquiry on Destination Account Failed",FAILED),

    NIP_106("NIP_106","Application Error : No response from NIBSS" , PENDING),

    NIP_107("NIP_107","Application Error : Transaction could not be completed", PENDING),

    NIP_108("NIP_108","Application Error :  Payment Reference Not Unique", FAILED),

    NIP_109("NIP_109","Authorization  Error :  Unauthorised access to resource", FAILED),

    NIP_110("NIP_110","Authentication Error :  Invalid signature sent", FAILED),

    NIP_111("NIP_111","Authentication Error :  Access token not sent in request", FAILED),

    NIP_112("NIP_112","Authentication Error :  Invalid signature sent", FAILED),

    NIP_113("NIP_113","Authorization Error :  Could not connect to resource", FAILED),

    NIP_114("NIP_114","Application  Error :  Client Details already exist in the database", FAILED),

    NIP_115("NIP_115","Authentication  Error :  Client ID not sent ", FAILED),

    NIP_116("NIP_116","Authentication  Error :  Invalid Client ID  ", FAILED),

    NIP_117("NIP_117","Authentication Error :  User token not sent in request", FAILED),

    NIP_118("NIP_118","Authentication Error :  User token has expired", FAILED),

    NIP_119("NIP_119","Authentication Error :  Invalid Token sent in the request", FAILED),
    NIP_120("NIP_120","Authentication Error :  Access forbidden to User", FAILED),
    NIP_121("NIP_121","Authentication Error :  Invalid Authorization Sent", FAILED),
    NIP_122("NIP_122","Authentication Error :  Invalid Authentication details found", FAILED),
    NIP_123("NIP_123","Authentication Error :  Invalid Authorization Sent; username or password not found", FAILED),
    NIP_124("NIP_124","Authentication Error :  Client not found", FAILED),
    NIP_125("NIP_125","Authentication Error :  Invalid Client Secret Key", FAILED),
    NIP_126("NIP_126","Authentication Error :  Invalid authentication", FAILED),
    NIP_127("NIP_127","Authentication Error :  Client Id does not match authenticated value", FAILED)
    ;



    @Getter
    private final String code;

    @Getter
    private final String description;

    @Getter
    private PaymentStatusEnum paymentStatusEnum;


    public static NIPResponseCodeEnum getResponseDescription(String code){
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
