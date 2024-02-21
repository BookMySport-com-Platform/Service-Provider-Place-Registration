package com.bookmysport.service_provider_place_reg.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.service_provider_place_reg.MiddleWares.GetSPDetailsMW;
import com.bookmysport.service_provider_place_reg.Models.PriceSportCourts;
import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Models.SportsDB;
import com.bookmysport.service_provider_place_reg.Repositories.SportsDBRepo;

@Service
public class UploadSportsService {

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;

    @Autowired
    private ResponseMessage responseMessage;

    @Autowired
    private SportsDBRepo sportsDBRepo;

    public ResponseEntity<ResponseMessage> uploadSportsInfoService(List<PriceSportCourts> pricePerSport, String token,
            String role) {
        try {
            ResponseEntity<ResponseMessage> spId = getSPDetailsMW.getSPDetailsByToken(token, role);

            String duplicatesMessage = "";

            for (int i = 0; i < pricePerSport.size(); i++) {

                SportsDB sportExistence=sportsDBRepo.findBySpIdAndSportName(UUID.fromString(spId.getBody().getMessage()),pricePerSport.get(i).getSport());

                if (sportExistence == null) {
                    SportsDB sportsdb = new SportsDB();
                    sportsdb.setSpId(UUID.fromString(spId.getBody().getMessage()));
                    sportsdb.setSportName(pricePerSport.get(i).getSport());
                    sportsdb.setPricePerHour(pricePerSport.get(i).getPrice());
                    sportsdb.setNumberOfCourts(pricePerSport.get(i).getNumberOfCourts());
                    sportsDBRepo.save(sportsdb);
                } else {
                    duplicatesMessage += sportsDBRepo.findBySportName(pricePerSport.get(i).getSport()).getSportName();
                    duplicatesMessage += ", ";
                }

            }

            if (duplicatesMessage.length() == 0) {
                responseMessage.setSuccess(true);
                responseMessage.setMessage("Sports registered successfully");
                return ResponseEntity.ok().body(responseMessage);
            } else {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Sports registered successfully but these " + duplicatesMessage
                        + " already exists, hence not been added.");
                return ResponseEntity.ok().body(responseMessage);
            }

        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }

    }
}
