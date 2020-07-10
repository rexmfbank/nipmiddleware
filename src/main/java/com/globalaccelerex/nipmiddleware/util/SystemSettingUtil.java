package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.service.db.SystemSettingDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemSettingUtil {

    public static String UP_STATUS = "UP";

    public static String DOWN_STATUS = "DOWN";

    public static String CALL_NIBSS_API ="CALL_NIBSS_API";



    private final SystemSettingDbService systemSettingDbService;

    @Autowired
    public SystemSettingUtil(SystemSettingDbService systemSettingDbService) {
        this.systemSettingDbService = systemSettingDbService;
    }

    //@TODO remove this method going forward
    public void changeStatus(String name , String value){
        //systemSettingDbService.updateServiceStatus(name, value);
    }

    //@TODO remove this check going forward
    public boolean isNibssStatusDown(){
        //return StringUtils.equalsIgnoreCase(systemSettingDbService.findStatus(CALL_NIBSS_API).getValue(),DOWN_STATUS);
        return false;
    }


}
