package com.globalaccelerex.nipmiddleware.api;

public interface ClientAPI {

    String OUTWARD_API = "/api/nipmiddleware/v1/outward";

    String CLIENT_API = "/api/nipmiddleware/v1/client";

    String NAME_ENQUIRY="/nameEnquiry";

    String FUNDS_TRANSFER="/ft";

    String TSQ = "/tsq";

    String ADMIN_API = "/api/nipmiddleware/v1/admin";

    String CREATE_CLIENT = "/createClient";

    String UPDATE_CLIENT = "/updateClient";

    String GET_CLIENT = "/getClient/{clientId}";

    String GET_CLIENTS= "/clients";

    String PATH_VARIABLE_CLIENT_ID = "clientId";

    String UPDATE_PASSWORD = "/updatePassword";

    String RESET_PASSWORD = "/resetPassword";


}
