package com.example.charity.service;

import com.example.charity.dto.FinancialReportDto;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.repository.FundraisingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundraisingEventService {

    private final FundraisingEventRepository fundraisingEventRepository;

    public FundraisingEvent createEvent(FundraisingEvent event) {
        return fundraisingEventRepository.save(event);
    }

    public FundraisingEvent getEventById(Long id) {
        return fundraisingEventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<FinancialReportDto> getFinancialReport() {
        return fundraisingEventRepository.findAll().stream()
                .map(event -> new FinancialReportDto(
                        event.getName(),
                        event.getAccountAmount(),
                        event.getCurrency().name()
                ))
                .toList();
    }

}
