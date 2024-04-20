package com.bookmysport.service_provider_place_reg.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bookmysport.service_provider_place_reg.MiddleWares.GetSPDetailsMW;
import com.bookmysport.service_provider_place_reg.Models.ImagesDB;
import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Repositories.ImagesDBRepo;

@Service
public class ImageUploadService {

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;

    @Autowired
    private ImagesDBRepo imagesDBRepo;

    @Autowired
    private S3PutObjectService s3PutObjectService;

    @Autowired
    private ResponseMessage responseMessage;

    public ResponseEntity<ResponseMessage> uploadImageService(String token, String role, List<MultipartFile> images) {
        try {

            String failedImages = "";

            for (int i = 0; i < images.size(); i++) {
                
                String spId = getSPDetailsMW.getSPDetailsByToken(token, role).getBody().getMessage();

                ImagesDB imagesDB = new ImagesDB();

                UUID imageUUID = UUID.randomUUID();
                UUID key=imageUUID;
                imagesDB.setImageId(imageUUID);

                imagesDB.setSpId(UUID.fromString(spId));

                ResponseMessage messageFromPutObjectService = s3PutObjectService.putObjectService(spId, key.toString(),images.get(i)).getBody();

                if (messageFromPutObjectService.getSuccess()) {
                    imagesDB.setImageURL(messageFromPutObjectService.getMessage());
                    imagesDBRepo.save(imagesDB);
                } else {
                    failedImages += images.get(i).getOriginalFilename();
                    failedImages += ", ";
                    failedImages+= "Reason: "+messageFromPutObjectService.getMessage();
                }
            }

            if (failedImages.length() == 0) {
                responseMessage.setSuccess(true);
                responseMessage.setMessage("Upload successfully!");
                return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
            } else {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Upload failed for: " + failedImages);
                return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
            }

        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error inside ImageUploadService.java " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }

    }
}
