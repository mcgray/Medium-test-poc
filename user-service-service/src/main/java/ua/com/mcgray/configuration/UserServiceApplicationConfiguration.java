package ua.com.mcgray.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author orezchykov
 * @since 01.12.14
 */

@Configuration
@Import({SimpleFileConfigurationService.class, DataSourceConfiguration.class, JpaConfiguration.class})
public class UserServiceApplicationConfiguration {





}
