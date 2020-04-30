package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class SessionIdUtil {

    private final NipConfig nipConfig;

    String DATE_TIME_FORMAT = "yyMMddHHmmss";

    int SESSION_ID_LENGTH = 12;


    @Autowired
    public SessionIdUtil(NipConfig nipConfig) {
        this.nipConfig = nipConfig;
    }

    public String generateSessionId(String originatorBankCode){
        val builder = new StringBuilder();
        builder.append(originatorBankCode)
                .append(DateFormatUtils.format(new Date(),DATE_TIME_FORMAT))
                .append(RandomStringUtils.randomNumeric(SESSION_ID_LENGTH));
        return builder.toString();
    }
}
