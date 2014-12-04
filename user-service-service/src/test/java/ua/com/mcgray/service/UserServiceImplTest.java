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
import ua.com.mcgray.domain.User;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.repository.UserRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    public static final long USER_ID = 1L;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private User user1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        user1 = new User();
        user1.setId(USER_ID);
    }

    @Test
    public void shouldGetAllUserDtos() throws Exception {
        User user2 = new User();
        user2.setId(2L);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<UserDto> userDtos = userService.getAll();
        assertThat(userDtos).isNotNull().hasSize(2);
        assertThat(userDtos.get(0).getId()).isEqualTo(1);
        assertThat(userDtos.get(1).getId()).isEqualTo(2);
        verify(userRepository).findAll();

    }

    @Test
    public void shouldGetUserDto() throws Exception {
        when(userRepository.findOne(USER_ID)).thenReturn(user1);

        UserDto userDto = userService.get(USER_ID);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(USER_ID);

        verify(userRepository).findOne(USER_ID);

    }

    @Test
    public void shouldThrowUserServiceExceptionWhenNoUserFound() throws Exception {
        when(userRepository.findOne(USER_ID)).thenReturn(null);

        thrown.expect(UserServiceException.class);
        thrown.expectMessage("There is no user with such id");
        userService.get(USER_ID);


    }
}