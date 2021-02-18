package com.globalaccelerex.nipmiddleware.api;

public interface NipAPI {

    String NAME_ENQUIRY_OUTWARD = "/nameenquirysingleitemRequest";

    String FUNDS_TRANSFER_OUTWARD= "/fundtransfersingleitem_dcRequest";

    String TSQ_OUTWARD = "/txnstatusquerysingleitemRequest";

    String INWARD_TARGET_NAMESPACE = "http://core.nip.nibss/";

    String PORT_TYPE_NAME = "nipInwardPort";

    String INWARD_WS_URI = "/api/nipmiddleware/v1/inward/ws/";

    String URL_MAPPINGS = "/api/nipmiddleware/v1/inward/ws/*";

    String FI_LIST_REQUEST = "financialinstitutionlistRequest";

    String NAME_ENQUIRY_REQUEST = "nameenquirysingleitemRequest";

    String FT_DIRECT_CREDIT_REQUEST = "fundtransfersingleitem_dcRequest";

    String FT_DIRECT_DEBIT_REQUEST = "fundtransfersingleitem_ddRequest";

    String TSQ_REQUEST="txnstatusquerysingleitemRequest";

    String BALANCE_ENQUIRY_REQUEST = "balanceenquiryRequest";

    String FT_ADVICE_DIRECT_CREDIT_REQUEST ="fundtransferAdvice_dcRequest";

    String FT_ADVICE_DIRECT_DEBIT_REQUEST ="fundtransferAdvice_ddRequest";

    String AMOUNT_BLOCK_REQUEST = "amountblockRequest";

    String AMOUNT_UNBLOCK_REQUEST = "amountunblockRequest";

    String ACCOUNT_BLOCK_REQUEST = "accountblockRequest";

    String ACCOUNT_UNBLOCK_REQUEST = "accountunblockRequest";

    String MANDATE_ADVICE_REQUEST = "mandateadviceRequest";

    String FT_CREDIT_ACKNOWLEDGEMENT_REQUEST ="ftackcreditRequest";
}
