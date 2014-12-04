package ua.com.mcgray.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.mcgray.domain.ToDo;
import ua.com.mcgray.domain.ToDoDto;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.repository.ToDoRepository;
import ua.com.mcgray.repository.ToDoShareAccountRepository;

/**
 * @author orezchykov
 * @since 04.12.14
 */

@Service
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    private UserService userService;

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private ToDoShareAccountRepository toDoShareAccountRepository;

    @Override
    public List<ToDoDto> getByAccountId(final Long userId) {
        UserDto userDto = userService.get(userId);
        ToDoShareAccount toDoShareAccount = toDoShareAccountRepository.findOne(userDto.getToDoShareAccountId());
        List<ToDo> toDos = toDoRepository.findByCreatedBy(toDoShareAccount);
        return toDos.stream().map(ToDoDto::new).collect(Collectors.toList());
    }
}
