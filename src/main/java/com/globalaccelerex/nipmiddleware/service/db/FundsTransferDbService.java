package com.globalaccelerex.nipmiddleware.service.db;

import com.globalaccelerex.nipmiddleware.entity.FundsTransferEntity;
import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.repository.FundsTransferRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_15;
import static com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum.FAILED;
import static com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum.SUCCESS;

@Service
public class FundsTransferDbService {

    private final FundsTransferRepository fundsTransferRepository;

    @Autowired
    public FundsTransferDbService(FundsTransferRepository fundsTransferRepository) {
        this.fundsTransferRepository = fundsTransferRepository;
    }

    public boolean confirmClientAndPaymentReference(String clientId, String paymentReference){
        final val fundsTransferEntityOpt = fundsTransferRepository.findByClientIdAndPaymentReference(clientId,paymentReference);
        return fundsTransferEntityOpt.isPresent();
    }

    public FundsTransferEntity findRecord(String clientId, String paymentReference){
        final val fundsTransferEntityOpt = fundsTransferRepository.findByClientIdAndPaymentReference(clientId,paymentReference);
        if(fundsTransferEntityOpt.isPresent()){
            return fundsTransferEntityOpt.get();
        }else{
            throw new NIPMiddleWareAPIException(NIP_15.getCode(),NIP_15.getDescription(),false);
        }
    }

    public void updateFTResponseCode(String sessionId , String responseCode){
        final val fundsTransferEntity = fundsTransferRepository.findBySessionId(sessionId).get();
        fundsTransferEntity.setResponseCode(responseCode);
        fundsTransferEntity.setPaymentStatusEnum(NIPResponseCodeEnum.getPaymentStatusEnum(responseCode));
        fundsTransferRepository.save(fundsTransferEntity);
    }

    public void saveFundsTransferEntity(FundsTransferEntity fundsTransferEntity){
        fundsTransferRepository.save(fundsTransferEntity);
    }
}
