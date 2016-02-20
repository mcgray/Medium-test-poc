package ua.com.mcgray.web;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.exception.AccountServiceException;
import ua.com.mcgray.service.AccountService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author orezchykov
 * @since 2/17/16
 */

public class AccountServiceControllerTest {

    private static final long FAKE_USER_ID = 234L;
    private static final long USER_ID = 1L;
    private static final long TODOSHARE_ACCOUNT_ID = 2L;

    @InjectMocks
    private AccountServiceController accountServiceController;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;
    private ToDoShareAccountDto toDoShareAccountDto;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        final ToDoShareAccount toDoShareAccount = new ToDoShareAccount();
        toDoShareAccount.setId(TODOSHARE_ACCOUNT_ID);
        toDoShareAccountDto = new ToDoShareAccountDto(toDoShareAccount);
        mockMvc = MockMvcBuilders.standaloneSetup(accountServiceController)
                .build();
    }

    @Test
    public void shouldGetAccountByUserId() throws Exception {
        when(accountService.getAccountByUserId(USER_ID)).thenReturn(toDoShareAccountDto);
        mockMvc.perform(get("/account/" + String.valueOf(USER_ID))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", new Object[]{}).value(Long.valueOf(TODOSHARE_ACCOUNT_ID).intValue()))
                .andExpect(status().isOk());

        verify(accountService).getAccountByUserId(USER_ID);
    }

    @Test
    public void shouldHandleAccountServiceException() throws Exception {
        when(accountService.getAccountByUserId(FAKE_USER_ID))
                .thenThrow(new AccountServiceException("Wrong user ID"));
        mockMvc.perform(get("/account/" + String.valueOf(FAKE_USER_ID))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        verify(accountService).getAccountByUserId(FAKE_USER_ID);
    }
}