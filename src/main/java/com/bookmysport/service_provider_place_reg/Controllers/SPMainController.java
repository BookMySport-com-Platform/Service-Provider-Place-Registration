package com.bookmysport.service_provider_place_reg.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmysport.service_provider_place_reg.MiddleWares.GetSPDetailsMW;

@RestController
@RequestMapping("api")
public class SPMainController {

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;
    
    @GetMapping("getdetails")
    public ResponseEntity<Object> getSPDetaills(@RequestHeader String token,@RequestHeader String role)
    {
        return getSPDetailsMW.getSPDetailsByToken(token,role);
    }
}
