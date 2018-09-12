package ru.medisov.home_finance.web.config.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import ru.medisov.home_finance.service.social.ConnectionSignUpImpl;
import ru.medisov.home_finance.service.UserService;
import ru.medisov.home_finance.service.config.ServiceConfiguration;
import ru.medisov.home_finance.web.config.social.repository.CustomJdbcUsersConnectionRepository;

import javax.sql.DataSource;

@Configuration
@EnableSocial
@PropertySource("classpath:social-cfg.properties")
@Import(ServiceConfiguration.class)
@ComponentScan("ru.medisov.home_finance.web")
public class SocialConfig implements SocialConfigurer {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        GoogleConnectionFactory gfactory = new GoogleConnectionFactory(
                env.getProperty("google.client.id"),
                env.getProperty("google.client.secret")
        );

        gfactory.setScope(env.getProperty("google.scope"));
        cfConfig.addConnectionFactory(gfactory);
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

        CustomJdbcUsersConnectionRepository usersConnectionRepository = new CustomJdbcUsersConnectionRepository(
                dataSource, connectionFactoryLocator, Encryptors.noOpText()
        );

        ConnectionSignUp connectionSignUp = new ConnectionSignUpImpl(userService);
        usersConnectionRepository.setConnectionSignUp(connectionSignUp);

        return usersConnectionRepository;
    }

    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }
}