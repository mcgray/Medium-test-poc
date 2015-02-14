package ua.com.mcgray;

import com.caucho.hessian.client.HessianProxyFactory;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.domain.User;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.service.AccountService;
import ua.com.mcgray.service.UserService;
import ua.com.mcgray.test.EmbeddedMysqlProvider;
import ua.com.mcgray.test.MediumTest;

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
    public static final long TODOSHARE_ACCOUNT_ID = 2L;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Value("${server.contextPath}")
    private String contextPath;

    @Value("${local.server.port}")
    private int port;

    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        accountService = (AccountService) hessianProxyFactory.create(AccountService.class,
                "http://localhost:" + port + contextPath + "/remoting/AccountService");

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
        ToDoShareAccountDto toDoShareAccount = accountService.getByUserId(USER_ID);
        assertThat(toDoShareAccount).isNotNull();
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
