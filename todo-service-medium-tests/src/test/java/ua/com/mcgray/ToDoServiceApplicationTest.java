package ua.com.mcgray;

import java.util.Collection;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.exception.AccountServiceException;
import ua.com.mcgray.service.AccountService;
import ua.com.mcgray.test.EmbeddedMysqlProvider;
import ua.com.mcgray.test.MediumTest;
import ua.com.mcgray.utils.EmbeddedCouchbaseProvider;
import ua.com.mcgray.utils.RestCaller;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author orezchykov
 * @since 05.12.14
 */

@Category(MediumTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
        ToDoServiceApplication.class,
        ToDoServiceApplicationTest.Config.class
})
@WebIntegrationTest("server.port=0")
public class ToDoServiceApplicationTest {

        static {
                final EmbeddedMysqlProvider embeddedMysqlProvider = new EmbeddedMysqlProvider();
                embeddedMysqlProvider.start("3336", "true", "mysql", "mysql", "root");
                try {
                        EmbeddedCouchbaseProvider.setup(8091, "TODO");
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        public static final long USER_ID = 1L;
        public static final long FAKE_TODO_ID = 234L;

        @Value("${server.contextPath}")
        private String contextPath;

        @Value("${local.server.port}")
        private int port;

        @Autowired
        private CacheManager cacheManager;

        @Autowired
        private AccountService accountService;

        private RestCaller restCaller;

        @Before
        public void setUp() throws Exception {
                restCaller = new RestCaller(port, contextPath);

                Collection<String> cacheNames = cacheManager.getCacheNames();
                for (String cacheName : cacheNames) {
                        cacheManager.getCache(cacheName).clear();
                }

        }

        private void prepareUserService() {
                ToDoShareAccount toDoShareAccount = new ToDoShareAccount();
                toDoShareAccount.setId(1L);
            final ToDoShareAccountDto toDoShareAccountDto = new ToDoShareAccountDto(toDoShareAccount);
            when(accountService.getAccountByUserId(USER_ID)).thenReturn(toDoShareAccountDto);
        }

        @Test
        public void shouldGetToDoByUserIdJson() throws Exception {
                prepareUserService();
                ResponseEntity<String> responseEntity = restCaller.queryApi(HttpMethod.GET, "/todo/1");
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(responseEntity.getBody()).isNotNull();
                ReadContext context = JsonPath.parse(responseEntity.getBody());
                assertThat((String) context.read("$.[0].title")).isEqualTo("Admin personal ToDo");
                assertThat((String) context.read("$.[1].title")).isEqualTo("Admin shared ToDo");

        }

        @Test
        public void shouldThrowToDoServiceExceotionJson() throws Exception {
                when(accountService.getAccountByUserId(FAKE_TODO_ID)).thenThrow(new AccountServiceException());

                ResponseEntity<String> responseEntity = restCaller.queryApi(HttpMethod.GET, "/todo/" + String.valueOf(FAKE_TODO_ID));
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        }

        @Configuration
        public static  class Config {

                @Bean
                @Primary
                public AccountService accountService() {
                        return mock(AccountService.class);
                }

        }
}
