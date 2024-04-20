package com.bookmysport.service_provider_place_reg.Models;

import lombok.Data;

@Data
public class ServiceProviderModel {
    private String id;

    private String userName;

    private String email;

    private long phoneNumber;

    private String password;

    private String address;

    private String centreName;

    private int startTime;

    private int stopTime;

    private double rating;

    private int numberOfRatings;

    private float ratingSum;
}
