package com.globalaccelerex.nipmiddleware.service.db;

import com.globalaccelerex.nipmiddleware.entity.FundsTransferEntity;
import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.repository.FundsTransferRepository;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_130;


@Service
public class FundsTransferDbService {

    private final FundsTransferRepository fundsTransferRepository;

    private static final long FIVE_MINS_MILLIS = 1000*60*5 ;

    private static final long TWENTY_FOUR_HOURS_MILLIS = 1000*60*60*24 ;

    @Autowired
    public FundsTransferDbService(FundsTransferRepository fundsTransferRepository) {
        this.fundsTransferRepository = fundsTransferRepository;
    }

    public Optional<FundsTransferEntity> confirmClientAndPaymentReference(String clientId, String paymentReference){
       return fundsTransferRepository.findByClientIdAndPaymentReference(clientId,paymentReference);

    }

    public FundsTransferEntity findRecordByClientIdAndSessionId(String clientId, String sessionId, IMarker iMarker){
        val fundsTransferEntityOpt = fundsTransferRepository.findBySessionIdAndClientId(sessionId, clientId);
        if(fundsTransferEntityOpt.isPresent()){
            return fundsTransferEntityOpt.get();
        }else{
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildExceptionFromEnum(NIP_130,iMarker);
            throw nipMiddleWareAPIException;
        }
    }

    public FundsTransferEntity findRecord(String clientId, String paymentReference, IMarker iMarker){
        final val fundsTransferEntityOpt = fundsTransferRepository.findByClientIdAndPaymentReference(clientId,paymentReference);
        if(fundsTransferEntityOpt.isPresent()){
            return fundsTransferEntityOpt.get();
        }else{
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildExceptionFromEnum(NIP_130,iMarker);
            throw nipMiddleWareAPIException;
        }
    }

    public FundsTransferEntity findRecord(String clientId, String paymentReference, String sessionId, IMarker iMarker){
        final val fundsTransferEntityOpt = fundsTransferRepository
                .findByClientIdAndPaymentReferenceAndSessionId(clientId,paymentReference,sessionId);
        if(fundsTransferEntityOpt.isPresent()){
            return fundsTransferEntityOpt.get();
        }else{
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildExceptionFromEnum(NIP_130,iMarker);
            throw nipMiddleWareAPIException;
        }
    }

    public FundsTransferEntity updateFTResponseCode(String sessionId , String responseCode , String clientId,String description, IMarker iMarker){
        final val fundsTransferEntityOpt = fundsTransferRepository.findBySessionIdAndClientId(sessionId,clientId);
        if(!fundsTransferEntityOpt.isPresent()){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildExceptionFromEnum(NIP_130,iMarker);
            throw nipMiddleWareAPIException;
        }
        val fundsTransferEntity = fundsTransferEntityOpt.get();
        //only allow updates on db if status is initially PENDING
        if (StringUtils.isNotBlank(responseCode) &&  PaymentStatusEnum.isPending(fundsTransferEntity.getPaymentStatusEnum())){
            val nipResponseCodeEnum = NIPResponseCodeEnum.getResponseCodeEnum(responseCode);
            if((nipResponseCodeEnum.equals(NIPResponseCodeEnum.NIP_15) || nipResponseCodeEnum.equals(NIPResponseCodeEnum.NIP_25))){

                val currentTimeMillis = System.currentTimeMillis();
                val createdAtMillis = fundsTransferEntity.getCreatedAt().getTime();
                val timeDiffMillis = currentTimeMillis - createdAtMillis;
                if(timeDiffMillis > FIVE_MINS_MILLIS && timeDiffMillis < TWENTY_FOUR_HOURS_MILLIS){
                    iMarker.info("forcing failure after error code 15 and 25 for more than 5 mins less than 24 hours ");
                    fundsTransferEntity.setResponseCode(responseCode);
                    fundsTransferEntity.setResponseDescription(nipResponseCodeEnum.getDescription());
                    fundsTransferEntity.setPaymentStatusEnum(PaymentStatusEnum.FAILED);
                }//no need for else since the status will still be left as pending
            }else{
                fundsTransferEntity.setResponseCode(responseCode);
                fundsTransferEntity.setResponseDescription(StringUtils.defaultIfBlank(description,nipResponseCodeEnum.getDescription()));
                fundsTransferEntity.setPaymentStatusEnum(NIPResponseCodeEnum.getPaymentStatusEnum(responseCode));
            }
            return fundsTransferRepository.save(fundsTransferEntity);
        }
        return fundsTransferEntity;
    }

    public void saveFundsTransferEntity(FundsTransferEntity fundsTransferEntity){
        fundsTransferRepository.save(fundsTransferEntity);
    }
}
