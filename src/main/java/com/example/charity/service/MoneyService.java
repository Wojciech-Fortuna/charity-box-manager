package com.example.charity.service;

import com.example.charity.model.CollectionBox;
import com.example.charity.model.Currency;
import com.example.charity.model.Money;
import com.example.charity.repository.MoneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoneyService {

    private final MoneyRepository moneyRepository;

    public void addMoneyToBox(CollectionBox box, Currency currency, BigDecimal amount) {
        if (!box.isAssigned()) {
            throw new IllegalStateException("Cannot add money to an unassigned collection box.");
        }
        Money money = new Money(null, currency.name(), amount, box);
        moneyRepository.save(money);
    }

    public List<Money> findByBox(CollectionBox box) {
        return moneyRepository.findAll().stream()
                .filter(m -> m.getCollectionBox().getBoxId().equals(box.getBoxId()))
                .toList();
    }

    public void deleteByBox(CollectionBox box) {
        moneyRepository.deleteAll(findByBox(box));
    }
}
