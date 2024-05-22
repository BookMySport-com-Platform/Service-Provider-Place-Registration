package com.bookmysport.service_provider_place_reg.Controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bookmysport.service_provider_place_reg.MiddleWares.GetSPDetailsMW;
import com.bookmysport.service_provider_place_reg.Models.ImagesDB;
import com.bookmysport.service_provider_place_reg.Models.PriceSportCourts;
import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Models.SportsDB;
import com.bookmysport.service_provider_place_reg.Repositories.ImagesDBRepo;
import com.bookmysport.service_provider_place_reg.Repositories.SportsDBRepo;
import com.bookmysport.service_provider_place_reg.Services.DeleteImagesService;
import com.bookmysport.service_provider_place_reg.Services.DeleteSport;
import com.bookmysport.service_provider_place_reg.Services.FetchSportsImages;
import com.bookmysport.service_provider_place_reg.Services.ImageUploadService;
import com.bookmysport.service_provider_place_reg.Services.SearchPlaygroundService;
import com.bookmysport.service_provider_place_reg.Services.SportRatingService;
import com.bookmysport.service_provider_place_reg.Services.UpdateSportDetials;
import com.bookmysport.service_provider_place_reg.Services.UploadSportsService;

@RestController
@RequestMapping("api")
public class SPMainController {

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;

    @Autowired
    private UploadSportsService uploadSportsService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FetchSportsImages fetchSportsImages;

    @Autowired
    private UpdateSportDetials updateSportDetials;

    @Autowired
    private DeleteImagesService deleteImagesService;

    @Autowired
    private SearchPlaygroundService searchPlaygroundService;

    @Autowired
    private DeleteSport deleteSportService;

    @Autowired
    private SportRatingService sportRatingService;

    @Autowired
    private SportsDBRepo sportsDBRepo;

    @Autowired
    private ImagesDBRepo imagesDBRepo;

    @GetMapping("getdetails")
    public ResponseEntity<ResponseMessage> getSPDetaills(@RequestHeader String token, @RequestHeader String role) {
        return getSPDetailsMW.getSPDetailsByToken(token, role);
    }

    @PostMapping("uploadsports")
    public ResponseEntity<ResponseMessage> uploadSports(@RequestHeader String token, @RequestHeader String role,
            @RequestBody List<PriceSportCourts> pricePerSport) {
        return uploadSportsService.uploadSportsInfoService(pricePerSport, token, role);
    }

    @PostMapping("uploadimages")
    public ResponseEntity<ResponseMessage> uploadImages(@RequestHeader String token, @RequestHeader String role,
            @RequestBody List<MultipartFile> images) {
        return imageUploadService.uploadImageService(token, role, images);
    }

    @GetMapping("getsportsforsp")
    public List<SportsDB> getAllSportsForSP(@RequestHeader String token, @RequestHeader String role) {
        return fetchSportsImages.fetchSportsService(token, role);
    }

    @GetMapping("getimagesforsp")
    public List<ImagesDB> getAllImagesForSP(@RequestHeader String token, @RequestHeader String role) {
        return fetchSportsImages.fetchImagesService(token, role);
    }

    @GetMapping("getsportbyspidandsportid")
    public ResponseEntity<Object> getSportBySportIdAndSpId(@RequestHeader String spId, @RequestHeader String sportId) {
        return fetchSportsImages.fetchSportBySportIdAndSpIdService(spId, sportId);
    }

    @PutMapping("updatesportdetails")
    public ResponseEntity<ResponseMessage> updateSportDetails(@RequestBody SportsDB latestsportDetails) {
        return updateSportDetials.updateSportDetailsService(latestsportDetails);
    }

    @DeleteMapping("deleteimage")
    public ResponseEntity<ResponseMessage> deleteImage(@RequestBody ImagesDB imageInfo) {
        return deleteImagesService.deleteImageService(imageInfo);
    }

    @GetMapping("getbysportname")
    public List<Object> searchBySportName(@RequestHeader String searchItem) {
        return searchPlaygroundService.searchBySportName(searchItem);
    }

    @DeleteMapping("deletesport")
    public ResponseEntity<ResponseMessage> deleteSport(@RequestHeader String token, @RequestHeader String role,
            @RequestHeader UUID sportId) {

        return deleteSportService.deleteSport(token, role, sportId);
    }

    @PostMapping("addsportrating")
    public ResponseEntity<ResponseMessage> addSportRating(@RequestHeader String spId, @RequestHeader String sportId,
            @RequestHeader float rating) {
        return sportRatingService.sportRatingService(spId, sportId, rating);
    }

    @GetMapping("getsports")
    public List<SportsDB> getAllSports(@RequestHeader String spId) {
        return sportsDBRepo.findBySpId(UUID.fromString(spId));
    }

    @GetMapping("getimages")
    public List<ImagesDB> getAllImages(@RequestHeader String spId) {
        return imagesDBRepo.findBySpId(UUID.fromString(spId));
    }

}
