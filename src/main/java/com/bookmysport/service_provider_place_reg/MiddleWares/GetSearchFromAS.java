package com.bookmysport.service_provider_place_reg.MiddleWares;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class GetSearchFromAS {

    @Autowired
    private WebClient webClient;

    public List<Object> getSearchFromASService(String searchItem) {
        Mono<List<Object>> userDetailsMono = webClient.get()
                .uri(System.getenv("SEARCH_URL_OF_AS"))
                .headers(headers -> {
                    headers.set("Content-Type", "application/json");
                    headers.set("searchItem", searchItem);
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {
                });

        List<Object> searchResultsFromAS = userDetailsMono.block();
        return searchResultsFromAS;

    }

    public Object getSpIdInfoFromAS(String spId) {
        Mono<Object> userDetailsMono = webClient.get()
                .uri(System.getenv("SEACRH_INFO_BY_SPID"))
                .headers(headers -> {
                    headers.set("Content-Type", "application/json");
                    headers.set("spId", spId);
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Object>() {
                });

        Object spIdInfoFromAS = userDetailsMono.block();
        return spIdInfoFromAS;
    }
}
