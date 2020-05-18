package com.globalaccelerex.nipmiddleware.messaging;

import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QueueTransactionHandler {

    private ClientCallbackService clientCallbackService;

    @Autowired
    public void setClientCallbackService(ClientCallbackService clientCallbackService) {
        this.clientCallbackService = clientCallbackService;
    }

    public QueuePayload handlePayload(IMarker marker, QueuePayload queuePayload){
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
