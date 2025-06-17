package com.example.charity.service;

import com.example.charity.dto.FinancialReportDto;
import com.example.charity.model.Currency;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.repository.FundraisingEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundraisingEventServiceTest {

    private FundraisingEventRepository repository;
    private FundraisingEventService service;

    @BeforeEach
    void setUp() {
        repository = mock(FundraisingEventRepository.class);
        service = new FundraisingEventService(repository);
    }

    @Test
    void createEvent_savesEvent() {
        FundraisingEvent event = new FundraisingEvent();
        when(repository.save(event)).thenReturn(event);

        FundraisingEvent result = service.createEvent(event);

        verify(repository).save(event);
        assertThat(result).isEqualTo(event);
    }

    @Test
    void getEventById_whenFound_returnsEvent() {
        FundraisingEvent event = new FundraisingEvent();
        event.setEventId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        FundraisingEvent result = service.getEventById(1L);

        assertThat(result).isEqualTo(event);
    }

    @Test
    void getEventById_whenNotFound_throwsException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getEventById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Event not found");
    }

    @Test
    void getFinancialReport_returnsMappedDtos() {
        FundraisingEvent event = new FundraisingEvent();
        event.setName("Help");
        event.setCurrency(Currency.PLN);
        event.setAccountAmount(BigDecimal.valueOf(123.45));

        when(repository.findAll()).thenReturn(List.of(event));

        List<FinancialReportDto> report = service.getFinancialReport();

        assertThat(report).hasSize(1);
        assertThat(report.get(0).name()).isEqualTo("Help");
        assertThat(report.get(0).amount()).isEqualByComparingTo("123.45");
        assertThat(report.get(0).currency()).isEqualTo("PLN");
    }
}
