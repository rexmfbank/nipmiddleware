package com.globalaccelerex.nipmiddleware.api;

public interface BankAPI {

    String MOCK_CBA_API ="/api/nipmiddleware/v1/cba";

    String NAME_ENQUIRY_API = "/nameEnquiry";

    String FI_LIST_API ="/fI";

    String TSQ_API= "/tsq";

    String BALANCE_ENQUIRY_API ="/balanceEnquiry";

    String MANDATE_ADVICE_API = "/mandateAdvice";

    String ACCOUNT_BLOCK_API = "/accountBlock";

    String ACCOUNT_UNBLOCK_API = "/accountUnblock";

    String AMOUNT_BLOCK_API = "/amountBlock";

    String AMOUNT_UNBLOCK_API = "/amountUnblock";

    String FT_DIRECT_CREDIT_API = "/ftDirectCredit";

    String FT_DIRECT_DEBIT_API = "/ftDirectDebit";

    String FT_ADVICE_DIRECT_CREDIT_API = "/ftAdviceDirectCredit";

    String FT_ADVICE_DIRECT_DEBIT_API = "/ftAdviceDirectDebit";
}
