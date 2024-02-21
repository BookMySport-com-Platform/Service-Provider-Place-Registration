package com.bookmysport.service_provider_place_reg.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookmysport.service_provider_place_reg.MiddleWares.GetSearchFromAS;
import com.bookmysport.service_provider_place_reg.Models.SportsDB;
import com.bookmysport.service_provider_place_reg.Repositories.SportsDBRepo;

@Service
public class SearchPlaygroundService {

    @Autowired
    private SportsDBRepo sportsDBRepo;

    @Autowired
    private GetSearchFromAS getSearchFromAS;

    public List<Object> searchBySportName(String searchItem) {
        List<Object> searchFromAS = getSearchFromAS.getSearchFromASService(searchItem);
        
        List<SportsDB> searchResults = sportsDBRepo.findBySearch(searchItem);

        List<Object> fetchedResultsFromAS = new ArrayList<>();

        for (int i = 0; i < searchResults.size(); i++) {
            fetchedResultsFromAS.add(getSearchFromAS.getSpIdInfoFromAS(searchResults.get(i).getSpId().toString()));
        }

        List<Object> combinedResults = new ArrayList<>();
        combinedResults.addAll(searchFromAS);
        combinedResults.addAll(fetchedResultsFromAS);

        return combinedResults;
    }
}
