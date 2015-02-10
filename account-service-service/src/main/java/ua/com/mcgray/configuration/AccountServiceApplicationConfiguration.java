package ua.com.mcgray.configuration;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ua.com.mcgray.service.UserService;

/**
 * @author orezchykov
 * @since 07.02.15
 */

@Configuration
@Import({SimpleFileConfigurationService.class, DataSourceConfiguration.class, JpaConfiguration.class})
public class AccountServiceApplicationConfiguration {

    @Autowired
    private ConfigurationService configurationService;

    @Bean
    public UserService userService() throws MalformedURLException {
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        return (UserService) hessianProxyFactory.create(UserService.class, configurationService.getUserServiceUrl());
    }



}
