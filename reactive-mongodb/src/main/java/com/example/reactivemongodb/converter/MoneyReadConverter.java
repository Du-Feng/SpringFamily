package com.example.reactivemongodb.converter;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;

public class MoneyReadConverter implements Converter<Long, Money> {

    @Override
    public Money convert(Long aLong) {
        return Money.ofMinor(CurrencyUnit.of("CNY"), aLong);
    }
}
