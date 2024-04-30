package com.bookmysport.service_provider_place_reg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableScheduling
public class ServiceProviderPlaceRegApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceProviderPlaceRegApplication.class, args);
	}

	@Bean
    WebClient webClient() {
        return WebClient.builder().build();
    }

}
