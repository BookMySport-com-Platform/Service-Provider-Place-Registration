package com.bookmysport.service_provider_place_reg.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Models.SportsDB;
import com.bookmysport.service_provider_place_reg.Repositories.SportsDBRepo;

@Service
public class UpdateSportDetials {

    @Autowired
    private SportsDBRepo sportsDBRepo;

    @Autowired
    private ResponseMessage responseMessage;

    public ResponseEntity<ResponseMessage> updateSportDetailsService(SportsDB latestsportDetails) {
        try {
            SportsDB sportToBeUpdated = sportsDBRepo.findBySpIdAndSportId(latestsportDetails.getSpId(),
                    latestsportDetails.getSportId());

            if (sportToBeUpdated == null) {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Sport with sportId: " + latestsportDetails.getSportId() + " and with spId: "
                        + latestsportDetails.getSpId() + " doesn't exist.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
            } else {
                sportToBeUpdated.setSportName(latestsportDetails.getSportName());
                sportToBeUpdated.setPricePerHour(latestsportDetails.getPricePerHour());
                sportToBeUpdated.setNumberOfCourts(latestsportDetails.getNumberOfCourts());
                sportsDBRepo.save(sportToBeUpdated);

                responseMessage.setSuccess(true);
                responseMessage.setMessage("Sport details updated successfully");
                return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
            }
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage
                    .setMessage("Internal Server Error in method updateSportDetailsService. Reason: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }

    }
}
