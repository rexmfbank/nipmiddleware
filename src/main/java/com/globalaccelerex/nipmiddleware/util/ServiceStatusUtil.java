package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.service.db.ServiceStatusDbService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceStatusUtil {

    public static String UP_STATUS = "UP";

    public static String DOWN_STATUS = "DOWN";

    public static String CALL_NIBSS_API ="CALL_NIBSS_API";

    public static String TXN_SUSPENDED_MSG ="Transactions To NIBSS are temporarily suspended";

    private final ServiceStatusDbService serviceStatusDbService;

    @Autowired
    public ServiceStatusUtil(ServiceStatusDbService serviceStatusDbService) {
        this.serviceStatusDbService = serviceStatusDbService;
    }

    public void changeStatus(String name , String value){
        serviceStatusDbService.updateServiceStatus(name, value);
    }

    public boolean isNibssStatusUp(){
        return StringUtils.equalsIgnoreCase(serviceStatusDbService.findStatus(CALL_NIBSS_API).getValue(),UP_STATUS);
    }
}
