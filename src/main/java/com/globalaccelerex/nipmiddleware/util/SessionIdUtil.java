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

    @Autowired
    public SessionIdUtil(NipConfig nipConfig) {
        this.nipConfig = nipConfig;
    }

    public String generateSessionId(){
        val builder = new StringBuilder();
        builder.append(nipConfig.getSenderBankCode())
                .append(DateFormatUtils.format(new Date(),nipConfig.getDateTimeFormat()))
                .append(RandomStringUtils.randomNumeric(nipConfig.getSessionIdLength()));
        return builder.toString();
    }
}
