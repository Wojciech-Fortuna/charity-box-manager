package com.example.charity.dto;

import java.math.BigDecimal;

public record FinancialReportDto(
        String name,
        BigDecimal amount,
        String currency
) {}
