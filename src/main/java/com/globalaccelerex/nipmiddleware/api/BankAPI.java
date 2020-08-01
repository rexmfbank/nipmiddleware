package com.globalaccelerex.nipmiddleware.api;

public interface BankAPI {

    String MOCK_CBA_API ="/api/nipmiddleware/v1/cba";

    String NAME_ENQUIRY_API = "/nameEnquiry";

    String FI_LIST_API ="/fI";

    String TSQ_API= "/tsq";

    String BALANCE_ENQUIRY_API ="/balanceEnquiry/{originatingInstitutionCode}";

    String MANDATE_ADVICE_API = "/mandateAdvice/{originatingInstitutionCode}";

    String ACCOUNT_BLOCK_API = "/accountBlock/{originatingInstitutionCode}";

    String ACCOUNT_UNBLOCK_API = "/accountUnblock/{originatingInstitutionCode}";

    String AMOUNT_BLOCK_API = "/amountBlock/{originatingInstitutionCode}";

    String AMOUNT_UNBLOCK_API = "/amountUnblock/{originatingInstitutionCode}";

    String FT_DIRECT_CREDIT_API = "/ftDirectCredit/{originatingInstitutionCode}";

    String FT_DIRECT_DEBIT_API = "/ftDirectDebit/{originatingInstitutionCode}";

    String FT_ADVICE_DIRECT_CREDIT_API = "/ftAdviceDirectCredit/{originatingInstitutionCode}";

    String FT_ADVICE_DIRECT_DEBIT_API = "/ftAdviceDirectDebit/{originatingInstitutionCode}";
}
