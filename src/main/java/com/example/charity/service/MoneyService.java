package com.example.charity.service;

import com.example.charity.model.CollectionBox;
import com.example.charity.model.Currency;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.model.Money;
import com.example.charity.repository.MoneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoneyService {

    private final MoneyRepository moneyRepository;

    public void addMoneyToBox(CollectionBox box, Currency currency, BigDecimal amount) {
        if (!box.isAssigned()) {
            throw new IllegalStateException("Cannot add money to an unassigned collection box.");
        }
        Money money = new Money(null, currency, amount, box);
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

    public void emptyBox(CollectionBox box, ExchangeRateService exchangeService) {
        FundraisingEvent event = box.getEvent();
        if (event == null) {
            throw new IllegalStateException("Box must be assigned to an event to be emptied.");
        }

        List<Money> allMoney = findByBox(box);
        BigDecimal total = BigDecimal.ZERO;

        Map<Currency, List<Money>> groupedByCurrency = allMoney.stream()
                .collect(Collectors.groupingBy(Money::getCurrency));

        for (Map.Entry<Currency, List<Money>> entry : groupedByCurrency.entrySet()) {
            Currency fromCurrency = entry.getKey();
            List<Money> monies = entry.getValue();

            BigDecimal rate = exchangeService.getRate(fromCurrency, event.getCurrency());

            for (Money money : monies) {
                BigDecimal converted = money.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP);
                total = total.add(converted);
            }
        }

        event.setAccountAmount(event.getAccountAmount().add(total));

        deleteByBox(box);
    }
}
