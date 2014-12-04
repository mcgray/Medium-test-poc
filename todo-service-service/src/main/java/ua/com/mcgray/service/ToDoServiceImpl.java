package ua.com.mcgray.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.mcgray.domain.ToDo;
import ua.com.mcgray.domain.ToDoDto;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.ToDoServiceException;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.repository.ToDoRepository;
import ua.com.mcgray.repository.ToDoShareAccountRepository;

/**
 * @author orezchykov
 * @since 04.12.14
 */

@Service
public class ToDoServiceImpl implements ToDoService {

    private static Logger logger = LoggerFactory.getLogger(ToDoServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private ToDoShareAccountRepository toDoShareAccountRepository;

    @Override
    public List<ToDoDto> getByAccountId(final Long userId) {
        UserDto userDto;
        try {
            userDto = userService.get(userId);
        } catch (UserServiceException e) {
            String msg = "There is no user with id: " + userId;
            logger.error(msg);
            throw new ToDoServiceException(msg, e);
        }
        ToDoShareAccount toDoShareAccount = toDoShareAccountRepository.findOne(userDto.getToDoShareAccountId());
        if (toDoShareAccount == null) {
            throw new ToDoServiceException("There is no account with id:" + userDto.getToDoShareAccountId());
        }
        List<ToDo> toDos = toDoRepository.findByCreatedBy(toDoShareAccount);
        return toDos.stream().map(ToDoDto::new).collect(Collectors.toList());
    }
}
