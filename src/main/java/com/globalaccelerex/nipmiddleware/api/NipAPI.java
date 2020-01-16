package com.globalaccelerex.nipmiddleware.api;

public interface NipAPI {

    String NAME_ENQUIRY_OUTWARD = "/nameenquirysingleitemRequest";

    String FUNDS_TRANSFER_OUTWARD= "/fundtransfersingleitem_dcRequest";

    String TSQ_OUTWARD = "/txnstatusquerysingleitemRequest";

    String INWARD_TARGET_NAMESPACE = "http://core.nip.nibss/";

    String PORT_TYPE_NAME = "nipInwardPort";

    String LOCATION_URI = "/api/nipmiddleware/ws";

    String URL_MAPPINGS = "/api/nipmiddleware/ws/*";

    String FI_LIST_REQUEST = "financialinstitutionlist";

    String NAME_ENQUIRY_REQUEST = "nameenquirysingleitem";

    String FT_DIRECT_CREDIT_REQUEST = "fundtransfersingleitem_dc";

    String FT_DIRECT_DEBIT_REQUEST = "fundtransfersingleitem_dd";

    String TSQ_REQUEST="txnstatusquerysingleitem";

    String BALANCE_ENQUIRY_REQUEST = "balanceenquiry";

    String FT_ADVICE_DIRECT_CREDIT_REQUEST ="fundtransferAdvice_dc";

    String FT_ADVICE_DIRECT_DEBIT_REQUEST ="fundtransferAdvice_dd";

    String AMOUNT_BLOCK_REQUEST = "amountblock";

    String AMOUNT_UNBLOCK_REQUEST = "amountunblock";

    String ACCOUNT_BLOCK_REQUEST = "accountblock";

    String ACCOUNT_UNBLOCK_REQUEST = "accountunblock";

    String MANDATE_ADVICE_REQUEST = "mandateadvice";

    String FT_CREDIT_ACKNOWLEDGEMENT_REQUEST ="ftackcreditrequest";
}
