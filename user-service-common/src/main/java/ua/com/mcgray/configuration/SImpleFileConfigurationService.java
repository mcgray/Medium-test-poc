package ua.com.mcgray.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author orezchykov
 * @since 01.12.14
 */

@Configuration
//@PropertySource("file:/etc/mcgray/configuration.properties")
@PropertySource("classpath:configuration.properties")
public class SimpleFileConfigurationService implements ConfigurationService {

    @Autowired
    private Environment environment;


    @Override
    public DbConfiguration getDbConfiguration() {

        DbConfiguration dbConfiguration = new DbConfiguration();

        dbConfiguration.setDriverClassName(environment.getProperty("db.general.driverclassname"));
        dbConfiguration.setMaxActive(Integer.parseInt(environment.getProperty("db.general.maxactive")));
        dbConfiguration.setMinIdle(Integer.parseInt(environment.getProperty("db.general.minidle")));
        dbConfiguration.setMaxIdle(Integer.parseInt(environment.getProperty("db.general.maxidle")));
        dbConfiguration.setMaxWait(Integer.parseInt(environment.getProperty("db.general.maxwait")));
        dbConfiguration.setValidationQuery(environment.getProperty("db.general.validationquery"));
        dbConfiguration.setTestOnBorrow(Boolean.parseBoolean(environment.getProperty("db.general.testonborrow")));
        dbConfiguration.setTestWhileIdle(Boolean.parseBoolean(environment.getProperty("db.general.testwhileidle")));
        dbConfiguration.setUrl(environment.getProperty("db.user.url"));
        dbConfiguration.setUser(environment.getProperty("db.user.user"));
        dbConfiguration.setPassword(environment.getProperty("db.user.password"));
        
        return dbConfiguration;
        
    }

}
