package com.example.charity.dto;

import com.example.charity.model.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFundraisingEventDto(
        @NotBlank String name,
        @NotNull Currency currency
) {}
