package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.*;
import com.globalaccelerex.nipmiddleware.service.NIPInwardService;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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

    public AccountblockResponse handleAccountBlock(Accountblock accountblock, IMarker marker){
        final val encryptedAccountBlockString = accountblock.getRequest();
        final val clearAccountBlockString = nipConfig.isIgnoreEncryption() ? encryptedAccountBlockString : decryptString(encryptedAccountBlockString);

        marker.setRequest(" Account Block Clear String ",clearAccountBlockString);

        final val accountBlockRequestVO = xmlUtil.unmarshal(clearAccountBlockString, AccountBlockRequestVO.class);

        // some backend calls

        final val accountBlockResponseVO = nipInwardService.handleAccountBlock(accountBlockRequestVO);

        marker.setResponse("Response from AccountBlock CBA " + accountBlockResponseVO.toString());

        final val accountBlockResponseVOXmlString = xmlUtil.marshal(AccountBlockResponseVO.class, accountBlockResponseVO);

        final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? accountBlockResponseVOXmlString : encryptString(accountBlockResponseVOXmlString);

        final val accountBlockResponse = new AccountblockResponse();
        accountBlockResponse.setReturn(encryptedXmlString);
        return accountBlockResponse;
    }

    public AccountunblockResponse handleAccountUnblock(Accountunblock accountunblock, IMarker marker){
        final val encryptedAccountUnblockString = accountunblock.getRequest();
        final val clearAccountUnblockString = nipConfig.isIgnoreEncryption() ? encryptedAccountUnblockString : decryptString(encryptedAccountUnblockString);

        marker.setRequest(" Account Unblock Clear String ",clearAccountUnblockString);

        final val accountUnblockRequestVO = xmlUtil.unmarshal(clearAccountUnblockString, AccountUnblockRequestVO.class);

        // some backend calls
        final val accountUnblockResponseVO = nipInwardService.handleAccountUnblock(accountUnblockRequestVO);

        marker.setResponse("Response from Account Unblock CBA " + accountUnblockResponseVO.toString());

        final val accountUnblockResponseVOXmlString = xmlUtil.marshal(AccountUnblockResponseVO.class, accountUnblockResponseVO);

        final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? accountUnblockResponseVOXmlString : encryptString(accountUnblockResponseVOXmlString);

        final val accountUnblockResponse = new AccountunblockResponse();
        accountUnblockResponse.setReturn(encryptedXmlString);
        return accountUnblockResponse;
    }

    public AmountblockResponse handleAmountBlock(Amountblock amountblock, IMarker marker){
        final val encryptedAmountBlockString = amountblock.getRequest();
        final val clearAmountBlockString = nipConfig.isIgnoreEncryption() ? encryptedAmountBlockString : decryptString(encryptedAmountBlockString);

        marker.setRequest(" Amount Block Clear String ",clearAmountBlockString);
        final val amountBlockRequestVO = xmlUtil.unmarshal(clearAmountBlockString, AmountBlockRequestVO.class);

        // some backend calls
        final val amountBlockResponseVO = nipInwardService.handleAmountBlock(amountBlockRequestVO);

        marker.setResponse("Response from Amount block CBA " + amountBlockResponseVO.toString());

        final val amountBlockResponseVOXmlString = xmlUtil.marshal(AmountBlockResponseVO.class, amountBlockResponseVO);

        final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? amountBlockResponseVOXmlString : encryptString(amountBlockResponseVOXmlString);

        final val amountBlockResponse = new AmountblockResponse();
        amountBlockResponse.setReturn(encryptedXmlString);
        return amountBlockResponse;
    }

}
