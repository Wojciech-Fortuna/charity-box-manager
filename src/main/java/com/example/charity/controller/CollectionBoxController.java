package com.example.charity.controller;

import com.example.charity.model.CollectionBox;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.service.CollectionBoxService;
import com.example.charity.service.FundraisingEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boxes")
@RequiredArgsConstructor
public class CollectionBoxController {

    private final CollectionBoxService collectionBoxService;
    private final FundraisingEventService fundraisingEventService;

    @PostMapping
    public CollectionBox registerBox(@RequestBody CollectionBox box) {
        return collectionBoxService.registerNewBox(box);
    }

    @GetMapping
    public List<CollectionBox> getAllBoxes() {
        return collectionBoxService.listAllBoxes();
    }

    @DeleteMapping("/{id}")
    public void unregisterBox(@PathVariable Long id) {
        collectionBoxService.unregisterBox(id); // w wersji minimum
    }

    @PostMapping("/{boxId}/assign/{eventId}")
    public void assignBoxToEvent(@PathVariable Long boxId, @PathVariable Long eventId) {
        CollectionBox box = collectionBoxService.getBox(boxId);
        FundraisingEvent event = fundraisingEventService.getEventById(eventId);
        collectionBoxService.assignToEvent(box, event);
    }
}
