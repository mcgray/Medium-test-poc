package ua.com.mcgray.configuration;

import java.util.HashMap;

import com.couchbase.client.CouchbaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.cache.CouchbaseCacheManager;

/**
 * @author orezchykov
 * @since 05.12.14
 */

@Configuration
@EnableCaching
public class CacheConfiguration implements CachingConfigurer {

    @Autowired
    private CouchbaseClient couchbaseClient;

    @Bean
    @Override
    public CacheManager cacheManager() {
        HashMap<String, CouchbaseClient> clients = new HashMap<>();
        clients.put("todo", couchbaseClient);
        CouchbaseCacheManager couchbaseCacheManager = new CouchbaseCacheManager(clients);
        return couchbaseCacheManager;
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }
}
