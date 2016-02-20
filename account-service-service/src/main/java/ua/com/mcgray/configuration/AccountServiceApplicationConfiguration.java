package ua.com.mcgray.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author orezchykov
 * @since 07.02.15
 */

@Configuration
@Import({SimpleFileConfigurationService.class, DataSourceConfiguration.class, JpaConfiguration.class})
public class AccountServiceApplicationConfiguration {

    @Autowired
    private ConfigurationService configurationService;

}
