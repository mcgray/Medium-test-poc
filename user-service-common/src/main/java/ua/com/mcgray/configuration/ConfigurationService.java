package ua.com.mcgray.configuration;

import java.util.List;

/**
 * @author orezchykov
 * @since 01.12.14
 */

public interface ConfigurationService {

    DbConfiguration getDbConfiguration();

    List<String> getCouchbaseHosts();

    String getCouchbaseBucketName();

    String getCouchbaseBucketPassword();

//    String getUserServiceUrl();
//
//    String getAccountServiceUrl();
}
