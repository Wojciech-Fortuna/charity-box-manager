package com.example.charity.controller;

import com.example.charity.model.CollectionBox;
import com.example.charity.service.CollectionBoxService;
import com.example.charity.service.MoneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/money")
@RequiredArgsConstructor
public class MoneyController {

    private final MoneyService moneyService;
    private final CollectionBoxService collectionBoxService;

    @PostMapping("/add")
    public void addMoneyToBox(@RequestBody Map<String, String> request) {
        Long boxId = Long.parseLong(request.get("boxId"));
        String currency = request.get("currency");
        BigDecimal amount = new BigDecimal(request.get("amount"));

        CollectionBox box = collectionBoxService.getBox(boxId);
        moneyService.addMoneyToBox(box, currency, amount);
    }
}
