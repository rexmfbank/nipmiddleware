package com.globalaccelerex.nipmiddleware.logging.api;

public interface ILogging {

    /**
     * @param message
     */
    void trace(String message);
    /**
     * @param message
     * @param exception
     */
    void trace(String message, Throwable exception);
    
    /**
     * @param message
     */
    void debug(String message);

    /**
     * @param message
     * @param exception
     */
    void debug(String message, Throwable exception);

    /**
     * @param throwable
     */
    void debug(Throwable throwable);

    /**
     * @param message
     */
    void info(String message);

    /**
     * @param message
     * @param throwable
     */
    void info(String message, Throwable throwable);

    /**
     * @param throwable
     */
    void info(Throwable throwable);

    /**
     * @return
     */
    boolean isDebug();

    /**
     * @return
     */
    boolean isInfo();

    /**
     * @return
     */
    boolean isTrace();

    /**
     * @return
     */
    

}
