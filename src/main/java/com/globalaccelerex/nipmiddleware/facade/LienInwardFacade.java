package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Accountblock;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.AccountblockResponse;
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

        //log.info("\n encryptedAccountBlockString ::::: {} \n clearAccountBlockString ::::: {}" ,encryptedAccountBlockString, clearAccountBlockString);

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

}
