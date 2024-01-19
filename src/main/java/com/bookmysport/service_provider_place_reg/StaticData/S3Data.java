package com.bookmysport.service_provider_place_reg.StaticData;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Data {
    public final static Region region = Region.AP_NORTHEAST_3;
    public final static S3Client s3Client = S3Client.builder().region(region).build();
    public final static String bucketName="bookmysport";
}
