package com.globalaccelerex.nipmiddleware.logging.impl;



import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Marker implements IMarker {

    private long endTime;
    private long startTime;
    private long requestStartTime;
    private long requestEndTime;
    private final String id;
    private String mainRequest;
    private String mainResponse;
    private String request;
    private String response;
    private boolean isSecuredRequest = false;
    private boolean isSecuredResponse = false;
    private static LoadingCache<String, IMarker> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats()
            .build(new CacheLoader<String, IMarker>() {
                @Override
                public IMarker load(String id) {
                    return getMarker( id) ;
                }
            });
     
    private final List<StatisticsData> statisticsData = new ArrayList();

    private Marker() {
        this.id = Long.toString(System.nanoTime());
        start();
    }

    private Marker(String id) {
        this.id = Long.toString(System.nanoTime())+"_"+id;
        start();
    }

    private static Marker getMarker(String id){
        return StringUtils.isBlank(id) ? new Marker() : new Marker(id);
    }
    
    public static IMarker fromString(){
        return getMarker("");
    }
    public static IMarker fromString(String id) {
        try{
           return  StringUtils.isBlank(id) ? new Marker() : cache.getUnchecked(StringUtils.defaultString(id)) ;
        }catch (UncheckedExecutionException ex){
            
        }
        return null ; 
    }

    public static IMarker fromMap(String id) {
        return fromString(id);
    }

    @Override
    public void setIsSecuredRequest(boolean isSecuredRequest) {
        this.isSecuredRequest = isSecuredRequest;
    }

    @Override
    public void setIsSecuredResponse(boolean isSecuredResponse) {
        this.isSecuredResponse = isSecuredResponse;
    }

    @Override
    public void setMainRequest(String requestURI, String requestBody, boolean isSecuredRequest) {
        if (isSecuredRequest) {
            this.mainRequest = String.format("%s => ****** ", requestURI);
        } else {
            this.mainRequest = String.format("%s => %s ", requestURI, requestBody);
        }
    }

    @Override
    public void setMainResponse(String response, boolean isSecuredResponse) {
        if (isSecuredResponse) {
            this.mainResponse = "******";
        } else {
            this.mainResponse = response;
        }
    }

    @Override
    public void setRequest(String requestURL, String requestBody) {
        if (isSecuredRequest) {
            this.request = String.format("%s => ****** ", requestURL);
        } else {
            this.request = String.format("%s => %s ", requestURL, requestBody);
        }
        requestStartTime = System.currentTimeMillis();
    }

    @Override
    public void setResponse(String response) {
        if (isSecuredResponse) {
            this.response = "******";
        } else {
            this.response = response;
        }
        requestEndTime = System.currentTimeMillis();
        StatisticsData data = StatisticsData.builder()
                .endTime(requestEndTime)
                .id(this.id)
                .requestURI(this.request)
                .response(this.response)
                .startTime(requestStartTime)
                .duration(requestEndTime - requestStartTime)
                .build();
        statisticsData.add(data);
        clearStatisticsData();
    }

    private void clearStatisticsData() {
        this.request = null;
        this.response = null;
        isSecuredResponse = false;
        isSecuredRequest = false;
    }

    @Override
    public void showSummary() {
        info("STATS => " + getSummary());
    }

    @Override
    public String getSummary() {
        return MarkerData.builder()
                .duration(this.endTime - this.startTime)
                .endTime(endTime)
                .id(id)
                .startTime(startTime)
                .statisticsData(statisticsData)
                .mainRequest(mainRequest)
                .mainResponse(mainResponse)
                .build().toString();
    }

    @Override
    public String toString() {
        return getSummary(); 
    }
    
    @Override
    public void trace(String message) {
        if (log.isTraceEnabled()) {
            log.trace(MarkerLogFormat.MESSAGE.getFormat(), //
                    getStackTraceElement().getClassName(),
                    getStackTraceElement().getMethodName(),
                    getStackTraceElement().getLineNumber(), //
                    this.id,
                    message);
        }
    }

    @Override
    public void trace(String message, Throwable exception) {
        if (log.isTraceEnabled()) {
            log.trace(MarkerLogFormat.MESSAGE_THROWABLE.getFormat(), //
                    getStackTraceElement().getClassName(), getStackTraceElement().getMethodName(), getStackTraceElement().getLineNumber(), //
                    this.id, message, exception);
        }
    }

    @Override
    public void debug(String message) {
        if (log.isDebugEnabled()) {
            log.debug(MarkerLogFormat.MESSAGE.getFormat(), //
                    getStackTraceElement().getClassName(),
                    getStackTraceElement().getMethodName(),
                    getStackTraceElement().getLineNumber(), //
                    this.id,
                    message);
        }
    }

    @Override
    public void debug(String message, Throwable exception) {
        if (log.isDebugEnabled()) {
            log.debug(MarkerLogFormat.MESSAGE_THROWABLE.getFormat(), //
                    getStackTraceElement().getClassName(), getStackTraceElement().getMethodName(), getStackTraceElement().getLineNumber(), //
                    this.id, message, exception);
        }
    }

    @Override
    public void debug(Throwable throwable) {
        if (log.isDebugEnabled()) {
            log.debug(MarkerLogFormat.MESSAGE.getFormat(), //
                    getStackTraceElement().getClassName(), getStackTraceElement().getMethodName(), getStackTraceElement().getLineNumber(), //
                    this.id, throwable);
        }
    }

    @Override
    public void info(String message) {
        if (log.isInfoEnabled()) {
            log.info(MarkerLogFormat.MESSAGE.getFormat(), //
                    getStackTraceElement().getClassName(), getStackTraceElement().getMethodName(), getStackTraceElement().getLineNumber(), //
                    this.id, message);
        }

    }

    @Override
    public void info(String message, Throwable throwable) {
        if (log.isInfoEnabled()) {
            log.info(MarkerLogFormat.MESSAGE_THROWABLE.getFormat(), //
                    getStackTraceElement().getClassName(), getStackTraceElement().getMethodName(), getStackTraceElement().getLineNumber(), //
                    this.id, message, throwable);
        }

    }

    @Override
    public void info(Throwable throwable) {
        if (log.isInfoEnabled()) {
            log.info(MarkerLogFormat.MESSAGE.getFormat(), //
                    getStackTraceElement().getClassName(), getStackTraceElement().getMethodName(), getStackTraceElement().getLineNumber(), //
                    this.id, throwable);
        }

    }

    @Override
    public boolean isDebug() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isInfo() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTrace() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public final void start() {
        this.startTime = System.currentTimeMillis(); 
        cache.put(id, this);
    }

    @Override
    public void done() {
        this.endTime = System.currentTimeMillis();
        showSummary();
        cache.invalidate(id);
        this.request = null;
        this.response = null;
    }

    @Override
    public String getID() {
        return this.id;
    }

    private StackTraceElement getStackTraceElement() {
        StackTraceElement ste = null;
        final StackTraceElement[] stArr = new RuntimeException().getStackTrace();
        if (stArr.length >= 3) {
            ste = stArr[2]; //Position in the stacktrace for the original call to transaction
        } else {
            //Can occure in some JVM´s that Stacktrace is emtpy 
            ste = new StackTraceElement(Marker.class.getSimpleName(), "getStackTraceElement", this.getClass().getName(), 0);
        }
        return ste;
    }

    public enum MarkerLogFormat {
        MESSAGE("[{}.{}():{}] Transaction ({}) {}"), //
        THROWABLE("[{}.{}():{}] Transaction ({})  {}"), //
        MESSAGE_THROWABLE("[{}.{}():{}] Transaction ({}) {} {}"); //

        String format;

        MarkerLogFormat(final String format) {
            this.format = format;
        }

        public String getFormat() {
            return this.format;
        }
    }

}