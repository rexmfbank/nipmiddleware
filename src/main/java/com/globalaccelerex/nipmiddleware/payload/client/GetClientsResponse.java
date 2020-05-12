package com.globalaccelerex.nipmiddleware.payload.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetClientsResponse {

    private List<ClientDetail> clientDetailList;

    private int totalPages;
    private long totalElements;
    private boolean hasPrevious;
    private boolean hasNext;
    private boolean isLast;
    private boolean isFirst;
    private boolean hasContent;
    private int numberOfElement;
    private int size;
}
