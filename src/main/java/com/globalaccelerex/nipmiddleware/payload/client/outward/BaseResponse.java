package com.globalaccelerex.nipmiddleware.payload.client.outward;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@AllArgsConstructor
public class BaseResponse {

    @NotEmpty
    private String clientId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date responseTime;

    private String responseCode;

    private String responseDescription;

    public BaseResponse(){
        responseTime = new Date();
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
        this.responseDescription = NIPResponseCodeEnum.getResponseDescription(responseCode).getDescription();
    }


}

