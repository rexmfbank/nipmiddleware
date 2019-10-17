
package com.globalaccelerex.nipmiddleware.logging.impl;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class MarkerData {
    private long endTime;
    private long startTime;
    private String id;
    private List<StatisticsData> statisticsData;
    private long duration; 
    private String mainRequest;
    private String mainResponse;
}