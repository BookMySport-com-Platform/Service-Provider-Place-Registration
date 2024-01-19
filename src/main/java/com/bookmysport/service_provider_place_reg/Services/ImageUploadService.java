package com.bookmysport.service_provider_place_reg.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.service_provider_place_reg.MiddleWares.GetSPDetailsMW;
import com.bookmysport.service_provider_place_reg.Models.ImagesDB;
import com.bookmysport.service_provider_place_reg.Models.KeyPath;
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

    public ResponseEntity<ResponseMessage> uploadImageService(String token, String role, List<String> imagePaths) {
        try {
            ResponseEntity<ResponseMessage> spId = getSPDetailsMW.getSPDetailsByToken(token, role);

            for (int i = 0; i < imagePaths.size(); i++) {
                ImagesDB imagesDB = new ImagesDB();
                UUID imageUUID = UUID.randomUUID();
                imagesDB.setImageId(imageUUID);
                imagesDB.setSpId(UUID.fromString(spId.getBody().getMessage()));

                KeyPath keyPath = new KeyPath();
                keyPath.setKey(imageUUID.toString());
                keyPath.setPath(imagePaths.get(i));

                imagesDB.setImageURL(s3PutObjectService.putObjectService(spId.getBody().getMessage(), keyPath).getBody()
                        .getMessage());

                imagesDBRepo.save(imagesDB);
            }

            responseMessage.setSuccess(true);
            responseMessage.setMessage("Upload successfully!");
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }

    }
}
