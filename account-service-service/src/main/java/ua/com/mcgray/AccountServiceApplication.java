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
import ua.com.mcgray.configuration.AccountServiceApplicationConfiguration;
import ua.com.mcgray.monitoring.ToDoShareCounterService;
import ua.com.mcgray.monitoring.ToDoShareGaugeService;
import ua.com.mcgray.service.AccountService;

/**
 * @author orezchykov
 * @since 07.02.15
 */

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableWebMvc
@ComponentScan("ua.com.mcgray")
@Import(AccountServiceApplicationConfiguration.class)
public class AccountServiceApplication implements ServletContextInitializer{

    private static Logger logger = LoggerFactory.getLogger(AccountServiceApplication.class);

    @Autowired
    private MetricWriter metricWriter;

    @Autowired
    private AccountService accountService;

    @Bean
    public GaugeService gaugeService() {
        return new ToDoShareGaugeService(metricWriter);
    }

    @Bean
    public CounterService counterService() {
        return new ToDoShareCounterService(metricWriter);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("/remoting/*");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean(name = "/AccountService")
    public HessianServiceExporter getAccountServiceRemotingService() {
        HessianServiceExporter hessianServiceExporter = new HessianServiceExporter();
        hessianServiceExporter.setServiceInterface(AccountService.class);
        hessianServiceExporter.setService(accountService);
        hessianServiceExporter.afterPropertiesSet();
        return hessianServiceExporter;
    }

    @Bean
    protected ServletContextListener listener() {
        return new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                logger.info("ToDoServiceApplication ServletContext initialized");
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                logger.info("ToDoServiceApplication ServletContext destroyed");
            }
        };
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("ToDoServiceApplication initializer started");
    }



}

