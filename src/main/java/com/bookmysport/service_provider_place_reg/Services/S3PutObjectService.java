package com.bookmysport.service_provider_place_reg.Services;

import java.io.File;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.service_provider_place_reg.Models.KeyPath;
import com.bookmysport.service_provider_place_reg.Models.ResponseMessage;
import com.bookmysport.service_provider_place_reg.StaticData.S3Data;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class S3PutObjectService {

    @Autowired
    private ResponseMessage responseMessage;

    public boolean checkObjectInBucket(String spId,String bucketName, String key) {
        S3Client s3Client = S3Data.s3Client;

        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ResponseEntity<ResponseMessage> preSignedURLService(String spId,String key) {
        S3Presigner s3Client = S3Presigner.builder().region(S3Data.region).build();

        try {
            if (checkObjectInBucket(spId,S3Data.bucketName, key)) {
                GetObjectRequest request = GetObjectRequest.builder()
                        .bucket(S3Data.bucketName)
                        .key(key)
                        .build();

                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofHours(10))
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
            responseMessage.setMessage("Internal Server Error "+e.getMessage());
            return ResponseEntity.badRequest().body(responseMessage);
        }
    }

    public ResponseEntity<ResponseMessage> putObjectService(String spId, KeyPath keyPath) {
        S3Client client = S3Data.s3Client;

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(S3Data.bucketName)
                    .key(spId + '/' + keyPath.getKey())
                    .build();

            client.putObject(putOb, RequestBody.fromFile(new File(keyPath.getPath())));

            responseMessage.setSuccess(true);
            responseMessage.setMessage(preSignedURLService(spId,spId + '/' + keyPath.getKey()).getBody().getMessage());

            return ResponseEntity.ok().body(responseMessage);

        } catch (Exception e) {
            responseMessage.setSuccess(false);
            System.out.println(e.getMessage());
            responseMessage.setMessage("Object " + spId + '/' + keyPath.getKey() + " insertion falied");

            return ResponseEntity.badRequest().body(responseMessage);
        }
    }
}
