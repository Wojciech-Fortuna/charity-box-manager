package com.example.charity.service;

import com.example.charity.model.CollectionBox;
import com.example.charity.model.Currency;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.model.Money;
import com.example.charity.repository.MoneyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyServiceTest {

    private MoneyRepository moneyRepository;
    private ExchangeRateService exchangeRateService;
    private MoneyService moneyService;

    @BeforeEach
    void setUp() {
        moneyRepository = mock(MoneyRepository.class);
        exchangeRateService = mock(ExchangeRateService.class);
        moneyService = new MoneyService(moneyRepository);
    }

    @Test
    void addMoneyToAssignedBox_savesMoney() {
        CollectionBox box = new CollectionBox();
        box.setBoxId(1L);
        box.setEvent(new FundraisingEvent()); // means "assigned"

        moneyService.addMoneyToBox(box, Currency.EUR, BigDecimal.valueOf(10.00));

        verify(moneyRepository).save(any(Money.class));
    }

    @Test
    void addMoneyToUnassignedBox_throwsException() {
        CollectionBox box = new CollectionBox(); // no event = unassigned

        assertThatThrownBy(() -> moneyService.addMoneyToBox(box, Currency.EUR, BigDecimal.TEN))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unassigned");
    }

    @Test
    void findByBox_returnsOnlyMoneyForBox() {
        CollectionBox box = new CollectionBox();
        box.setBoxId(1L);

        Money m1 = new Money(null, Currency.PLN, BigDecimal.TEN, box);
        m1.setCollectionBox(box);

        CollectionBox otherBox = new CollectionBox();
        otherBox.setBoxId(2L);

        Money m2 = new Money(null, Currency.PLN, BigDecimal.ONE, otherBox);

        when(moneyRepository.findAll()).thenReturn(List.of(m1, m2));

        List<Money> result = moneyService.findByBox(box);

        assertThat(result).containsExactly(m1);
    }

    @Test
    void emptyBox_transfersConvertedMoneyAndDeletesMoney() {
        CollectionBox box = new CollectionBox();
        box.setBoxId(1L);
        FundraisingEvent event = new FundraisingEvent();
        event.setAccountAmount(BigDecimal.ZERO);
        event.setCurrency(Currency.PLN);
        box.setEvent(event);

        Money eurMoney = new Money(null, Currency.EUR, new BigDecimal("10.00"), box);
        when(moneyRepository.findAll()).thenReturn(List.of(eurMoney));

        when(exchangeRateService.getRate(Currency.EUR, Currency.PLN)).thenReturn(new BigDecimal("4.00"));

        moneyService.emptyBox(box, exchangeRateService);

        assertThat(event.getAccountAmount()).isEqualByComparingTo("40.00");
        verify(moneyRepository).deleteAll(List.of(eurMoney));
    }

    @Test
    void emptyBox_whenBoxNotAssigned_throwsException() {
        CollectionBox box = new CollectionBox(); // no event assigned

        assertThatThrownBy(() -> moneyService.emptyBox(box, exchangeRateService))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("assigned to an event");
    }
}
