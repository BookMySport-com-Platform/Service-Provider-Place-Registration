package com.bookmysport.service_provider_place_reg.StaticData;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Data {

    public final static Region region = Region.AP_SOUTH_1;
    public final static S3Client s3Client;
    static {
        s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(() -> AwsBasicCredentials.create(System.getenv("AWS_ACCESS_KEY_ID"), System.getenv("AWS_SECRET_ACCESS_KEY")))
                .build();
    }
    public final static String bucketName = System.getenv("BUCKET_NAME");
}
