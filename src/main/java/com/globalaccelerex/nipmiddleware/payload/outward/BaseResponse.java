package com.globalaccelerex.nipmiddleware.payload.outward;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

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
        this.responseDescription = NIPResponseCodeEnum.getResponseCodeEnum(responseCode).getDescription();
    }

    public void setResponse(NIPResponseCodeEnum nipResponseCodeEnum){
        this.responseCode = nipResponseCodeEnum.getCode();
        this.responseDescription = nipResponseCodeEnum.getDescription();
    }



}

