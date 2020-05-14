package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.mapper.ClientMapper;
import com.globalaccelerex.nipmiddleware.payload.client.*;
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

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;

@Slf4j
@Service
public class AdminFacade {

    private final ClientDbService clientDbService;

    private final JwtTokenUtil jwtTokenUtil;

    private final ClientMapper clientMapper;

    private final NIPOutwardFacade nipOutwardFacade;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AdminFacade(ClientDbService clientDbService, JwtTokenUtil jwtTokenUtil, ClientMapper clientMapper, NIPOutwardFacade nipOutwardFacade) {
        this.clientDbService = clientDbService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientMapper = clientMapper;
        this.nipOutwardFacade = nipOutwardFacade;
    }

    public CreateClientResponse createClient(CreateClientRequest createClientRequest){
        final val iMarker = createClientRequest.getMarker();
        iMarker.info("::::::: Handling Create Client ::::::: ");
        final val clientId = createClientRequest.getClientId();

        if(clientDbService.isClientPresent(clientId).isPresent()){
            throw new NIPMiddleWareAPIException(NIP_114,iMarker);
        }
        val neSingleRequest = clientMapper.mapNESingleRequest.apply(createClientRequest);
        neSingleRequest.setMarker(createClientRequest.getMarker());
        val neSingleResponse = nipOutwardFacade.doNameEnquiry(neSingleRequest);

        if(NIPResponseCodeEnum.isSuccess(neSingleResponse.getResponseCode())){
            final val clientEntity = clientMapper.mapClientEntity.apply(createClientRequest);
            clientEntity.setAccountName(neSingleResponse.getAccountName());
            clientEntity.setAccountNo(neSingleResponse.getAccountNo());
            clientEntity.setBankCode(neSingleResponse.getDestinationInstitutionCode());
            clientEntity.setKycLevel(neSingleResponse.getKycLevel());
            clientEntity.setBvn(neSingleResponse.getBankVerificationNo());
            clientDbService.saveClientEntity(clientEntity);

            final val jwtTokenStr = jwtTokenUtil.createJWT(clientId, "NIP", "X_TOKEN", 0);
            final val createClientResponse = clientMapper.mapCreateClientResponse.apply(createClientRequest);
            createClientResponse.setSecretKey(jwtTokenStr);
            return createClientResponse;
        }else{
            throw new NIPMiddleWareAPIException(NIP_105,iMarker);
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
            throw new NIPMiddleWareAPIException(NIP_124,marker);
        }
    }

    public void updateClient(UpdateClientRequest updateClientRequest){
        IMarker marker = updateClientRequest.getMarker();
        marker.info("processing update client  request ");
        val clientEntityOpt = clientDbService.isClientPresent(updateClientRequest.getClientId());
        if(!clientEntityOpt.isPresent()){
            throw new NIPMiddleWareAPIException(NIP_124,marker);
        }

        val clientEntity = clientEntityOpt.get();
        //check if name already exists
        val clientEntityOpt_ = clientDbService.isClientNamePresent(updateClientRequest.getClientName());
        if(clientEntityOpt_ .isPresent() && !StringUtils.equalsIgnoreCase(clientEntityOpt_.get().getClientId() ,updateClientRequest.getClientId())){
            throw new NIPMiddleWareAPIException(NIP_114 , marker);
        }
        val accountNo = updateClientRequest.getAccountNo();
        val bankCode = updateClientRequest.getBankCode();
        val originatorBankCode = updateClientRequest.getOriginatorBankCode();

        val updatedClientEntity = clientMapper.updateClientEntity(clientEntity, updateClientRequest);

        if(StringUtils.isNoneBlank(accountNo,bankCode,originatorBankCode)){
            // Do NameEnquiry
            val neSingleRequest = clientMapper.mapNESingleRequest_1.apply(updateClientRequest);
            neSingleRequest.setMarker(updateClientRequest.getMarker());
            val neSingleResponse = nipOutwardFacade.doNameEnquiry(neSingleRequest);

            if(NIPResponseCodeEnum.isSuccess(neSingleResponse.getResponseCode())){
                updatedClientEntity.setAccountName(neSingleResponse.getAccountName());
                updatedClientEntity.setAccountNo(neSingleResponse.getAccountNo());
                updatedClientEntity.setBankCode(neSingleResponse.getDestinationInstitutionCode());
                updatedClientEntity.setKycLevel(neSingleResponse.getKycLevel());
                updatedClientEntity.setBvn(neSingleResponse.getBankVerificationNo());
            }else{
                throw new NIPMiddleWareAPIException(NIP_105,marker);
            }
        }

        clientDbService.updateClientEntity(updatedClientEntity);

        marker.info("done processing update client request ");
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
            throw new NIPMiddleWareAPIException(NIP_124,marker);
        }

        val clientEntity = clientEntityOpt.get();

        val newPassword = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        clientEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
        clientDbService.updateClientEntity(clientEntity);
        val resetPasswordResponse = new ResetPasswordResponse(NIP_00);
        resetPasswordResponse.setPassword(newPassword);
        resetPasswordResponse.setClientId(resetPasswordRequest.getClientId());
        marker.info("done resetting password  request ");
        return resetPasswordResponse;
    }
}
