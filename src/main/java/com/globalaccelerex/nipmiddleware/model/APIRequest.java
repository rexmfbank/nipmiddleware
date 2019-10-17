/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIRequest {
    @JsonIgnore
    private IMarker marker;
}
