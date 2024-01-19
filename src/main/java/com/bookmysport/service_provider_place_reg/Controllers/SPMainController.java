package com.bookmysport.service_provider_place_reg.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmysport.service_provider_place_reg.MiddleWares.GetSPDetailsMW;
import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Services.ImageUploadService;
import com.bookmysport.service_provider_place_reg.Services.UploadSportsService;

@RestController
@RequestMapping("spr/api")
public class SPMainController {

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;

    @Autowired
    private UploadSportsService uploadSportsService;

    @Autowired
    private ImageUploadService imageUploadService;

    @GetMapping("getdetails")
    public ResponseEntity<ResponseMessage> getSPDetaills(@RequestHeader String token, @RequestHeader String role) {
        return getSPDetailsMW.getSPDetailsByToken(token, role);
    }

    @PostMapping("uploadsports")
    public ResponseEntity<ResponseMessage> uploadSports(@RequestHeader String token, @RequestHeader String role,
            @RequestHeader List<String> sports) {
        return uploadSportsService.uploadSportsInfoService(sports, token, role);
    }

    @PostMapping("uploadimages")
    public ResponseEntity<ResponseMessage> uploadImages(@RequestHeader String token, @RequestHeader String role,@RequestHeader List<String> imagePaths)
    {
        return imageUploadService.uploadImageService(token,role,imagePaths);
    }
}
