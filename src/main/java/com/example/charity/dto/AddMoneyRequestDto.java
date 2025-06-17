package com.example.charity.dto;

import com.example.charity.model.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AddMoneyRequestDto(
        @NotNull Long boxId,
        @NotNull Currency currency,
        @NotNull @DecimalMin("0.01") BigDecimal amount
) {}
