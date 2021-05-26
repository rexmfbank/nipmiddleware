package com.globalaccelerex.nipmiddleware.controller;


import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.facade.outward.FtFacade;
import com.globalaccelerex.nipmiddleware.facade.outward.TsqFacade;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.mapper.NIPOutwardMapper;
import com.globalaccelerex.nipmiddleware.payload.client.fundstransfer.FTPendingResponse;
import com.globalaccelerex.nipmiddleware.payload.client.fundstransfer.FTSingleCreditRequest;
import com.globalaccelerex.nipmiddleware.payload.client.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.tsq.TsqRequest;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.service.db.FundsTransferDbService;
import com.globalaccelerex.nipmiddleware.util.SessionIdUtil;
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
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_09;


@Slf4j
@RestController
@RequestMapping(OUTWARD_API)
public class OutwardController extends APIController{

    private final FtFacade ftFacade;

    private final SessionIdUtil sessionIdUtil;

    private final TsqFacade tsqFacade;

    private  ClientDbService clientDbService;

    private FundsTransferDbService fundsTransferDbService;

    private  NIPOutwardMapper nipOutwardMapper;

    @Autowired
    public OutwardController(FtFacade ftFacade, SessionIdUtil sessionIdUtil, TsqFacade tsqFacade) {
        this.ftFacade = ftFacade;
        this.sessionIdUtil = sessionIdUtil;
        this.tsqFacade = tsqFacade;
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

            final val neSingleResponse = ftFacade.doNameEnquiry(neSingleRequest);
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


            final val fundsTransferEntityOpt = ftFacade.confirmClientAndPaymentReference(ftSingleCreditRequest);

            final val ftPendingResponse = new FTPendingResponse();
            ftPendingResponse.setClientId(ftSingleCreditRequest.getClientId());
            if (fundsTransferEntityOpt.isPresent()){
                marker.info("<<<<<<<< Duplicate  reference detected >>>>>>>> clientId [ " + ftSingleCreditRequest.getClientId() + " ] " +
                        "- Reference [ " + ftSingleCreditRequest.getPaymentReference() + " ] ");
                final val sessionId = fundsTransferEntityOpt.get().getSessionId();
                ftPendingResponse.setSessionId(sessionId);
                ftPendingResponse.setPaymentReference(ftSingleCreditRequest.getPaymentReference());
            }else {
                val responseMsg = ftFacade.validateCompulsoryFields(ftSingleCreditRequest);
                if(StringUtils.isNotBlank(responseMsg)){
                    val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
                    nipMiddleWareAPIException.buildFailureStatusException(responseMsg,marker);
                    throw nipMiddleWareAPIException;
                }

                final val sessionId = sessionIdUtil.generateSessionId(ftSingleCreditRequest.getOriginatorBankCode());

                val clientEntity = clientDbService.findClientByClientId(ftSingleCreditRequest.getClientId()).get();
                ftSingleCreditRequest.updateCompulsoryFields(clientEntity);
                final val fundsTransferEntity = nipOutwardMapper.mapFundsTransferEntity.apply(ftSingleCreditRequest);
                fundsTransferEntity.setSessionId(sessionId);
                fundsTransferEntity.setPaymentStatusEnum(NIP_09.getPaymentStatusEnum());
                fundsTransferEntity.setResponseCode(NIP_09.getCode());
                fundsTransferEntity.setResponseDescription(NIP_09.getDescription());
                fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);

                ftFacade.doFundsTransfer(ftSingleCreditRequest, sessionId);
                ftPendingResponse.setSessionId(sessionId);
                ftPendingResponse.setPaymentReference(ftSingleCreditRequest.getPaymentReference());
            }
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

            final val tsqResponse = tsqFacade.doTsq(tsqRequest);
            marker.setMainResponse(tsqResponse.toString(), false);
            return new ResponseEntity(tsqResponse, HttpStatus.OK);
        }finally {
            marker.done();
        }
    }

    @Autowired
    public void setClientDbService(ClientDbService clientDbService) {
        this.clientDbService = clientDbService;
    }

    @Autowired
    public void setNipOutwardMapper(NIPOutwardMapper nipOutwardMapper) {
        this.nipOutwardMapper = nipOutwardMapper;
    }

    @Autowired
    public void setFundsTransferDbService(FundsTransferDbService fundsTransferDbService) {
        this.fundsTransferDbService = fundsTransferDbService;
    }
}
