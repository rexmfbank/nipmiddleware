package com.globalaccelerex.nipmiddleware.messaging;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueuePayload {

    private String clientId;

    private String sessionId;

    private int waitDuration ;

    private QueueMode mode ;

    private boolean reQueue ;
}
