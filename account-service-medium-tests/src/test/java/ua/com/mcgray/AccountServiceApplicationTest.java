package ua.com.mcgray;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.domain.User;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.service.UserService;
import ua.com.mcgray.test.EmbeddedMysqlProvider;
import ua.com.mcgray.test.MediumTest;
import ua.com.mcgray.utils.RestCaller;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author orezchykov
 * @since 10.02.15
 */

@Category(MediumTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
        AccountServiceApplication.class,
        AccountServiceApplicationTest.Config.class

})
@WebIntegrationTest("server.port=0")
public class AccountServiceApplicationTest {

    static {
        final EmbeddedMysqlProvider embeddedMysqlProvider = new EmbeddedMysqlProvider();
        embeddedMysqlProvider.start("3336", "true", "mysql", "mysql", "root");
    }

    public static final long USER_ID = 1L;
    public static final long FAKE_USER_ID = 234L;
    public static final long TODOSHARE_ACCOUNT_ID = 2L;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Value("${server.contextPath}")
    private String contextPath;

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private UserService userService;

    private RestCaller restCaller;

    @Before
    public void setUp() throws Exception {
        restCaller = new RestCaller(port, contextPath);

    }

    @Test
    public void shouldGetAccountByUserId() throws Exception {
        final User user = new User();
        user.setId(USER_ID);
        ToDoShareAccount toDoShareAccount1 = new ToDoShareAccount();
        toDoShareAccount1.setId(TODOSHARE_ACCOUNT_ID);
        user.setToDoShareAccount(toDoShareAccount1);
        final UserDto userDto = new UserDto(user);
        when(userService.get(USER_ID)).thenReturn(userDto);
        ResponseEntity<String> responseEntity = restCaller.queryApi(HttpMethod.GET, "/account/" + String.valueOf(USER_ID));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        ReadContext context = JsonPath.parse(responseEntity.getBody());
        assertThat((Integer) context.read("$.id")).isEqualTo(Long.valueOf(TODOSHARE_ACCOUNT_ID).intValue());
    }


    @Test
    public void shouldGetBadRequestOnNonExistingUser() throws Exception {
        when(userService.get(FAKE_USER_ID)).thenThrow(new UserServiceException());
        ResponseEntity<String> responseEntity = restCaller.queryApi(HttpMethod.GET, "/account/" + String.valueOf(FAKE_USER_ID));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Configuration
    public static class Config {

        @Bean
        @Primary
        public UserService userService() {
            return mock(UserService.class);
        }

    }
}
