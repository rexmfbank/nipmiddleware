package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.facade.outward.FtFacade;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.mapper.ClientMapper;
import com.globalaccelerex.nipmiddleware.payload.client.createClient.CreateClientRequest;
import com.globalaccelerex.nipmiddleware.payload.client.createClient.CreateClientResponse;
import com.globalaccelerex.nipmiddleware.payload.client.getclients.ClientDetail;
import com.globalaccelerex.nipmiddleware.payload.client.getclients.GetClientsRequest;
import com.globalaccelerex.nipmiddleware.payload.client.getclients.GetClientsResponse;
import com.globalaccelerex.nipmiddleware.payload.client.resetpassword.ResetPasswordRequest;
import com.globalaccelerex.nipmiddleware.payload.client.resetpassword.ResetPasswordResponse;
import com.globalaccelerex.nipmiddleware.payload.client.updateclient.UpdateClientRequest;
import com.globalaccelerex.nipmiddleware.payload.client.updateclient.UpdateClientStatusRequest;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_00;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.*;

@Slf4j
@Service
public class AdminFacade {

    private  ClientDbService clientDbService;

    private  JwtTokenUtil jwtTokenUtil;

    private  ClientMapper clientMapper;

    private  FtFacade ftFacade;

    private BCryptPasswordEncoder bcryptPasswordEncoder;


    public CreateClientResponse createClient(CreateClientRequest createClientRequest){
        final val iMarker = createClientRequest.getMarker();
        iMarker.info("::::::: Handling Create Client ::::::: ");
        final val clientId = createClientRequest.getClientId();

        if(clientDbService.isClientPresent(clientId).isPresent()){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(CLIENT_DETAILS_EXIST_IN_DB_MSG,iMarker);
            throw nipMiddleWareAPIException;
        }
        final val accountDetail = createClientRequest.getAccountDetail();
        final val clientEntity = clientMapper.mapClientEntity.apply(createClientRequest);
        final val jwtTokenStr = jwtTokenUtil.createJWT(clientId, "NIP", "X_TOKEN", 0);
        if(accountDetail.isDetailsAvailable()){
            // no need to call NIP
            clientEntity.setAccountName(accountDetail.getAccountName());
            clientEntity.setAccountNo(accountDetail.getAccountNo());
            clientEntity.setBankCode(accountDetail.getBankCode());
            clientEntity.setOriginatorBankCode(createClientRequest.getOriginatorBankCode());
            clientEntity.setKycLevel(accountDetail.getKycLevel());
            clientEntity.setBvn(accountDetail.getBvn());
            clientDbService.saveClientEntity(clientEntity);
            final val createClientResponse = clientMapper.mapCreateClientResponse.apply(createClientRequest);
            createClientResponse.setSecretKey(jwtTokenStr);
            return createClientResponse;
        }

        val neSingleRequest = clientMapper.mapNESingleRequest.apply(createClientRequest);
        neSingleRequest.setMarker(createClientRequest.getMarker());
        val neSingleResponse = ftFacade.doNameEnquiry(neSingleRequest);
        if(NIPResponseCodeEnum.isSuccess(neSingleResponse.getResponseCode())){

            clientEntity.setAccountName(neSingleResponse.getAccountName());
            clientEntity.setAccountNo(neSingleResponse.getAccountNo());
            clientEntity.setBankCode(neSingleResponse.getDestinationBankCode());
            clientEntity.setOriginatorBankCode(createClientRequest.getOriginatorBankCode());
            clientEntity.setKycLevel(neSingleResponse.getKycLevel());
            clientEntity.setBvn(neSingleResponse.getBankVerificationNo());
            clientDbService.saveClientEntity(clientEntity);


            final val createClientResponse = clientMapper.mapCreateClientResponse.apply(createClientRequest);
            createClientResponse.setSecretKey(jwtTokenStr);
            return createClientResponse;
        }else{
            final val responseCodeEnum = NIPResponseCodeEnum.getResponseCodeEnum(neSingleResponse.getResponseCode());
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(responseCodeEnum.getDescription(),responseCodeEnum.getCode(), iMarker);
            throw nipMiddleWareAPIException;
        }
    }

    public ClientDetail getClientDetail(String clientId , IMarker marker){
        marker.info("processing get client  request ");
        val clientEntityOpt = clientDbService.isClientPresent(clientId);
        if(clientEntityOpt.isPresent()){
            final val clientDetail = clientMapper.mapClientDetail.apply(clientEntityOpt.get());

            marker.info("done processing get client request ");
            return clientDetail;
        }else{
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(CLIENT_NOT_FOUND_MSG,marker);
            throw nipMiddleWareAPIException;
        }
    }

