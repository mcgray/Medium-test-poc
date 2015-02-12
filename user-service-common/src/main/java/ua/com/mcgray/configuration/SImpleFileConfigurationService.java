package ua.com.mcgray.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * @author orezchykov
 * @since 01.12.14
 */

@Configuration
//@PropertySource("file:/etc/mcgray/configuration.properties")
//@PropertySource("classpath:configuration.properties")
public class SimpleFileConfigurationService implements ConfigurationService {

    @Value("${config.location}")
    private String configLocation;

    @Autowired
    private ResourceLoader resourceLoader;

    private Properties properties;

    @PostConstruct
    protected void loadProperties() {
        try {
            properties = new Properties();
            properties.load(resourceLoader.getResource(configLocation).getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Can't find services.properties", e);
        }
    }


    @Override
    public DbConfiguration getDbConfiguration() {

        DbConfiguration dbConfiguration = new DbConfiguration();

        dbConfiguration.setDriverClassName(properties.getProperty("db.general.driverclassname"));
        dbConfiguration.setMaxActive(Integer.parseInt(properties.getProperty("db.general.maxactive")));
        dbConfiguration.setMinIdle(Integer.parseInt(properties.getProperty("db.general.minidle")));
        dbConfiguration.setMaxIdle(Integer.parseInt(properties.getProperty("db.general.maxidle")));
        dbConfiguration.setMaxWait(Integer.parseInt(properties.getProperty("db.general.maxwait")));
        dbConfiguration.setValidationQuery(properties.getProperty("db.general.validationquery"));
        dbConfiguration.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("db.general.testonborrow")));
        dbConfiguration.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty("db.general.testwhileidle")));
        dbConfiguration.setUrl(properties.getProperty("db.user.url"));
        dbConfiguration.setUser(properties.getProperty("db.user.user"));
        dbConfiguration.setPassword(properties.getProperty("db.user.password"));
        
        return dbConfiguration;
        
    }

    @Override
    public List<String> getCouchbaseHosts() {
        return Arrays.asList(properties.getProperty("couchbase.todo.hosts").split(","));
    }

    @Override
    public String getCouchbaseBucketName() {
        return properties.getProperty("couchbase.todo.bucket");
    }

    @Override
    public String getCouchbaseBucketPassword() {
        return properties.getProperty("couchbase.todo.password");
    }

    @Override
    public String getUserServiceUrl() {
        return properties.getProperty("service.user.url");
    }

    @Override
    public String getAccountServiceUrl() {
        return properties.getProperty("service.account.url");
    }

}
