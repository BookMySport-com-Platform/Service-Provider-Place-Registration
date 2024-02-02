package com.bookmysport.service_provider_place_reg.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.service_provider_place_reg.MiddleWares.GetSPDetailsMW;
import com.bookmysport.service_provider_place_reg.Models.ImagesDB;
import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Models.SportsDB;
import com.bookmysport.service_provider_place_reg.Repositories.ImagesDBRepo;
import com.bookmysport.service_provider_place_reg.Repositories.SportsDBRepo;

@Service
public class FetchSportsImages {

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;

    @Autowired
    private SportsDBRepo sportsDBRepo;

    @Autowired
    private ImagesDBRepo imagesDBRepo;

    @Autowired
    private ResponseMessage responseMessage;

    public List<SportsDB> fetchSportsService(String token, String role) {

        ResponseEntity<ResponseMessage> spId = getSPDetailsMW.getSPDetailsByToken(token, role);
        List<SportsDB> sports = sportsDBRepo.findBySpId(UUID.fromString(spId.getBody().getMessage()));
        return sports;
    }

    public List<ImagesDB> fetchImagesService(String token, String role) {
        ResponseEntity<ResponseMessage> spId = getSPDetailsMW.getSPDetailsByToken(token, role);
        List<ImagesDB> images = imagesDBRepo.findBySpId(UUID.fromString(spId.getBody().getMessage()));
        return images;
    }

    public ResponseEntity<Object> fetchSportBySportIdAndSpIdService(String token, String role, String sportId) {
        try {
            ResponseEntity<ResponseMessage> spId = getSPDetailsMW.getSPDetailsByToken(token, role);

            SportsDB sortedBySportIdAndSpId = sportsDBRepo
                    .findBySpIdAndSportId(UUID.fromString(spId.getBody().getMessage()), UUID.fromString(sportId));

            if (sortedBySportIdAndSpId != null) {
                return ResponseEntity.ok().body(sortedBySportIdAndSpId);
            } else {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Sport with this id doesn't exists.");
                return ResponseEntity.badRequest().body(responseMessage);
            }

        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }

    }

}
