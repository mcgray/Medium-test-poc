package ua.com.mcgray;

import java.util.List;

import com.caucho.hessian.client.HessianProxyFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.com.mcgray.dto.UserDto;
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
    public void shouldGetAllUsers() throws Exception {
        List<UserDto> userDtos = userService.getAll();
        assertThat(userDtos).isNotNull().hasSize(2);
        assertThat(userDtos.get(0).getId()).isEqualTo(1);
        assertThat(userDtos.get(1).getId()).isEqualTo(2);

    }
}