package ua.com.mcgray;

import java.util.Collection;
import java.util.List;

import com.caucho.hessian.client.HessianProxyFactory;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.com.mcgray.domain.ToDoDto;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.domain.User;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.ToDoServiceException;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.service.ToDoService;
import ua.com.mcgray.service.UserService;
import ua.com.mcgray.test.EmbeddedMysqlProvider;
import ua.com.mcgray.test.MediumTest;
import ua.com.mcgray.utils.EmbeddedCouchbaseProvider;
import ua.com.mcgray.utils.RestCaller;

import static org.fest.assertions.Assertions.assertThat;
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
@WebAppConfiguration
@IntegrationTest("server.port=0")
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

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Value("${server.contextPath}")
        private String contextPath;

        @Value("${local.server.port}")
        private int port;

        @Autowired
        private CacheManager cacheManager;

        @Autowired
        private UserService userService;

        private RestCaller restCaller;

        private ToDoService toDoService;

        @Before
        public void setUp() throws Exception {
                restCaller = new RestCaller(port, contextPath);

                HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
                toDoService = (ToDoService) hessianProxyFactory.create(ToDoService.class,
                        "http://localhost:" + port + contextPath + "/remoting/ToDoService");

                Collection<String> cacheNames = cacheManager.getCacheNames();
                for (String cacheName : cacheNames) {
                        cacheManager.getCache(cacheName).clear();
                }

        }

        @Test
        public void shouldGetToDoByUserIdHessian() throws Exception {
                prepareUserService();
                List<ToDoDto> toDoDtos = toDoService.getByAccountId(USER_ID);

                assertThat(toDoDtos).isNotNull().hasSize(2);
                assertThat(toDoDtos.get(0).getTitle()).isEqualTo("Admin personal ToDo");
                assertThat(toDoDtos.get(1).getTitle()).isEqualTo("Admin shared ToDo");

        }

        private void prepareUserService() {
                User user = new User();
                user.setId(USER_ID);
                ToDoShareAccount toDoShareAccount = new ToDoShareAccount();
                toDoShareAccount.setId(1L);
                user.setToDoShareAccount(toDoShareAccount);
                final UserDto userDto = new UserDto(user);
                when(userService.get(USER_ID)).thenReturn(userDto);
        }

        @Test
        public void shouldThrowToDoServiceExceptionHessian() throws Exception {
                when(userService.get(USER_ID)).thenThrow(UserServiceException.class);

                thrown.expect(ToDoServiceException.class);
                toDoService.getByAccountId(USER_ID);

        }

        @Test
        public void shouldGetToDoByUserIdJson() throws Exception {
                prepareUserService();
                ResponseEntity<String> responseEntity = restCaller.queryApi(HttpMethod.GET, "/remoting/api/todo/1");
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(responseEntity.getBody()).isNotNull();
                ReadContext context = JsonPath.parse(responseEntity.getBody());
                assertThat((String) context.read("$.[0].title")).isEqualTo("Admin personal ToDo");
                assertThat((String) context.read("$.[1].title")).isEqualTo("Admin shared ToDo");

        }

        @Test
        public void shouldThrowToDoServiceExceotionJson() throws Exception {
                when(userService.get(123L)).thenThrow(UserServiceException.class);

                ResponseEntity<String> responseEntity = restCaller.queryApi(HttpMethod.GET, "/remoting/api/todo/123");
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        }

        @Configuration
        public static  class Config {

                @Bean
                @Primary
                public UserService userService() {
                        return Mockito.mock(UserService.class);
                }

        }
}
