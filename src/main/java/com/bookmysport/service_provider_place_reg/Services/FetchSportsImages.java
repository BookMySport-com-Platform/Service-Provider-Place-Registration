package com.bookmysport.service_provider_place_reg.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<SportsDB> fetchSportsService(String token, String role) {

        ResponseEntity<ResponseMessage> spId = getSPDetailsMW.getSPDetailsByToken(token, role);
        List<SportsDB> sports = sportsDBRepo.findBySpId(UUID.fromString(spId.getBody().getMessage()));
        return sports;
    }

    public List<ImagesDB> fetchImagesService(String token, String role)
    {
        ResponseEntity<ResponseMessage> spId = getSPDetailsMW.getSPDetailsByToken(token, role);
        List<ImagesDB> images=imagesDBRepo.findBySpId(UUID.fromString(spId.getBody().getMessage()));
        return images;
    }
    
}
