package com.example.charity.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CurrencyApiResponse {

    private final Map<String, Map<String, BigDecimal>> currencies = new HashMap<>();

    @JsonAnySetter
    public void set(String currencyCode, Object value) {
        if (value instanceof Map<?, ?> nestedMap) {
            Map<String, BigDecimal> parsedRates = new HashMap<>();
            for (Map.Entry<?, ?> entry : nestedMap.entrySet()) {
                Object key = entry.getKey();
                Object rateValue = entry.getValue();
                if (key instanceof String && rateValue instanceof Number) {
                    parsedRates.put((String) key, new BigDecimal(rateValue.toString()));
                }
            }
            currencies.put(currencyCode.toLowerCase(), parsedRates);
        }
    }
}
