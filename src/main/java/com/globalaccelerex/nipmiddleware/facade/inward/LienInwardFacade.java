package com.globalaccelerex.nipmiddleware.facade.inward;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountunblock.AmountUnblockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountunblock.AmountUnblockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.*;
import com.globalaccelerex.nipmiddleware.service.NIPInwardService;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LienInwardFacade extends AbstractInwardFacade{

    @Autowired
    private NIPInwardService nipInwardService;

    @Autowired
    public LienInwardFacade(SSMUtil ssmUtil, XmlUtil xmlUtil, NipConfig nipConfig) {
        super(ssmUtil,xmlUtil, nipConfig);
    }

    public AccountblockResponse handleAccountBlock(Accountblock accountblock, IMarker marker, String originatingInstitutionCode){
        final val accountBlockResponse = new AccountblockResponse();
        try{
            if(StringUtils.isBlank(originatingInstitutionCode)){
                marker.setRequest(" Originating Institution Code is not available ","");
                accountBlockResponse.setReturn(accountblock.getRequest());
                marker.setResponse("Sending Encrypted Request back ");
            }else{
                final val encryptedAccountBlockString = accountblock.getRequest();
                final val clearAccountBlockString = nipConfig.isIgnoreEncryption() ? encryptedAccountBlockString : decryptString(encryptedAccountBlockString,originatingInstitutionCode,marker);

                marker.setRequest(" Account Block Clear String ",clearAccountBlockString);

                final val accountBlockRequestVO = xmlUtil.unmarshal(clearAccountBlockString, AccountBlockRequestVO.class);

                // some backend calls

                final val accountBlockResponseVO = nipInwardService.handleAccountBlock(accountBlockRequestVO,originatingInstitutionCode,marker);

                marker.setResponse("Response from AccountBlock CBA " + accountBlockResponseVO.toString());

                final val accountBlockResponseVOXmlString = xmlUtil.marshal(AccountBlockResponseVO.class, accountBlockResponseVO);

                final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? accountBlockResponseVOXmlString : encryptString(accountBlockResponseVOXmlString,originatingInstitutionCode,marker);

                accountBlockResponse.setReturn(encryptedXmlString);
            }
        }catch (Exception exception){
            if(exception instanceof NIPMiddleWareAPIException){
                val nipMiddleWareAPIException =(NIPMiddleWareAPIException) exception;
                marker.info(nipMiddleWareAPIException.getErrorResponse().getResponseMessage(),nipMiddleWareAPIException);
            }else {
                marker.info(exception.getMessage(),exception);
            }
            accountBlockResponse.setReturn("");
        }

        return accountBlockResponse;
    }

    public AccountunblockResponse handleAccountUnblock(Accountunblock accountunblock, IMarker marker, String originatingInstitutionCode){
        final val accountUnblockResponse = new AccountunblockResponse();
        try{
            if(StringUtils.isBlank(originatingInstitutionCode)){
                marker.setRequest(" Originating Institution Code is not available ","");
                accountUnblockResponse.setReturn(accountunblock.getRequest());
                marker.setResponse("Sending Encrypted Request back ");
            }else{
                final val encryptedAccountUnblockString = accountunblock.getRequest();
                final val clearAccountUnblockString = nipConfig.isIgnoreEncryption() ? encryptedAccountUnblockString : decryptString(encryptedAccountUnblockString,originatingInstitutionCode,marker);

                marker.setRequest(" Account Unblock Clear String ",clearAccountUnblockString);

                final val accountUnblockRequestVO = xmlUtil.unmarshal(clearAccountUnblockString, AccountUnblockRequestVO.class);

                // some backend calls
                final val accountUnblockResponseVO = nipInwardService.handleAccountUnblock(accountUnblockRequestVO,originatingInstitutionCode,marker);

                marker.setResponse("Response from Account Unblock CBA " + accountUnblockResponseVO.toString());

                final val accountUnblockResponseVOXmlString = xmlUtil.marshal(AccountUnblockResponseVO.class, accountUnblockResponseVO);

                final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? accountUnblockResponseVOXmlString : encryptString(accountUnblockResponseVOXmlString,originatingInstitutionCode,marker);

                accountUnblockResponse.setReturn(encryptedXmlString);
            }
        }catch (Exception exception){
            if(exception instanceof NIPMiddleWareAPIException){
                val nipMiddleWareAPIException =(NIPMiddleWareAPIException) exception;
                marker.info(nipMiddleWareAPIException.getErrorResponse().getResponseMessage(),nipMiddleWareAPIException);
            }else {
                marker.info(exception.getMessage(),exception);
            }

            accountUnblockResponse.setReturn("");
        }

        return accountUnblockResponse;
    }

    public AmountblockResponse handleAmountBlock(Amountblock amountblock, IMarker marker, String originatingInstitutionCode){
        final val amountBlockResponse = new AmountblockResponse();
        try{
            if(StringUtils.isBlank(originatingInstitutionCode)){
                marker.setRequest(" Originating Institution Code is not available ","");
                amountBlockResponse.setReturn(amountblock.getRequest());
                marker.setResponse("Sending Encrypted Request back ");
            }else{
                final val encryptedAmountBlockString = amountblock.getRequest();
                final val clearAmountBlockString = nipConfig.isIgnoreEncryption() ? encryptedAmountBlockString : decryptString(encryptedAmountBlockString,originatingInstitutionCode,marker);

                marker.setRequest(" Amount Block Clear String ",clearAmountBlockString);
                final val amountBlockRequestVO = xmlUtil.unmarshal(clearAmountBlockString, AmountBlockRequestVO.class);

                // some backend calls
                final val amountBlockResponseVO = nipInwardService.handleAmountBlock(amountBlockRequestVO,originatingInstitutionCode,marker);

                marker.setResponse("Response from Amount block CBA " + amountBlockResponseVO.toString());

                final val amountBlockResponseVOXmlString = xmlUtil.marshal(AmountBlockResponseVO.class, amountBlockResponseVO);

                final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? amountBlockResponseVOXmlString : encryptString(amountBlockResponseVOXmlString,originatingInstitutionCode,marker);

                amountBlockResponse.setReturn(encryptedXmlString);
            }
        }catch (Exception exception){
            if(exception instanceof NIPMiddleWareAPIException){
                val nipMiddleWareAPIException =(NIPMiddleWareAPIException) exception;
                marker.info(nipMiddleWareAPIException.getErrorResponse().getResponseMessage(),nipMiddleWareAPIException);
            }else {
                marker.info(exception.getMessage(),exception);
            }
            amountBlockResponse.setReturn("");
        }

        return amountBlockResponse;
    }

    public AmountunblockResponse handleAmountUnblock(Amountunblock amountunblock, IMarker marker, String originatingInstitutionCode){
        final val amountunblockResponse = new AmountunblockResponse();
        try{
            if(StringUtils.isBlank(originatingInstitutionCode)){
                marker.setRequest(" Originating Institution Code is not available ","");
                amountunblockResponse.setReturn(amountunblock.getRequest());
                marker.setResponse("Sending Encrypted Request back ");
            }else{
                final val encryptedAmountUnblockString = amountunblock.getRequest();
                final val clearAmountUnblockString = nipConfig.isIgnoreEncryption() ? encryptedAmountUnblockString : decryptString(encryptedAmountUnblockString,originatingInstitutionCode,marker);

                marker.setRequest(" Amount UnBlock Clear String ",clearAmountUnblockString);

                final val amountUnblockRequestVO = xmlUtil.unmarshal(clearAmountUnblockString, AmountUnblockRequestVO.class);

                // some backend calls

                final val amountUnblockResponseVO = nipInwardService.handleAmountUnblock(amountUnblockRequestVO,originatingInstitutionCode,marker);

                marker.setResponse("Response from Amount Unblock CBA " + amountUnblockResponseVO.toString());

                final val amountUnblockResponseVOXmlString = xmlUtil.marshal(AmountUnblockResponseVO.class, amountUnblockResponseVO);

                final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? amountUnblockResponseVOXmlString : encryptString(amountUnblockResponseVOXmlString,originatingInstitutionCode,marker);

                amountunblockResponse.setReturn(encryptedXmlString);
            }
        }catch (Exception exception){
            if(exception instanceof NIPMiddleWareAPIException){
                val nipMiddleWareAPIException =(NIPMiddleWareAPIException) exception;
                marker.info(nipMiddleWareAPIException.getErrorResponse().getResponseMessage(),nipMiddleWareAPIException);
            }else {
                marker.info(exception.getMessage(),exception);
            }
            amountunblockResponse.setReturn("");
        }

        return amountunblockResponse;
    }

}
