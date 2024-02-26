package com.bookmysport.service_provider_place_reg.Models;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Component
@Table(name = "sports_db")
public class SportsDB {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID sportId;

    private String sportName;
    
    private UUID spId;

    @NotNull
    private int pricePerHour;

    @NotNull
    private int numberOfCourts;

    private double rating;

    private int numberOfRatings;

    private float ratingSum;
}
