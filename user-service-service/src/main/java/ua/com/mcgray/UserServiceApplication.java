package ua.com.mcgray;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ua.com.mcgray.configuration.UserServiceApplicationConfiguration;
import ua.com.mcgray.monitoring.ToDoShareCounterService;
import ua.com.mcgray.monitoring.ToDoShareGaugeService;
import ua.com.mcgray.service.UserService;

/**
 * @author orezchykov
 * @since 01.12.14
 */

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableWebMvc
@ComponentScan("ua.com.mcgray")
@Import(UserServiceApplicationConfiguration.class)
public class UserServiceApplication implements ServletContextInitializer {

    private static Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MetricWriter metricWriter;

    @Bean
    public GaugeService gaugeService() {
        return new ToDoShareGaugeService(metricWriter);
    }

    @Bean
    public CounterService counterService() {
        return new ToDoShareCounterService(metricWriter);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("/remoting/*");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean(name = "/UserService")
    public HessianServiceExporter getUserServiceRemotingService() {
        HessianServiceExporter hessianServiceExporter = new HessianServiceExporter();
        hessianServiceExporter.setServiceInterface(UserService.class);
        hessianServiceExporter.setService(userService);
        hessianServiceExporter.afterPropertiesSet();
        return hessianServiceExporter;
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
