package ua.com.mcgray.configuration;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ua.com.mcgray.service.AccountService;

/**
 * @author orezchykov
 * @since 04.12.14
 */

@Configuration
@Import({SimpleFileConfigurationService.class, DataSourceConfiguration.class, JpaConfiguration.class, CouchbaseConfiguration.class})
public class ToDoServiceApplicationConfiguration {

    @Autowired
    private ConfigurationService configurationService;

    @Bean
    public AccountService accountService() throws MalformedURLException {
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        return (AccountService) hessianProxyFactory.create(AccountService.class, configurationService.getAccountServiceUrl());
    }



}
