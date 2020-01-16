package com.globalaccelerex.nipmiddleware.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public enum ChannelCodesEnum {

    CC_1(1,"BANK TELLER"),

    CC_2(2,"INTERNET BANKING"),

    CC_3(3,"MOBILE PHONES"),

    CC_4(4,"POS TERMINALS"),

    CC_5(5,"ATM"),

    CC_6(6,"VENDOR MERCHANT PORTAL"),

    CC_7(7,"THIRD-PARTY PAYMENT PLATFORM"),

    CC_8(8,"USSD"),

    CC_9(9,"OTHER CHANNELS"),

    CC_10(10,"SOCIAL MEDIA"),

    CC_11(11,"AGENCY BANKING");

    @Getter
    private final int code;

    @Getter
    private final String channelName;
}
