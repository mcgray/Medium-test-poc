package ua.com.mcgray.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.domain.User;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.AccountServiceException;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.repository.ToDoShareAccountRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.when;

public class AccountServiceImplTest {

    public static final long USER_ID = 1L;
    public static final long TODOSHARE_ACCOUNT_ID = 2L;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private UserService userService;

    @Mock
    private ToDoShareAccountRepository toDoShareAccountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;
    private ToDoShareAccount toDoShareAccount;
    private User user;
    private UserDto userDto;
    private ToDoShareAccountDto toDoShareAccountDto;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        toDoShareAccount = new ToDoShareAccount();
        toDoShareAccount.setId(TODOSHARE_ACCOUNT_ID);
        user = new User();
        user.setId(USER_ID);
        user.setToDoShareAccount(toDoShareAccount);
        userDto = new UserDto(user);
        toDoShareAccountDto = new ToDoShareAccountDto(toDoShareAccount);
    }

    @Test
    public void shouldGetByUserId() throws Exception {

        when(userService.get(USER_ID)).thenReturn(userDto);
        when(toDoShareAccountRepository.findOne(TODOSHARE_ACCOUNT_ID)).thenReturn(toDoShareAccount);
        ToDoShareAccountDto account = accountService.getByUserId(USER_ID);

        assertThat(account).isEqualTo(toDoShareAccountDto);


    }

    @Test
    public void shouldThrowAseWhenUserIdIsNull() throws Exception {
        thrown.expect(AccountServiceException.class);
        thrown.expectMessage("cannot be null");
        accountService.getByUserId(null);

    }

    @Test
    public void shouldThrowAseInCaseUserServiceThrowsUse() throws Exception {
        thrown.expect(AccountServiceException.class);
        thrown.expectMessage("There is no user with id");
        thrown.expectCause(instanceOf(UserServiceException.class));
        when(userService.get(USER_ID)).thenThrow(UserServiceException.class);
        accountService.getByUserId(USER_ID);

    }

    @Test
    public void shouldThrowAseInCaseAccountIsNull() throws Exception {
        when(userService.get(USER_ID)).thenReturn(userDto);
        when(toDoShareAccountRepository.findOne(TODOSHARE_ACCOUNT_ID)).thenReturn(null);
        thrown.expect(AccountServiceException.class);
        thrown.expectMessage("There is no account with id");
        accountService.getByUserId(USER_ID);
    }
}