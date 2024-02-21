package com.bookmysport.service_provider_place_reg.Repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookmysport.service_provider_place_reg.Models.SportsDB;

import jakarta.transaction.Transactional;

public interface SportsDBRepo extends JpaRepository<SportsDB, UUID> {
    List<SportsDB> findBySpId(UUID spId);
    SportsDB findBySportName(String sportName);

    @Transactional
    @Query(value = "SELECT * FROM sports_db WHERE sp_id=:spId AND sport_id=:sportId",nativeQuery = true)
    SportsDB findBySpIdAndSportId(@Param("spId") UUID spId, @Param("sportId") UUID sportId);

    @Transactional
    @Query(value="SELECT * FROM sports_db WHERE sport_name like %?1% ",nativeQuery = true)
    List<SportsDB> findBySearch(String searchItem);

    @Transactional
    @Query(value = "SELECT * FROM sports_db WHERE sp_id=:spId AND sport_name =:sportName",nativeQuery = true)
    SportsDB findBySpIdAndSportName(@Param("spId") UUID spId, @Param("sportName") String sportName);
}
