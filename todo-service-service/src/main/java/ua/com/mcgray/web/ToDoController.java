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
import ua.com.mcgray.domain.ToDoDto;
import ua.com.mcgray.exception.ToDoServiceException;
import ua.com.mcgray.service.ToDoService;

/**
 * @author orezchykov
 * @since 04.12.14
 */

@RestController
public class ToDoController {

    private static Logger logger = LoggerFactory.getLogger(ToDoController.class);

    @Autowired
    private ToDoService toDoService;

    @RequestMapping(value = "/todo/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ToDoDto> getToDoByAccount(@PathVariable Long userId) {
        return toDoService.getByUserId(userId);
    }

    @ExceptionHandler(ToDoServiceException.class)
    public ResponseEntity<String> handleToDoServiceException(ToDoServiceException e) {
        logger.warn("App exception handler caught ToDoServiceException: {}", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }





}
