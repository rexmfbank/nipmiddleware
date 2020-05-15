package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.util.ServiceStatusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.globalaccelerex.nipmiddleware.api.ClientAPI.*;
import static com.globalaccelerex.nipmiddleware.util.ServiceStatusUtil.DOWN_STATUS;
import static com.globalaccelerex.nipmiddleware.util.ServiceStatusUtil.UP_STATUS;

@Slf4j
@RestController
@RequestMapping(ADMIN_API)
public class ServiceStatusController {

    private final ServiceStatusUtil serviceStatusUtil;

    @Autowired
    public ServiceStatusController(ServiceStatusUtil serviceStatusUtil) {
        this.serviceStatusUtil = serviceStatusUtil;
    }

    @PostMapping(UP)
    public ResponseEntity<?> allowTransactions(){
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< Allow Transactions To NIBSS  >>>>>>>>");
        marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                build().toUri().toASCIIString(), "Updating Service Status to UP", false);
        try{
            serviceStatusUtil.changeStatus(UP_STATUS);
            marker.setMainResponse("Completed Updating Service Status to UP", false);
            return new ResponseEntity( HttpStatus.OK);
        }finally {
            marker.done();
        }
    }

    @PostMapping(DOWN)
    public ResponseEntity<?> rejectTransactions(){
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< Reject Transactions To NIBSS  >>>>>>>>");
        marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                build().toUri().toASCIIString(), "Updating Service Status to DOWN", false);
        try{
            serviceStatusUtil.changeStatus(DOWN_STATUS);
            marker.setMainResponse("Completed Updating Service Status to DOWN", false);
            return new ResponseEntity( HttpStatus.OK);
        }finally {
            marker.done();
        }
    }
}
