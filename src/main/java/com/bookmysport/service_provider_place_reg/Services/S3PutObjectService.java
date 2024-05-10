package com.bookmysport.service_provider_place_reg.Services;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bookmysport.service_provider_place_reg.Models.ImagesDB;
import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.Repositories.ImagesDBRepo;
import com.bookmysport.service_provider_place_reg.StaticData.S3Data;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class S3PutObjectService {

    @Autowired
    private ResponseMessage responseMessage;

    @Autowired
    private ImagesDBRepo imagesDBRepo;

    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredRecords() {
        LocalDate expiryTime = LocalDate.now();

        List<ImagesDB> imagesFromDB = imagesDBRepo.findAll();
        String folderName = System.getenv("FOLDER_FOR_SERVICE_PROVIDER_IMAGES");

        for (int i = 0; i < imagesFromDB.size(); i++) {

            if(imagesFromDB.get(i).getDateOfGenration().isBefore(expiryTime))
            {
                String key = folderName + '/' + imagesFromDB.get(i).getSpId() + '/' + imagesFromDB.get(i).getImageId();
                ResponseEntity<ResponseMessage> newPresignedURL = preSignedURLService(
                        imagesFromDB.get(i).getSpId().toString(), key);
                imagesFromDB.get(i).setImageURL(newPresignedURL.getBody().getMessage());
                imagesFromDB.get(i).setDateOfGenration(LocalDate.now());
                imagesDBRepo.save(imagesFromDB.get(i));
            }            

        }
    }

    public boolean checkObjectInBucket(String bucketName, String key) {
        S3Client s3Client = S3Data.s3Client;

        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ResponseEntity<ResponseMessage> preSignedURLService(String spId, String key) {
        S3Presigner s3Client = S3Presigner.builder().region(S3Data.region).build();

        try {
            if (checkObjectInBucket(S3Data.bucketName, key)) {
                GetObjectRequest request = GetObjectRequest.builder()
                        .bucket(S3Data.bucketName)
                        .key(key)
                        .build();

                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofHours(12))
                        .getObjectRequest(request)
                        .build();

                PresignedGetObjectRequest presignedGetObjectRequest = s3Client.presignGetObject(presignRequest);

                responseMessage.setSuccess(true);
                responseMessage.setMessage(presignedGetObjectRequest.url().toString());

                return ResponseEntity.ok().body(responseMessage);
            } else {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Object '" + key + "' does not exists!");
                return ResponseEntity.ok().body(responseMessage);
            }
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error " + e.getMessage());
            return ResponseEntity.ok().body(responseMessage);
        }
    }

    public ResponseEntity<ResponseMessage> putObjectService(String spId, String key, MultipartFile image) {
        S3Client client = S3Data.s3Client;

        String folderName = System.getenv("FOLDER_FOR_SERVICE_PROVIDER_IMAGES");
        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(S3Data.bucketName)
                    .key(folderName + '/' + spId + '/' + key)
                    .contentType(image.getContentType())
                    .build();

            RequestBody requestBody = RequestBody.fromInputStream(image.getInputStream(), image.getSize());

            PutObjectResponse response = client.putObject(putOb, requestBody);

            if (response.eTag().isEmpty()) {
                responseMessage.setSuccess(false);
                responseMessage
                        .setMessage("Object " + folderName + '/' + spId + '/' + key + " insertion falied "
                                + response.eTag());
                return ResponseEntity.ok().body(responseMessage);
            } else {
                responseMessage.setSuccess(true);
                responseMessage
                        .setMessage(
                                preSignedURLService(spId, folderName + '/' + spId + '/' + key).getBody().getMessage());

                return ResponseEntity.ok().body(responseMessage);
            }

        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage(
                    "Object " + folderName + '/' + spId + '/' + key + " insertion falied " + e.getMessage());
            return ResponseEntity.ok().body(responseMessage);
        }
    }
}