package com.example.charity.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCollectionBoxDto(
        @NotBlank String identifier
) {}
