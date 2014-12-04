package ua.com.mcgray;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.service.UserService;
import ua.com.mcgray.test.EmbeddedMysqlProvider;
import ua.com.mcgray.test.MediumTest;
import ua.com.mcgray.utils.RestCaller;

import static org.fest.assertions.Assertions.assertThat;

@Category(MediumTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
     UserServiceApplication.class

})
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class UserServiceApplicationTest {

    static {
        final EmbeddedMysqlProvider embeddedMysqlProvider = new EmbeddedMysqlProvider();
        embeddedMysqlProvider.start("3336", "true", "mysql", "mysql", "root");
    }

    public static final long USER_ID = 1L;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Value("${server.contextPath}")
    private String contextPath;

    @Value("${local.server.port}")
    private int port;

    private RestCaller restCaller;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        restCaller = new RestCaller(port, contextPath);
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        userService = (UserService) hessianProxyFactory.create(UserService.class,
                "http://localhost:" + port + contextPath + "/remoting/UserService");

    }

    @Test
    public void shouldGetAllUsersHessian() throws Exception {
        List<UserDto> userDtos = userService.getAll();
        assertThat(userDtos).isNotNull().hasSize(2);
        assertThat(userDtos.get(0).getId()).isEqualTo(1);
        assertThat(userDtos.get(1).getId()).isEqualTo(2);

    }

    @Test
    public void shouldGetUserHessian() throws Exception {
        UserDto userDto = userService.get(USER_ID);
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(USER_ID);

    }

    @Test
    public void shouldThrowUserServiceExceptionWhenUserNotFoundHessian() throws Exception {
        thrown.expect(UserServiceException.class);
        userService.get(123L);

    }

    @Test
    public void shouldGetAllUsersJson() throws Exception {
        ResponseEntity<String> responseEntity = restCaller.queryApi(HttpMethod.GET, "/remoting/api/users");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        ReadContext context = JsonPath.parse(responseEntity.getBody());
        assertThat((Integer) context.read("$.[0].id")).isEqualTo(1);
        assertThat((Integer) context.read("$.[1].id")).isEqualTo(2);
    }

    @Test
    public void shouldGetUserJson() throws Exception {
        ResponseEntity<String> responseEntity = restCaller.queryApi(HttpMethod.GET, "/remoting/api/user/1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        ReadContext context = JsonPath.parse(responseEntity.getBody());
        assertThat((Integer) context.read("$.id")).isEqualTo(1);
    }

    @Test
    public void shouldGetBadRequestOnNonExistingUser() throws Exception {
        ResponseEntity<String> responseEntity = restCaller.queryApi(HttpMethod.GET, "/remoting/api/user/123");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}