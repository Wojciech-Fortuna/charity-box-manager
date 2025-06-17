package com.example.charity.controller;

import com.example.charity.dto.CollectionBoxInfoDto;
import com.example.charity.dto.CreateCollectionBoxDto;
import com.example.charity.model.CollectionBox;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.service.CollectionBoxService;
import com.example.charity.service.ExchangeRateService;
import com.example.charity.service.FundraisingEventService;
import com.example.charity.service.MoneyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boxes")
@RequiredArgsConstructor
public class CollectionBoxController {

    private final CollectionBoxService collectionBoxService;
    private final FundraisingEventService fundraisingEventService;
    private final MoneyService moneyService;
    private final ExchangeRateService exchangeRateService;

    @PostMapping
    public CollectionBox registerBox(@RequestBody @Valid CreateCollectionBoxDto dto) {
        CollectionBox box = new CollectionBox();
        box.setIdentifier(dto.identifier());
        return collectionBoxService.registerNewBox(box);
    }

    @GetMapping
    public List<CollectionBoxInfoDto> getAllBoxes() {
        return collectionBoxService.listAllBoxes().stream()
                .map(box -> new CollectionBoxInfoDto(
                        box.getBoxId(),
                        box.getEvent() != null,
                        box.getMoneyList() == null || box.getMoneyList().isEmpty()
                ))
                .toList();
    }

    @DeleteMapping("/{boxId}")
    public void unregisterBox(@PathVariable Long boxId) {
        collectionBoxService.unregisterBox(boxId);
    }

    @PostMapping("/{boxId}/assign/{eventId}")
    public void assignBoxToEvent(@PathVariable Long boxId, @PathVariable Long eventId) {
        CollectionBox box = collectionBoxService.getBox(boxId);
        FundraisingEvent event = fundraisingEventService.getEventById(eventId);
        collectionBoxService.assignToEvent(box, event);
    }

    @PostMapping("/{boxId}/empty")
    public void emptyBox(@PathVariable Long boxId) {
        CollectionBox box = collectionBoxService.getBox(boxId);
        moneyService.emptyBox(box, exchangeRateService);
    }
}
