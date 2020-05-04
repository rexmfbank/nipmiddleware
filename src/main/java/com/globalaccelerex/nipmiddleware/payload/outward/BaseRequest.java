package com.globalaccelerex.nipmiddleware.payload.outward;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globalaccelerex.nipmiddleware.annotation.IsAlphaNumeric;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequest {

    @JsonIgnore
    private IMarker marker;

    @IsAlphaNumeric
    @NotEmpty(message = "clientId is required")
    private String clientId;

}