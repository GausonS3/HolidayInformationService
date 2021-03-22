package com.bluestone.holiday.service;

import com.bluestone.holiday.model.CountryDto;
import com.bluestone.holiday.model.PublicHolidayDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class NagerApiTest {

    public static MockWebServer mockBackEnd;

    private NagerApi nagerApi;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());

        nagerApi = new NagerApi(WebClient.builder(), baseUrl);
    }

    @Test
    void getAvailableCountryCodes() throws Exception {
        // given
        CountryDto pl = new CountryDto("PL", "Poland");
        CountryDto cn = new CountryDto("CN", "China");

        List<CountryDto> availableCountryCodes = List.of(pl, cn);

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(availableCountryCodes))
                .addHeader("Content-Type", "application/json"));

        // when
        List<CountryDto> result = nagerApi.getAvailableCountryCodes();

        // then
        assertEquals(result, availableCountryCodes);
    }

    @Test
    void getHolidaysFor() throws Exception {
        // given
        int year = 2020;
        String countryCode = "PL";
        List<PublicHolidayDto> holidays = new ArrayList<>();

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(holidays))
                .addHeader("Content-Type", "application/json"));
        // when
        List<PublicHolidayDto> result = nagerApi.getHolidaysFor(year, countryCode);

        // then
        assertEquals(holidays, result);
    }
}
