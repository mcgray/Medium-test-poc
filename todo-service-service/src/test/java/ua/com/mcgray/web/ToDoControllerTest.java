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
import ua.com.mcgray.domain.ToDo;
import ua.com.mcgray.domain.ToDoDto;
import ua.com.mcgray.exception.ToDoServiceException;
import ua.com.mcgray.service.ToDoService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ToDoControllerTest {

    public static final long TODO_ID1 = 1L;
    public static final String TITLE1 = "Title1";
    public static final String TITLE2 = "Title2";

    private MockMvc mockMvc;

    @Mock
    private ToDoService toDoService;

    @InjectMocks
    private ToDoController toDoController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(toDoController).alwaysDo(print()).build();

    }

    @Test
    public void shouldGetToDoByAccount() throws Exception {
        ToDo toDo1 = new ToDo();
        toDo1.setId(TODO_ID1);
        toDo1.setTitle(TITLE1);
        ToDo toDo2 = new ToDo();
        toDo2.setId(2L);
        toDo2.setTitle(TITLE2);
        when(toDoService.getByAccountId(1L)).thenReturn(Arrays.asList(new ToDoDto(toDo1), new ToDoDto(toDo2)));

        mockMvc.perform(get("/api/todo/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].title").value(TITLE1))
                .andExpect(jsonPath("$.[1].title").value(TITLE2))
                .andExpect(status().isOk());

        verify(toDoService).getByAccountId(1L);


    }

    @Test
    public void shouldThrowExceptionIfThereIsNoUserOrAccount() throws Exception {
        when(toDoService.getByAccountId(TODO_ID1)).thenThrow(ToDoServiceException.class);

        mockMvc.perform(get("/api/todo/1"))
                .andExpect(status().isBadRequest());


    }
}