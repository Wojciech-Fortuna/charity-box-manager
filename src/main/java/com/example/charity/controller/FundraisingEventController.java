package com.example.charity.controller;

import com.example.charity.dto.CreateFundraisingEventDto;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.service.FundraisingEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class FundraisingEventController {

    private final FundraisingEventService fundraisingEventService;

    @PostMapping
    public FundraisingEvent createEvent(@RequestBody @Valid CreateFundraisingEventDto dto) {
        FundraisingEvent event = new FundraisingEvent();
        event.setName(dto.name());
        event.setCurrency(dto.currency());
        event.setAccountAmount(BigDecimal.ZERO);
        return fundraisingEventService.createEvent(event);
    }

    @GetMapping
    public List<FundraisingEvent> getAllEvents() {
        return fundraisingEventService.getAllEvents();
    }
}
