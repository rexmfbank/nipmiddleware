
package com.globalaccelerex.nipmiddleware.logging.impl;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsData {
    private long endTime;
    private long startTime;
    private String id;
    private String requestURI;
    private String response;
    private long duration; 
}
