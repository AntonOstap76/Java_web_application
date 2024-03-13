package com.shopme.admin.test.setting;

import com.shopme.admin.repository.setting.CurrencyRepository;
import com.shopme.common.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void testCreateCurrencies(){
        List<Currency> listCurrencies = Arrays.asList(
          new Currency("United States Dollar", "$", "USD"),
          new Currency("British Pound", "£", "GPB"),
          new Currency("Japanese Yen", "¥", "JPY"),
          new Currency("Euro", "€", "EUR"),
          new Currency("Ukrainian hryvnia", "₴", "UAH"),
          new Currency("Australian Dollar", "$", "AUD"),
          new Currency("Canadian Dollar", "$", "CAD"),
          new Currency("Chinese Yuan", "¥", "CNY"),
          new Currency("Indian Rupee", "₹", "INR"),
                new Currency("Polish Zloty", "zł", "PLN")
        );
        currencyRepository.saveAll(listCurrencies);
        Iterable<Currency> iterable = currencyRepository.findAll();

        assertThat(iterable).size().isEqualTo(9);
    }

    @Test
    public void testListAllOrderByNameAsc(){
        List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();

        currencies.forEach(System.out::println);

        assertThat(currencies).size().isGreaterThan(0);
    }
}
