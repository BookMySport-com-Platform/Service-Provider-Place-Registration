package com.bookmysport.service_provider_place_reg.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmysport.service_provider_place_reg.Models.ImagesDB;

public interface ImagesDBRepo extends JpaRepository<ImagesDB, UUID> {
    
}

