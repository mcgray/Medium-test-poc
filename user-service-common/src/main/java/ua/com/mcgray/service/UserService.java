package ua.com.mcgray.service;

import java.util.List;

import ua.com.mcgray.dto.UserDto;

/**
 * @author orezchykov
 * @since 01.12.14
 */
public interface UserService {

    List<UserDto> getAll();


    UserDto get(Long userId);
}
