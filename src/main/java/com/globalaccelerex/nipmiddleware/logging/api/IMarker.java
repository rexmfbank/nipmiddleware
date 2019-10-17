package com.globalaccelerex.nipmiddleware.logging.api;



public interface IMarker extends ILogging , IRequestLogger {

	/**
     * 
     */
    void start();
	/**
     * 
     */
    void done();
    
    

    /**
     * @return
     */
    String getID();    

    public static final String IDENTIFIER = "marker";
}
