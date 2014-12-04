package ua.com.mcgray.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.service.UserService;

/**
 * @author orezchykov
 * @since 02.12.14
 */

@RestController
@RequestMapping("/api")
public class UserServiceController {

    private static Logger logger = LoggerFactory.getLogger(UserServiceController.class);

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getUsers() {
        return userService.getAll();
    }
    
    @RequestMapping(value = "user/{userId}", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUser(@PathVariable Long userId) {
        return userService.get(userId);
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<String> handleUserServiceException(UserServiceException e) {
        logger.warn("App exception handler caught UserServiceException: {}", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

    }

}
