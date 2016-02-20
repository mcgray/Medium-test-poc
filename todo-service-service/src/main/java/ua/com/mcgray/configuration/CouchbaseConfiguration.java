package ua.com.mcgray.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

/**
 * @author orezchykov
 * @since 04.12.14
 */
@Configuration
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {

    @Autowired
    private ConfigurationService configurationService;

    @Override
    protected List<String> bootstrapHosts() {
        return configurationService.getCouchbaseHosts();
    }

    @Override
    protected String getBucketName() {
        return configurationService.getCouchbaseBucketName();
    }

    @Override
    protected String getBucketPassword() {
        return configurationService.getCouchbaseBucketPassword();
    }
}
