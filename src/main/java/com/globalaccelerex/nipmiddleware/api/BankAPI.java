package com.globalaccelerex.nipmiddleware.api;

public interface BankAPI {

    String MOCK_CBA_API ="/api/nipmiddleware/v1/cba";

    String NAME_ENQUIRY_API = "/nameEnquiry";

    String FI_LIST_API ="/fiList";

    String TSQ_API= "/tsq/{sessionId}";

    String BALANCE_ENQUIRY_API ="/balanceEnquiry";

    String MANDATE_ADVICE_API = "/mandateAdvice";

    String ACCOUNT_BLOCK_API = "/accountBlock";

    String ACCOUNT_UNBLOCK_API = "/accountUnBlock";

    String AMOUNT_BLOCK_API = "/amountBlock";

    String AMOUNT_UNBLOCK_API = "/amountUnBlock";

    String FT_DIRECT_CREDIT_API = "/ft/dc";

    String FT_DIRECT_DEBIT_API = "/ft/dd";

    String FT_ADVICE_DIRECT_CREDIT_API = "/ftAdvice/dc";

    String FT_ADVICE_DIRECT_DEBIT_API = "/ftAdvice/dd";
}
