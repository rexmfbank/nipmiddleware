
package com.globalaccelerex.nipmiddleware.logging.api;

public interface IRequestLogger {

    void setRequest(String requestURI, String requestBody);

    void setResponse(String response);

    void setMainRequest(String requestURI, String requestBody, boolean isSecuredRequest);

    void setMainResponse(String response, boolean isSecuredResponse);

    void showSummary();

    String getSummary();

    void setIsSecuredRequest(boolean isSecuredRequest);

    void setIsSecuredResponse(boolean isSecuredResponse);
}
