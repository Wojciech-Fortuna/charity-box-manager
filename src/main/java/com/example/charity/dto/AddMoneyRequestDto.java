package com.example.charity.dto;

import java.math.BigDecimal;

public record AddMoneyRequestDto(Long boxId, String currency, BigDecimal amount) {}

