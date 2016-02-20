package ua.com.mcgray;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ua.com.mcgray.configuration.UserServiceApplicationConfiguration;

/**
 * @author orezchykov
 * @since 01.12.14
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableWebMvc
@EnableEurekaClient
@EnableFeignClients
@Import(UserServiceApplicationConfiguration.class)
public class UserServiceApplication implements ServletContextInitializer {

    private static Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    protected ServletContextListener listener() {
        return new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                logger.info("UserServiceApplication ServletContext initialized");
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                logger.info("UserServiceApplication ServletContext destroyed");
            }
        };
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("UserServiceApplication initializer started");
    }
}
