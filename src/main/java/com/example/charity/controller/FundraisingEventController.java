package com.example.charity.controller;

import com.example.charity.model.FundraisingEvent;
import com.example.charity.service.FundraisingEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class FundraisingEventController {

    private final FundraisingEventService fundraisingEventService;

    @PostMapping
    public FundraisingEvent createEvent(@RequestBody FundraisingEvent event) {
        return fundraisingEventService.createEvent(event);
    }

    @GetMapping
    public List<FundraisingEvent> getAllEvents() {
        return fundraisingEventService.getAllEvents();
    }
}
