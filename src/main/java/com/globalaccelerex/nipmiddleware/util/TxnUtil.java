package com.globalaccelerex.nipmiddleware.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TxnUtil {

    public static Map<String,Boolean> txnFlag = new HashMap<>();

    static {
        txnFlag.put("flag", true);
    }

   public static String TXN_SUSPENDED_MSG = "Transactions to NIBSS Temporarily suspended ,Contact the Administrator";

    public static String FLAG = "flag";
}
