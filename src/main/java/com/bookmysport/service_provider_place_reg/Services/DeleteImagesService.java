package com.bookmysport.service_provider_place_reg.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.service_provider_place_reg.Models.ImagesDB;
import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Repositories.ImagesDBRepo;
import com.bookmysport.service_provider_place_reg.StaticData.S3Data;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
public class DeleteImagesService {

    @Autowired
    private ResponseMessage responseMessage;

    @Autowired
    private S3PutObjectService s3PutObjectService;

    @Autowired
    private ImagesDBRepo imagesDBRepo;

    public ResponseEntity<ResponseMessage> deleteImageService(ImagesDB imageInfo) {
        S3Client client = S3Data.s3Client;
        try {
            String bucketName = System.getenv("BUCKET_NAME");
            String serviceProviderFolder=System.getenv("FOLDER_FOR_SERVICE_PROVIDER_IMAGES");
            String key = serviceProviderFolder + '/'+imageInfo.getSpId().toString() + '/' + imageInfo.getImageId();
            if (s3PutObjectService.checkObjectInBucket(bucketName, key)) {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                client.deleteObject(deleteObjectRequest);

                imagesDBRepo.deleteById(imageInfo.getImageId());

                responseMessage.setSuccess(true);
                responseMessage.setMessage("Object '" + key + "' deletion success!");

                return ResponseEntity.status(HttpStatus.OK).body(responseMessage);

            } else {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Object '" + key + "' not found");

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
            }
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage
                    .setMessage("Internal Server Error in DeleteImageService.java. Method: deleteImageService. Reason: "
                            + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);

        }
    }
}
