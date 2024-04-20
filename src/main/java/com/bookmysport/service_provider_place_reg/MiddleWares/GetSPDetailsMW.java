package com.bookmysport.service_provider_place_reg.MiddleWares;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Models.ServiceProviderModel;

@Service
public class GetSPDetailsMW {

    @Value("${AUTH_SERVICE_URL}")
    String authServiceUrl;

    @Autowired
    private ResponseMessage responseMessage;

    private final RestTemplate restTemplate;

    @Autowired
    public GetSPDetailsMW(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<ResponseMessage> getSPDetailsByToken(String token, String role) {
        try {

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.set("token", token);
            requestHeaders.set("role", role);

            HttpEntity<?> orderRequest = new HttpEntity<>(requestHeaders);

            String url = System.getenv("AUTH_SERVICE_URL");

            ResponseEntity<ServiceProviderModel> response = restTemplate.exchange(url, HttpMethod.GET, orderRequest,
                    ServiceProviderModel.class);

            responseMessage.setSuccess(true);
            responseMessage.setMessage(response.getBody().getId());
            
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);

        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }
}
