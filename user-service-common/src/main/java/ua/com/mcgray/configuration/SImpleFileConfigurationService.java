package ua.com.mcgray.configuration;

import java.util.Arrays;
import java.util.List;

import com.netflix.config.DynamicPropertyFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author orezchykov
 * @since 01.12.14
 */

@Configuration
public class SimpleFileConfigurationService implements ConfigurationService  {

//    @Autowired
//    private DiscoveryClient discoveryClient;

    @Override
    public DbConfiguration getDbConfiguration() {

        DbConfiguration dbConfiguration = new DbConfiguration();

        dbConfiguration.setDriverClassName(DynamicPropertyFactory.getInstance().getStringProperty("db.general.driverclassname", "").get());
        dbConfiguration.setMaxActive(DynamicPropertyFactory.getInstance().getIntProperty("db.general.maxactive", 0).get());
        dbConfiguration.setMinIdle(DynamicPropertyFactory.getInstance().getIntProperty("db.general.minidle", 0).get());
        dbConfiguration.setMaxIdle(DynamicPropertyFactory.getInstance().getIntProperty("db.general.maxidle", 0).get());
        dbConfiguration.setMaxWait(DynamicPropertyFactory.getInstance().getIntProperty("db.general.maxwait", 0).get());
        dbConfiguration.setValidationQuery(DynamicPropertyFactory.getInstance().getStringProperty("db.general.validationquery", "").get());
        dbConfiguration.setTestOnBorrow(DynamicPropertyFactory.getInstance().getBooleanProperty("db.general.testonborrow", false).get());
        dbConfiguration.setTestWhileIdle(DynamicPropertyFactory.getInstance().getBooleanProperty("db.general.testwhileidle", false).get());
        dbConfiguration.setUrl(DynamicPropertyFactory.getInstance().getStringProperty("db.user.url", "").get());
        dbConfiguration.setUser(DynamicPropertyFactory.getInstance().getStringProperty("db.user.user", "").get());
        dbConfiguration.setPassword(DynamicPropertyFactory.getInstance().getStringProperty("db.user.password", "").get());
        return dbConfiguration;
        
    }

    @Override
    public List<String> getCouchbaseHosts() {
        return Arrays.asList(DynamicPropertyFactory.getInstance().getStringProperty("couchbase.todo.hosts", "").get().split(","));
    }

    @Override
    public String getCouchbaseBucketName() {
        return DynamicPropertyFactory.getInstance().getStringProperty("couchbase.todo.bucket", "").get();
    }

    @Override
    public String getCouchbaseBucketPassword() {
        return DynamicPropertyFactory.getInstance().getStringProperty("couchbase.todo.password", "").get();
    }

//    @Override
//    public String getUserServiceUrl() {
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
//        if (instances != null && !instances.isEmpty()) {
//            return instances.get(0).getUri().toString();
//        }
//        return null;
//    }
//
//    @Override
//    public String getAccountServiceUrl() {
//        List<ServiceInstance> instances = discoveryClient.getInstances("account-service");
//        if (instances != null && !instances.isEmpty()) {
//            return instances.get(0).getUri().toString();
//        }
//        return null;
//    }


}
