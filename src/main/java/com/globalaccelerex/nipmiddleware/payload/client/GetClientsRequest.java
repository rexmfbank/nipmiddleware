package com.globalaccelerex.nipmiddleware.payload.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;

@Data
@ToString(callSuper = true)
public class GetClientsRequest  {

    @JsonIgnore
    private IMarker marker;

    private String startWith;

    @Min(value = 0 ,message = "size cannot be less than zero")
    private int size ;

    @Min(value = 0 ,message = "page index cannot be less than zero")
    private int pageIndex;
}
