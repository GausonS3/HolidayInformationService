package com.bluestone.holiday.service;

import com.bluestone.holiday.exceptions.TechnicalException;
import com.bluestone.holiday.model.CountryDto;
import com.bluestone.holiday.model.PublicHolidayDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class NagerApi {

    private final WebClient webClient;

    public NagerApi(WebClient.Builder webClientBuilder, @Value("${nager-api.address}") final String apiAddress) {
        this.webClient = webClientBuilder.baseUrl(apiAddress).build();
    }

    public List<CountryDto> getAvailableCountryCodes() {
        log.info("Getting country codes from API");
        Mono<List<CountryDto>> response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/AvailableCountries").build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});

        return response
                .onErrorResume(e -> Mono.error(
                        new TechnicalException("Holidays API did not respond, please try again later", e.getMessage())))
                .block();
    }

    public List<PublicHolidayDto> getHolidaysFor(int year, String country) {
        log.info("Getting holidays from API for {} {}", year, country);
        Mono<List<PublicHolidayDto>> response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/PublicHolidays/{year}/{countryCode}").build(year, country))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});

        return response
                .onErrorResume(e -> Mono.error(
                        new TechnicalException("Holidays API did not respond correctly, please try again later", e.getMessage())))
                .block();
    }
}
