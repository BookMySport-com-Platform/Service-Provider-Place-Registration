package com.bookmysport.service_provider_place_reg.Models;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ResponseMessage {
    private Boolean success;
    private String message;
}
