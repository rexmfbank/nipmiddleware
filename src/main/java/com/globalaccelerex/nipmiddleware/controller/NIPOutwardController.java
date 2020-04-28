package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.facade.NIPOutwardFacade;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer.FTPendingResponse;
import com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer.FTSingleCreditRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.tsq.TsqRequest;
import com.globalaccelerex.nipmiddleware.security.outward.OutwardAuthenticationData;
import com.globalaccelerex.nipmiddleware.util.SessionIdUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import static com.globalaccelerex.nipmiddleware.api.ClientAPI.*;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;

@Slf4j
@RestController
@RequestMapping(NIP_OUTWARD_API)
public class NIPOutwardController extends APIController{

    private final NIPOutwardFacade nipOutwardFacade;

    private final SessionIdUtil sessionIdUtil;

    @Autowired
    public NIPOutwardController(NIPOutwardFacade nipOutwardFacade, SessionIdUtil sessionIdUtil) {
        this.nipOutwardFacade = nipOutwardFacade;
        this.sessionIdUtil = sessionIdUtil;
    }

    @PostMapping(NAME_ENQUIRY)
    public ResponseEntity<?> doNameEnquiry(@Valid @RequestBody NESingleRequest neSingleRequest){
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< doNameEnquiry >>>>>>>>");
        try {

            ResponseEntity entity = validateClient(marker,neSingleRequest.getClientId());
            if (entity != null) {
                return entity;
            }

            neSingleRequest.setMarker(marker);
            marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), neSingleRequest.toString(), false);
            final val neSingleResponse = nipOutwardFacade.doNameEnquiry(neSingleRequest);
            marker.setMainResponse(neSingleResponse.toString(), false);
            return new ResponseEntity(neSingleResponse, HttpStatus.OK);
        } finally {
            marker.done();
        }
    }

    @PostMapping(FUNDS_TRANSFER)
    public ResponseEntity<?> doFundsTransfer(@Valid @RequestBody FTSingleCreditRequest ftSingleCreditRequest){
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< doFundsTransfer >>>>>>>>");
        try {
            ResponseEntity entity = validateClient(marker,ftSingleCreditRequest.getClientId());
            if (entity != null) {
                return entity;
            }
            ftSingleCreditRequest.setMarker(marker);
            marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), ftSingleCreditRequest.toString(), false);
            final val sessionId = sessionIdUtil.generateSessionId();

            final val result = nipOutwardFacade.confirmClientAndPaymentReference(ftSingleCreditRequest);
            final val ftPendingResponse = new FTPendingResponse(result);
            ftPendingResponse.setClientId(ftSingleCreditRequest.getClientId());
            if (!result){
                nipOutwardFacade.doFundsTransferAsync(ftSingleCreditRequest, sessionId);
            }
            ftPendingResponse.setSessionId(sessionId);
            ftPendingResponse.setPaymentReference(ftSingleCreditRequest.getPaymentReference());
            marker.setMainResponse(ftPendingResponse.toString(), false);
            return new ResponseEntity(ftPendingResponse, HttpStatus.OK);
        }finally {
            marker.done();
        }
    }

    @PostMapping(TSQ)
    public ResponseEntity<?> doTsq(@Valid @RequestBody TsqRequest tsqRequest){
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< doTsq >>>>>>>>");
        try{
            ResponseEntity entity = validateClient(marker,tsqRequest.getClientId());
            if (entity != null) {
                return entity;
            }

            tsqRequest.setMarker(marker);
            marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), tsqRequest.toString(), false);
            final val tsqResponse = nipOutwardFacade.doTsq(tsqRequest);
            marker.setMainResponse(tsqResponse.toString(), false);
            return new ResponseEntity(tsqResponse, HttpStatus.OK);
        }finally {
            marker.done();
        }
    }
}
