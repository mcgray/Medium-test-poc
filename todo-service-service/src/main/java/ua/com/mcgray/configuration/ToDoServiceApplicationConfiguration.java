package ua.com.mcgray.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author orezchykov
 * @since 04.12.14
 */

@Configuration
@Import({SimpleFileConfigurationService.class, DataSourceConfiguration.class, JpaConfiguration.class, CouchbaseConfiguration.class})
public class ToDoServiceApplicationConfiguration {

    @Autowired
    private ConfigurationService configurationService;

}
