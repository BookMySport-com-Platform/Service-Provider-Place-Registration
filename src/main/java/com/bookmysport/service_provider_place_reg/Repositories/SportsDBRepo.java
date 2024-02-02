package com.bookmysport.service_provider_place_reg.Repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmysport.service_provider_place_reg.Models.SportsDB;

public interface SportsDBRepo extends JpaRepository<SportsDB, UUID> {
    List<SportsDB> findBySpId(UUID spId);
    SportsDB findBySportName(String sportName);
}
