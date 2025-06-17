package com.example.charity.service;

import com.example.charity.dto.CurrencyApiResponse;
import com.example.charity.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    @Value("${currency.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getRate(Currency from, Currency to) {
        if (from == to) return BigDecimal.ONE;

        String url = apiUrl + "/" + from.name().toLowerCase() + ".json";

        try {
            ResponseEntity<CurrencyApiResponse> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<>() {}
                    );

            CurrencyApiResponse body = response.getBody();
            if (body == null) {
                throw new RuntimeException("No response from exchange API");
            }

            Map<String, Map<String, BigDecimal>> rates = body.getCurrencies();
            BigDecimal rate = rates.getOrDefault(from.name().toLowerCase(), Map.of())
                    .get(to.name().toLowerCase());

            if (rate == null) {
                throw new RuntimeException("Rate not found from " + from + " to " + to);
            }

            return rate;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch exchange rate from " + from + " to " + to, e);
        }
    }
}
