package com.globalaccelerex.nipmiddleware.messaging;

import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.util.TxnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.util.TxnUtil.FLAG;

@Service
public class QueueTransactionHandler {

    private final ClientCallbackService clientCallbackService;

    @Autowired
    public QueueTransactionHandler(ClientCallbackService clientCallbackService) {
        this.clientCallbackService = clientCallbackService;
    }

    public QueuePayload handlePayload(IMarker marker,QueuePayload queuePayload){
        switch (queuePayload.getMode()) {
            case TSQ:
                    if(TxnUtil.txnFlag.get(FLAG)){
                        return clientCallbackService.handleTsq(marker, queuePayload);
                    }else{
                        return queuePayload;
                    }
            case CALLBACK:
                return clientCallbackService.handleCallback(marker, queuePayload);
            default:
                return queuePayload;
        }
    }
}
