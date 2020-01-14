package com.globalaccelerex.nipmiddleware.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
public enum NIPResponseCodeEnum {

    NIP_00("00","Approved or Completed Successfully" ),

    NIP_01("01","Status unknown, please wait for settlement report"),

    NIP_03("03","Invalid Sender"),

    NIP_05("05","Do not honor"),

    NIP_06("06","Dormant Account"),

    NIP_07("07","Invalid Account"),

    NIP_08("08","Account Name Mismatch"),

    NIP_09("09","Request processing in progress"),

    NIP_12("12","Invalid transaction"),

    NIP_13("13","Invalid Amount"),

    NIP_14("14","Invalid Batch Number"),

    NIP_15("15","Invalid Session or Record ID"),

    NIP_16("16","Unknown Bank Code"),

    NIP_17("17","Invalid Channel"),

    NIP_18("18","Wrong Method Call"),

    NIP_21("21","No action taken"),

    NIP_25("25","Unable to locate record"),

    NIP_26("26","Duplicate record"),

    NIP_30("30","Format error"),

    NIP_34("34","Suspected fraud"),

    NIP_35("35","Contact sending bank"),

    NIP_51("51","No sufficient funds"),

    NIP_57("57","Transaction not permitted to sender"),

    NIP_58("58","Transaction not permitted on channel"),

    NIP_61("61","Transfer limit Exceeded"),

    NIP_63("63","Security violation"),

    NIP_65("65","Exceeds withdrawal frequency"),

    NIP_68("68","Response received too late"),

    NIP_69("69","Unsuccessful Account/Amount block"),

    NIP_70("70","Unsuccessful Account/Amount unblock"),

    NIP_71("71","Empty Mandate Reference Number"),

    NIP_91("91","Beneficiary Bank not available"),

    NIP_92("92","Routing error"),

    NIP_94("94","Duplicate transaction"),

    NIP_96("96","System malfunction"),

    NIP_97("97","Timeout waiting for response from destination"),

    NIP_98("98"," Invalid Http Client Error "),

    NIP_99("99","Service Timeout Error "),

    NIP_100("100","Application Error : One or More Fields is empty or has an incorrect value "),

    NIP_101("101","Application Error : A database constraint has been violated "),

    NIP_102("102","Application Error : Invalid Json Payload "),

    NIP_103("103","Unknown Response Code  , Kindly contact the administrator"),

    NIP_104("104","Application Error : Incorrect Destination Account BVN"),

    NIP_105("105","Application Error : Name Enquiry on Destination Account Failed"),

    NIP_106("106","Application Error : No response from NIBSS"),

    NIP_107("107","Application Error : Transaction could not be completed");;


    @Getter
    private final String code;

    @Getter
    private final String description;


    public static NIPResponseCodeEnum getResponseDescription(String code){
        return Stream.of(NIPResponseCodeEnum.values())
                .filter(nipResponseCodeEnum -> nipResponseCodeEnum.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(NIP_103);
    }

    public static boolean isSuccess(String code){
        return StringUtils.equalsIgnoreCase(NIP_00.code, code);
    }

}
