package ru.medisov.home_finance.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.medisov.home_finance.dao.config.DaoConfiguration;
import ru.medisov.home_finance.service.*;
import ru.medisov.home_finance.service.config.ServiceConfiguration;

@Configuration
@Import({DaoConfiguration.class, ServiceConfiguration.class})
public class WebConfiguration {

    @Bean("currencyService")
    public CurrencyService currencyService() {
        return new CurrencyServiceImpl();
    }

    @Bean("categoryService")
    public CategoryService categoryService() {
        return new CategoryServiceImpl();
    }

    @Bean("accountService")
    public AccountService accountService() {
        return new AccountServiceImpl();
    }

    @Bean("transactionService")
    public TransactionService transactionService() {
        return new TransactionServiceImpl();
    }
}
