package com.globalaccelerex.nipmiddleware.messaging;

import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.util.ServiceStatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QueueTransactionHandler {

    private final ClientCallbackService clientCallbackService;

    private final ServiceStatusUtil serviceStatusUtil;

    @Autowired
    public QueueTransactionHandler(ClientCallbackService clientCallbackService, ServiceStatusUtil serviceStatusUtil) {
        this.clientCallbackService = clientCallbackService;
        this.serviceStatusUtil = serviceStatusUtil;
    }

    public QueuePayload handlePayload(IMarker marker,QueuePayload queuePayload){
        switch (queuePayload.getMode()) {
            case TSQ:
                return clientCallbackService.handleTsq(marker, queuePayload);
            case CALLBACK:
                return clientCallbackService.handleCallback(marker, queuePayload);
            default:
                return queuePayload;
        }
    }
}
