package ua.com.mcgray.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.mcgray.domain.ToDo;
import ua.com.mcgray.domain.ToDoDto;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.domain.User;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.ToDoServiceException;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.repository.ToDoRepository;
import ua.com.mcgray.repository.ToDoShareAccountRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.when;

public class ToDoServiceImplTest {

    public static final long USER_ID = 1L;
    public static final long TODOSHARE_ACCOUNT_ID = 2L;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ToDoRepository toDoRepository;

    @Mock
    private ToDoShareAccountRepository toDoShareAccountRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ToDoServiceImpl toDoService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void shouldGetByAccountId() throws Exception {
        User user = new User();
        user.setId(USER_ID);
        ToDoShareAccount toDoShareAccount = new ToDoShareAccount();
        toDoShareAccount.setId(TODOSHARE_ACCOUNT_ID);
        user.setToDoShareAccount(toDoShareAccount);
        UserDto userDto = new UserDto(user);
        when(userService.get(USER_ID)).thenReturn(userDto);
        when(toDoShareAccountRepository.findOne(TODOSHARE_ACCOUNT_ID)).thenReturn(toDoShareAccount);
        ToDo toDo1 = new ToDo();
        toDo1.setId(1L);
        toDo1.setTitle("Title1");
        ToDo toDo2 = new ToDo();
        toDo2.setId(2L);
        toDo2.setTitle("Title2");
        when(toDoRepository.findByCreatedBy(toDoShareAccount)).thenReturn(Arrays.asList(toDo1, toDo2));

        List<ToDoDto> toDoDtos = toDoService.getByAccountId(USER_ID);

        assertThat(toDoDtos).isNotNull().hasSize(2);
        assertThat(toDoDtos.get(0).getTitle()).isEqualTo("Title1");
        assertThat(toDoDtos.get(1).getTitle()).isEqualTo("Title2");

    }

    @Test
    public void shouldThrowExceptionWhenThereIsNoUser() throws Exception {
        when(userService.get(USER_ID)).thenThrow(UserServiceException.class);

        thrown.expect(ToDoServiceException.class);
        thrown.expectCause(instanceOf(UserServiceException.class));

        toDoService.getByAccountId(USER_ID);

    }

    @Test
    public void shouldThrowExceptionWhenThereIsNoAccount() throws Exception {
        User user = new User();
        user.setId(USER_ID);
        ToDoShareAccount toDoShareAccount = new ToDoShareAccount();
        toDoShareAccount.setId(TODOSHARE_ACCOUNT_ID);
        user.setToDoShareAccount(toDoShareAccount);
        UserDto userDto = new UserDto(user);
        when(userService.get(USER_ID)).thenReturn(userDto);
        when(toDoShareAccountRepository.findOne(TODOSHARE_ACCOUNT_ID)).thenReturn(null);

        thrown.expect(ToDoServiceException.class);
        thrown.expectMessage("There is no account with id");
        toDoService.getByAccountId(USER_ID);
    }
}