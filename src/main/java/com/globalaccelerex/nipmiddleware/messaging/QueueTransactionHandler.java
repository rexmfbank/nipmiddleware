package com.globalaccelerex.nipmiddleware.messaging;

import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueTransactionHandler {

    private final TsqHandlerService tsqHandlerService;

    @Autowired
    public QueueTransactionHandler(TsqHandlerService tsqHandlerService) {
        this.tsqHandlerService = tsqHandlerService;
    }

    public QueuePayload handlePayload(IMarker marker,QueuePayload queuePayload){
        switch (queuePayload.getMode()) {
            case CALLBACK:
                return tsqHandlerService.handleTsq(marker, queuePayload);
            default:
                return queuePayload;
        }
    }
}
