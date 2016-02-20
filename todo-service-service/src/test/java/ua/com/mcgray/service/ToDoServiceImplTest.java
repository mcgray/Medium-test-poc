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
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.domain.User;
import ua.com.mcgray.exception.AccountServiceException;
import ua.com.mcgray.exception.ToDoServiceException;
import ua.com.mcgray.repository.ToDoRepository;

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
    private AccountService accountService;

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
        final ToDoShareAccountDto toDoShareAccountDto = new ToDoShareAccountDto(toDoShareAccount);
        when(accountService.getAccountByUserId(USER_ID)).thenReturn(toDoShareAccountDto);
        ToDo toDo1 = new ToDo();
        toDo1.setId(1L);
        toDo1.setTitle("Title1");
        ToDo toDo2 = new ToDo();
        toDo2.setId(2L);
        toDo2.setTitle("Title2");
        when(toDoRepository.findByCreatedById(toDoShareAccountDto.getId())).thenReturn(Arrays.asList(toDo1, toDo2));

        List<ToDoDto> toDoDtos = toDoService.getByUserId(USER_ID);

        assertThat(toDoDtos).isNotNull().hasSize(2);
        assertThat(toDoDtos.get(0).getTitle()).isEqualTo("Title1");
        assertThat(toDoDtos.get(1).getTitle()).isEqualTo("Title2");

    }

    @Test
    public void shouldThrowExceptionWhenThereIsNoAccount() throws Exception {
        when(accountService.getAccountByUserId(USER_ID)).thenThrow(AccountServiceException.class);

        thrown.expect(ToDoServiceException.class);
        thrown.expectCause(instanceOf(AccountServiceException.class));

        toDoService.getByUserId(USER_ID);

    }
}