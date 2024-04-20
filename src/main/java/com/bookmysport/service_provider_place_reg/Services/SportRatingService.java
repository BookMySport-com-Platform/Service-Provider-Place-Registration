package com.bookmysport.service_provider_place_reg.Services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Models.SportsDB;
import com.bookmysport.service_provider_place_reg.Repositories.SportsDBRepo;

@Service
public class SportRatingService {

    @Autowired
    private SportsDBRepo sportsDBRepo;

    @Autowired
    private ResponseMessage responseMessage;

    public ResponseEntity<ResponseMessage> sportRatingService(String spId, String sportId, float rating) {
        try {
            if (rating > 5 || rating < 0) {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Rating cannot exceed 5 and cannot be less than 0");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(responseMessage);
            } else {
                SportsDB sportDetails = sportsDBRepo.findBySpIdAndSportId(UUID.fromString(spId),
                        UUID.fromString(sportId));
                if (sportDetails != null) {

                    if (sportDetails.getNumberOfRatings() == 0) {
                        sportDetails.setRating(Math.round(rating * 10.0) / 10.0);
                        sportDetails.setRatingSum(rating);
                        sportDetails.setNumberOfRatings(1);
                    } else {
                        int totalNumberOfRatings = sportDetails.getNumberOfRatings() + 1;
                        sportDetails.setRatingSum(sportDetails.getRatingSum() + rating);
                        sportDetails.setNumberOfRatings(totalNumberOfRatings);
                        sportDetails.setRating(
                                Math.round((sportDetails.getRatingSum() / totalNumberOfRatings) * 10.0) / 10.0);
                    }

                    sportsDBRepo.save(sportDetails);

                    responseMessage.setSuccess(true);
                    responseMessage
                            .setMessage(
                                    "Rating added and number of ratings are: " + sportDetails.getNumberOfRatings());
                } else {
                    responseMessage.setSuccess(false);
                    responseMessage.setMessage("Invalid SpID");
                    return ResponseEntity.ok().body(responseMessage);
                }

                return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
            }
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage(
                    "Internal Server Error in PlaygroundRating.java. Method: playgroundRatingService. Reason: "
                            + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);

        }
    }
}