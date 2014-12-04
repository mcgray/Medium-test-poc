package ua.com.mcgray.web;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.mcgray.domain.User;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.service.UserService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserServiceControllerTest {

    public static final long USER_ID = 1L;
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserServiceController userServiceController;
    private User user1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userServiceController).alwaysDo(print()).build();
        user1 = new User();
        user1.setId(USER_ID);

    }

    @Test
    public void shouldGetUsers() throws Exception {
        User user2 = new User();
        user2.setId(2L);

        when(userService.getAll()).thenReturn(Arrays.asList(new UserDto(user1), new UserDto(user2)));
        mockMvc.perform(get("/api/users/")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].id", new Object[]{}).value(1))
                .andExpect(jsonPath("$.[1].id", new Object[]{}).value(2))
                .andExpect(status().isOk());

        verify(userService).getAll();
    }

    @Test
    public void shouldGetUser() throws Exception {
        when(userService.get(USER_ID)).thenReturn(new UserDto(user1));
        mockMvc.perform(get("/api/user/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", new Object[]{}).value(1))
                .andExpect(status().isOk());

        verify(userService).get(USER_ID);

    }

    @Test
    public void shouldReturnStatusWhenUserServiceExceptionIsThrown() throws Exception {
        when(userService.get(USER_ID)).thenThrow(new UserServiceException("There is no user with such id"));
        mockMvc.perform(get("/api/user/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        verify(userService).get(USER_ID);

    }
}