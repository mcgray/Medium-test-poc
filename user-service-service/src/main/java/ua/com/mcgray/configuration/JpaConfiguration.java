package ua.com.mcgray.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author orezchykov
 * @since 01.12.14
 */
@Configuration
public class JpaConfiguration {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adaptor = new HibernateJpaVendorAdapter();
        adaptor.setShowSql(false);
        adaptor.setGenerateDdl(false);
        adaptor.setDatabase(Database.MYSQL);
        return adaptor;
    }

}
