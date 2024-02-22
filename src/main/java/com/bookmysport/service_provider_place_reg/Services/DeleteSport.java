package com.bookmysport.service_provider_place_reg.Services;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.service_provider_place_reg.MiddleWares.GetSPDetailsMW;
import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Models.SportsDB;
import com.bookmysport.service_provider_place_reg.Repositories.SportsDBRepo;

// @Author - Nikunj Khandelwal
// This Service Class is used to delete the sport information present in the database 
// With respect to the Sport Id provided by the service provider 


@Service
public class DeleteSport {

@Autowired
private SportsDBRepo sportsDBRepo;

@Autowired
private ResponseMessage responseMessage;

@Autowired
private GetSPDetailsMW getSPDetailsMW; 


    public ResponseEntity<ResponseMessage> deleteSport(String token , String role , UUID sportId){
     
        try {
            // Validate the token and role
            ResponseEntity<ResponseMessage> spDetailsResponse = getSPDetailsMW.getSPDetailsByToken(token, role);
            if (spDetailsResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(spDetailsResponse.getStatusCode()).body(spDetailsResponse.getBody());
            }
    
            // Check if the sport exists in the database
            SportsDB sportToDelete = sportsDBRepo.findBySpIdAndSportId(UUID.fromString(spDetailsResponse.getBody().getMessage()),sportId);
            if (sportToDelete == null) {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Sport with ID " + sportId + " does not exist.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
            }
    
            // Delete the sport from the database
            sportsDBRepo.delete(sportToDelete);
            responseMessage.setSuccess(true);
            responseMessage.setMessage("Sport with ID " + sportId + " deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }



    }
}

