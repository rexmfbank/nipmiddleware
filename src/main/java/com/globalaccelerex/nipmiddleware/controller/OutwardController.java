package com.globalaccelerex.nipmiddleware.controller;


import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.facade.NIPOutwardFacade;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.payload.outward.fundstransfer.FTPendingResponse;
import com.globalaccelerex.nipmiddleware.payload.outward.fundstransfer.FTSingleCreditRequest;
import com.globalaccelerex.nipmiddleware.payload.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.outward.tsq.TsqRequest;
import com.globalaccelerex.nipmiddleware.util.SessionIdUtil;
import com.globalaccelerex.nipmiddleware.util.SystemSettingUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import static com.globalaccelerex.nipmiddleware.api.ClientAPI.*;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.PAYMENT_REFERENCE_EXISTS_MSG;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.TXN_SUSPENDED_MSG;


@Slf4j
@RestController
@RequestMapping(OUTWARD_API)
public class OutwardController extends APIController{

    private final NIPOutwardFacade nipOutwardFacade;

    private final SessionIdUtil sessionIdUtil;

    private final SystemSettingUtil systemSettingUtil;

    @Autowired
    public OutwardController(NIPOutwardFacade nipOutwardFacade, SessionIdUtil sessionIdUtil, SystemSettingUtil systemSettingUtil) {
        this.nipOutwardFacade = nipOutwardFacade;
        this.sessionIdUtil = sessionIdUtil;
        this.systemSettingUtil = systemSettingUtil;
    }

    @PostMapping(NAME_ENQUIRY)
    public ResponseEntity<?> doNameEnquiry(@Valid @RequestBody NESingleRequest neSingleRequest){
        IMarker marker = Marker.fromString(neSingleRequest.getAccountNo());
        marker.info("<<<<<<<< doNameEnquiry >>>>>>>>");
        try {

            ResponseEntity entity = validateClient(marker,neSingleRequest.getClientId());
            if (entity != null) {
                return entity;
            }

            neSingleRequest.setMarker(marker);
            marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), neSingleRequest.toString(), false);

            if(systemSettingUtil.isNibssStatusDown()){
                val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
                nipMiddleWareAPIException.buildFailureStatusException(TXN_SUSPENDED_MSG,marker);
                throw nipMiddleWareAPIException;
            }
            final val neSingleResponse = nipOutwardFacade.doNameEnquiry(neSingleRequest);
            marker.setMainResponse(neSingleResponse.toString(), false);
            return new ResponseEntity(neSingleResponse, HttpStatus.OK);
        } finally {
            marker.done();
        }
    }

    @PostMapping(FUNDS_TRANSFER)
    public ResponseEntity<?> doFundsTransfer(@Valid @RequestBody FTSingleCreditRequest ftSingleCreditRequest){
        IMarker marker = Marker.fromString(ftSingleCreditRequest.getPaymentReference());
        marker.info("<<<<<<<< doFundsTransfer >>>>>>>>");
        try {
            ResponseEntity entity = validateClient(marker,ftSingleCreditRequest.getClientId());
            if (entity != null) {
                return entity;
            }
            ftSingleCreditRequest.setMarker(marker);
            marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), ftSingleCreditRequest.toString(), false);

            if(systemSettingUtil.isNibssStatusDown()){
                val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
                nipMiddleWareAPIException.buildFailureStatusException(TXN_SUSPENDED_MSG,marker);
                throw nipMiddleWareAPIException;
            }

            final val result = nipOutwardFacade.confirmClientAndPaymentReference(ftSingleCreditRequest);

            final val ftPendingResponse = new FTPendingResponse();
            ftPendingResponse.setClientId(ftSingleCreditRequest.getClientId());
            if (result){
                val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
                nipMiddleWareAPIException.buildFailureStatusException(PAYMENT_REFERENCE_EXISTS_MSG,marker);
                throw nipMiddleWareAPIException;
            }

            val responseMsg = nipOutwardFacade.validateCompulsoryFields(ftSingleCreditRequest);
            if(StringUtils.isNotBlank(responseMsg)){
                val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
                nipMiddleWareAPIException.buildFailureStatusException(responseMsg,marker);
                throw nipMiddleWareAPIException;
            }

            final val sessionId = sessionIdUtil.generateSessionId(ftSingleCreditRequest.getOriginatorBankCode());
            nipOutwardFacade.doFundsTransferAsync(ftSingleCreditRequest, sessionId);
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
        IMarker marker = Marker.fromString(tsqRequest.getPaymentReference());
        marker.info("<<<<<<<< doTsq >>>>>>>>");
        try{
            ResponseEntity entity = validateClient(marker,tsqRequest.getClientId());
            if (entity != null) {
                return entity;
            }

            tsqRequest.setMarker(marker);
            marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), tsqRequest.toString(), false);

            if(systemSettingUtil.isNibssStatusDown()){
                val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
                nipMiddleWareAPIException.buildFailureStatusException(TXN_SUSPENDED_MSG,marker);
                throw nipMiddleWareAPIException;
            }

            final val tsqResponse = nipOutwardFacade.doTsq(tsqRequest);
            marker.setMainResponse(tsqResponse.toString(), false);
            return new ResponseEntity(tsqResponse, HttpStatus.OK);
        }finally {
            marker.done();
        }
    }


}