    public void updateClient(UpdateClientRequest updateClientRequest){
        IMarker marker = updateClientRequest.getMarker();
        marker.info("processing update client  request ");
        val clientEntityOpt = clientDbService.isClientPresent(updateClientRequest.getClientId());
        if(!clientEntityOpt.isPresent()){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(CLIENT_NOT_FOUND_MSG,marker);
            throw nipMiddleWareAPIException;
        }

        val clientEntity = clientEntityOpt.get();
        //check if name already exists
        val clientEntityOpt_ = clientDbService.isClientNamePresent(updateClientRequest.getClientName());
        if(clientEntityOpt_ .isPresent() && !StringUtils.equalsIgnoreCase(clientEntityOpt_.get().getClientId() ,updateClientRequest.getClientId())){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(CLIENT_DETAILS_EXIST_IN_DB_MSG,marker);
            throw nipMiddleWareAPIException;
        }
        final val accountDetail = updateClientRequest.getAccountDetail();
        val updatedClientEntity = clientMapper.updateClientEntity(clientEntity, updateClientRequest);
        val originatorBankCode = updateClientRequest.getOriginatorBankCode();
        if(accountDetail.isDetailsAvailable()){
            updatedClientEntity.setAccountName(accountDetail.getAccountName());
            updatedClientEntity.setAccountNo(accountDetail.getAccountNo());
            updatedClientEntity.setBankCode(accountDetail.getBankCode());
            updatedClientEntity.setOriginatorBankCode(originatorBankCode);
            updatedClientEntity.setKycLevel(accountDetail.getKycLevel());
            updatedClientEntity.setBvn(accountDetail.getBvn());
            clientDbService.updateClientEntity(updatedClientEntity);
            marker.info("done processing update client request ");
            return;
        }

        // Do NameEnquiry
        val neSingleRequest = clientMapper.mapNESingleRequest_1.apply(updateClientRequest);
        neSingleRequest.setMarker(updateClientRequest.getMarker());
        val neSingleResponse = ftFacade.doNameEnquiry(neSingleRequest);

        if(NIPResponseCodeEnum.isSuccess(neSingleResponse.getResponseCode())){
            updatedClientEntity.setAccountName(neSingleResponse.getAccountName());
            updatedClientEntity.setAccountNo(neSingleResponse.getAccountNo());
            updatedClientEntity.setBankCode(neSingleResponse.getDestinationBankCode());
            updatedClientEntity.setOriginatorBankCode(originatorBankCode);
            updatedClientEntity.setKycLevel(neSingleResponse.getKycLevel());
            updatedClientEntity.setBvn(neSingleResponse.getBankVerificationNo());
        }else{
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(NAME_ENQUIRY_FAILED_MSG,marker);
            throw nipMiddleWareAPIException;
        }
    }

    public GetClientsResponse getClients(GetClientsRequest getClientsRequest){
        IMarker marker = getClientsRequest.getMarker();
        marker.info("processing get clients  request ");
        val requestSize = getClientsRequest.getSize();
        val pageIndex = getClientsRequest.getPageIndex();
        val startWith = getClientsRequest.getStartWith();
        val pageClientEntities = clientDbService.findClients(requestSize, startWith, pageIndex);

        final val clientDetailList = pageClientEntities.getContent()
                .stream()
                .map(clientMapper.mapClientDetail)
                .collect(Collectors.toList());

        marker.info("done processing get clients  request ");
        return GetClientsResponse.builder()
                .clientDetailList(clientDetailList)
                .hasContent(pageClientEntities.hasContent())
                .hasNext(pageClientEntities.hasNext())
                .hasPrevious(pageClientEntities.hasPrevious())
                .isFirst(pageClientEntities.isFirst())
                .isLast(pageClientEntities.isLast())
                .numberOfElement(pageClientEntities.getNumberOfElements())
                .size(pageClientEntities.getSize())
                .totalElements(pageClientEntities.getTotalElements())
                .totalPages(pageClientEntities.getTotalPages())
                .build();
    }

    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest){
        IMarker marker = resetPasswordRequest.getMarker();
        marker.info("processing reset password  request ");
        val clientEntityOpt = clientDbService.isClientPresent(resetPasswordRequest.getClientId());
        if(!clientEntityOpt.isPresent()){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(CLIENT_NOT_FOUND_MSG,marker);
            throw nipMiddleWareAPIException;
        }

        val clientEntity = clientEntityOpt.get();

        val newPassword = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        clientEntity.setPassword(bcryptPasswordEncoder.encode(newPassword));
        clientDbService.updateClientEntity(clientEntity);
        val resetPasswordResponse = new ResetPasswordResponse(NIP_00);
        resetPasswordResponse.setPassword(newPassword);
        resetPasswordResponse.setClientId(resetPasswordRequest.getClientId());
        marker.info("done resetting password  request ");
        return resetPasswordResponse;
    }

    public void updateClientStatus(UpdateClientStatusRequest updateClientStatusRequest){
        IMarker marker = updateClientStatusRequest.getMarker();
        marker.info("processing Update Client Status request ");
        val clientEntityOpt = clientDbService.isClientPresent(updateClientStatusRequest.getClientId());
        if(!clientEntityOpt.isPresent()){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(CLIENT_NOT_FOUND_MSG,marker);
            throw nipMiddleWareAPIException;
        }
        val clientEntity = clientEntityOpt.get();
        clientEntity.setClientStatus(updateClientStatusRequest.getStatus());
        clientDbService.updateClientEntity(clientEntity);
        marker.info("done Updating Client Status");
    }

    @Autowired
    public void setClientDbService(ClientDbService clientDbService) {
        this.clientDbService = clientDbService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setClientMapper(ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
    }

    @Autowired
    public void setFtFacade(FtFacade ftFacade) {
        this.ftFacade = ftFacade;
    }

    @Autowired
    public void setBcryptPasswordEncoder(BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }
}
