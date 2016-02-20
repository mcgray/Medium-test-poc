package ua.com.mcgray.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.UserServiceException;

/**
 * @author orezchykov
 * @since 01.12.14
 */

@FeignClient("userService")
public interface UserService {

    @RequestMapping(value = "/users", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    List<UserDto> getAll();

    @RequestMapping(value = "user/{userId}", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto get(@PathVariable("userId") Long userId) throws UserServiceException;
}
