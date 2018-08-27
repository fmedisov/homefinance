package ru.medisov.home_finance.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import ru.medisov.home_finance.service.AccountService;
import ru.medisov.home_finance.service.CategoryService;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.service.config.ServiceConfiguration;
import ru.medisov.home_finance.web.converter.AccountConverter;
import ru.medisov.home_finance.web.converter.CategoryConverter;
import ru.medisov.home_finance.web.converter.CurrencyConverter;
import ru.medisov.home_finance.web.converter.TransactionConverter;

@Configuration
@EnableWebMvc
@Import(ServiceConfiguration.class)
//todo remove beans
@ComponentScan("ru.medisov.home_finance.web")
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountService accountService;

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/resources/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(templateResolver());
        springTemplateEngine.setEnableSpringELCompiler(true);

        return springTemplateEngine;
    }

    @Bean("currencyConverter")
    public CurrencyConverter currencyConverter() {
        return new CurrencyConverter();
    }

    @Bean("accountConverter")
    public AccountConverter accountConverter() {
        return new AccountConverter(currencyService);
    }

    @Bean("categoryConverter")
    public CategoryConverter categoryConverter() {
        return new CategoryConverter(categoryService);
    }

    @Bean("transactionConverter")
    public TransactionConverter transactionConverter() {
        return new TransactionConverter(categoryService, accountService);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine());
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        thymeleafViewResolver.setContentType("text/html; charset=UTF-8");

        registry.viewResolver(thymeleafViewResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }
}


