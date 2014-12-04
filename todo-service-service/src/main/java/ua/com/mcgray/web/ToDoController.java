package ua.com.mcgray.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.com.mcgray.domain.ToDoDto;
import ua.com.mcgray.service.ToDoService;

/**
 * @author orezchykov
 * @since 04.12.14
 */

@RestController
@RequestMapping("/api")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @RequestMapping(value = "/todo/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ToDoDto> getToDoByAccount(@PathVariable Long userId) {
        return toDoService.getByAccountId(userId);

    }



}
