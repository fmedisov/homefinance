package ru.medisov.home_finance.dao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.medisov.home_finance.dao.repository.*;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:dao_config.properties")
public class DaoConfiguration {

    @Value("${db.driver}")
    private String driver;
    @Value("${db.url}")
    private String url;
    @Value("${db.login}")
    private String login;
    @Value("${db.password}")
    private String password;
    @Value("${db.conn.params}")
    private String dbParameters;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url + dbParameters);
        dataSource.setUsername(login);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean("currencyRepository")
    public CurrencyRepository currencyRepository() {
        return new CurrencyRepositoryImpl();
    }

    @Bean("categoryRepository")
    public CategoryRepository categoryRepository() {
        return new CategoryRepositoryImpl();
    }

    @Bean("accountRepository")
    public AccountRepository accountRepository() {
        return new AccountRepositoryImpl();
    }

    @Bean("transactionRepository")
    public TransactionRepository transactionRepository() {
        return new TransactionRepositoryImpl();
    }

    @Bean("tagRepository")
    public TagRepository tagRepository() {
        return new TagRepositoryImpl();
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
