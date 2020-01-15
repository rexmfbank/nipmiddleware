package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Nameenquirysingleitem;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.NameenquirysingleitemResponse;
import com.globalaccelerex.nipmiddleware.service.NIPInwardService;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NIPInwardFacade extends AbstractInwardFacade{

    @Autowired
    private NIPInwardService nipInwardService;

    @Autowired
    public NIPInwardFacade(SSMUtil ssmUtil, XmlUtil xmlUtil, NipConfig nipConfig) {
        super(ssmUtil, xmlUtil, nipConfig);
    }

    public NameenquirysingleitemResponse handleNameEnquiry(Nameenquirysingleitem nameenquirysingleitem, IMarker marker){
        final val encryptedNEString = nameenquirysingleitem.getRequest();
        final val clearNEString = nipConfig.isIgnoreEncryption() ? encryptedNEString : decryptString(encryptedNEString);
        log.info("\n encryptedNEString ::::: {} \n clearNEString ::::: {}" ,encryptedNEString, clearNEString);

        marker.setRequest(" NameEnquiry Clear String ",clearNEString);

        final val neSingleRequestVO = xmlUtil.unmarshal(clearNEString, NESingleRequestVO.class);
        //connect to backend service to retrieve the name
        // add a logic to determine which backend CBA to call based on destination code
        final val neSingleResponseVO = nipInwardService.handleNameEnquiry(neSingleRequestVO);

        marker.setResponse("Response from NameEnquiry CBA " + neSingleResponseVO.toString());

        final val neSingleResponseVOXmlString = xmlUtil.marshal(NESingleResponseVO.class, neSingleResponseVO);

        final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? neSingleResponseVOXmlString : encryptString(neSingleResponseVOXmlString);

        final val nameenquirysingleitemResponse = new NameenquirysingleitemResponse();
        nameenquirysingleitemResponse.setReturn(encryptedXmlString);
        return nameenquirysingleitemResponse;
    }


}
