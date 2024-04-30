package com.bookmysport.service_provider_place_reg.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmysport.service_provider_place_reg.Models.ImagesDB;
import java.util.List;
import java.time.LocalDate;



public interface ImagesDBRepo extends JpaRepository<ImagesDB, UUID> {
    List<ImagesDB> findBySpId(UUID spId);
    List<ImagesDB> findByDateOfGenration(LocalDate dateOfGenration);
}

