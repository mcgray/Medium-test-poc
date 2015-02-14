package ua.com.mcgray.configuration;

import java.io.File;
import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author orezchykov
 * @since 01.12.14
 */

@Configuration
public class DataSourceConfiguration {

    @Autowired
    private ConfigurationService configurationService;

    @Primary
    @Bean
    public DataSource dataSource() {
        DbConfiguration dbConfiguration = configurationService.getDbConfiguration();

        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setDriverClassName(dbConfiguration.getDriverClassName());
        poolProperties.setUrl(dbConfiguration.getUrl());
        poolProperties.setUsername(dbConfiguration.getUser());
        poolProperties.setPassword(dbConfiguration.getPassword());
        poolProperties.setMaxActive(dbConfiguration.getMaxActive());
        poolProperties.setMaxIdle(dbConfiguration.getMaxIdle());
        poolProperties.setMinIdle(dbConfiguration.getMinIdle());
        poolProperties.setMaxWait(dbConfiguration.getMaxWait());
        poolProperties.setValidationQuery(dbConfiguration.getValidationQuery());
        poolProperties.setTestOnBorrow(dbConfiguration.getTestOnBorrow());
        poolProperties.setTestWhileIdle(dbConfiguration.getTestWhileIdle());

        return new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);

    }

    @Bean
    @Primary
    public LiquibaseProperties liquibaseProperties() {
        final LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
        String projectDir = new File("").getAbsolutePath().replaceAll("Medium-test-poc.*", "Medium-test-poc");
        final char pathSeparator = File.separatorChar;

        final String location = String.format("file:%s" + pathSeparator +
                "db" + pathSeparator + "scripts" + pathSeparator + "migration" + pathSeparator + "database_changelog.xml", projectDir);

        liquibaseProperties.setChangeLog(location);
        return liquibaseProperties;
    }


}
