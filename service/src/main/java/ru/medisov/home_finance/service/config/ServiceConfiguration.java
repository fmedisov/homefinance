package ru.medisov.home_finance.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.medisov.home_finance.dao.config.DaoConfiguration;

@Configuration
@Import({DaoConfiguration.class})
@ComponentScan("ru.medisov.home_finance.service")
public class ServiceConfiguration {
}
