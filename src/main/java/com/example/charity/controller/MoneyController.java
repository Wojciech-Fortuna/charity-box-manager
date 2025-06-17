package com.example.charity.controller;

import com.example.charity.dto.AddMoneyRequestDto;
import com.example.charity.model.CollectionBox;
import com.example.charity.service.CollectionBoxService;
import com.example.charity.service.MoneyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/money")
@RequiredArgsConstructor
public class MoneyController {

    private final MoneyService moneyService;
    private final CollectionBoxService collectionBoxService;

    @PostMapping("/add")
    public void addMoneyToBox(@RequestBody @Valid AddMoneyRequestDto dto) {
        CollectionBox box = collectionBoxService.getBox(dto.boxId());
        moneyService.addMoneyToBox(box, dto.currency(), dto.amount());
    }
}
