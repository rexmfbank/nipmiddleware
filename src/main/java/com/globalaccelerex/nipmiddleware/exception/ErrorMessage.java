package com.globalaccelerex.nipmiddleware.exception;

public interface ErrorMessage {

    String TXN_SUSPENDED_MSG ="Transactions To NIBSS are temporarily suspended";

    String PAYMENT_REFERENCE_EXISTS_MSG = "Application Error :  Payment Reference Already Exists";

    String EMPTY_FIELD_MSG = "Application Error : One or More Fields is empty or has an incorrect value ";

    String INVALID_FORMAT_MSG = "Application Error : One or More Fields has an invalid format";

    String NAME_ENQUIRY_FAILED_MSG = "Application Error : Name Enquiry on Destination Account Failed";

    String RECORD_NOT_FOUND_MSG = "Application Error :  Record Not Found in Database ";

    String NO_RESPONSE_FROM_NIBSS_MSG = "Application Error : No response from NIBSS";

    String TRANSACTION_NOT_COMPLETED_MSG = "Application Error : Transaction could not be completed";

    String CLIENT_NOT_FOUND_MSG = "Application Error :  Client not found";

    String UNAUTHORIZED_ACCESS_MSG = "Authorization  Error :  Unauthorised access to resource";

    String INVALID_CLIENT_SECRET_KEY_MSG = "Authentication Error :  Invalid Client Secret Key";

    String CONNECTION_ERROR_MSG = "Authorization Error :  Could not connect to resource";

    String ERROR_OCCURRED_MSG = "An Error has occurred ";

    String INVALID_SIGNATURE_MSG = "Authentication Error :  Invalid signature sent";

    String ACCESS_TOKEN_NOT_SENT_MSG = "Authentication Error :  Access token not sent in request";

    String USER_ACCESS_FORBIDDEN_MSG = "Authentication Error :  Access forbidden to User";

    String WEB_SERVICE_ERROR_MSG ="Error occurred while connecting to NIBSS WebService URL";

    String AUTHORIZATION_HEADER_NOT_SENT_MSG = "Authentication Error :   Authorization Header not  Sent";

    String INVALID_AUTHORIZATION_HEADER_MSG = "Authentication Error :  Invalid Authorization Sent";

    String NEW_AND_CONFIRM_PASSWORD_NOT_SAME_MSG = "Application Error :  New Password and Confirm Password are not the same ";

    String PASSWORD_MATCH_ERROR_MSG = "Application Error :  Password does not match ";

    String CLIENT_DETAILS_EXIST_IN_DB_MSG = "Application  Error :  Client Details already exist in the database";

    String INVALID_AUTHENTICATION_MSG = "Authentication Error :  Invalid authentication";

    String CLIENT_ID_NOT_MATCHING_MSG = "Authentication Error :  Client Id does not match authenticated value";
}
