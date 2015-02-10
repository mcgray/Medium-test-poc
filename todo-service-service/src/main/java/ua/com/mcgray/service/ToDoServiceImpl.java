package ua.com.mcgray.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.com.mcgray.domain.ToDo;
import ua.com.mcgray.domain.ToDoDto;
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.exception.AccountServiceException;
import ua.com.mcgray.exception.ToDoServiceException;
import ua.com.mcgray.repository.ToDoRepository;

/**
 * @author orezchykov
 * @since 04.12.14
 */

@Service
public class ToDoServiceImpl implements ToDoService {

    private static Logger logger = LoggerFactory.getLogger(ToDoServiceImpl.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private ToDoRepository toDoRepository;

    @Cacheable("todo")
    @Override
    public List<ToDoDto> getByUserId(final Long userId) {
        ToDoShareAccountDto toDoShareAccountDto;
        try {
            toDoShareAccountDto = accountService.getByUserId(userId);
        } catch (AccountServiceException e) {
            String msg = "There is no account for user with id: " + userId;
            logger.error(msg);
            throw new ToDoServiceException(msg, e);

        }

        List<ToDo> toDos = toDoRepository.findByCreatedById(toDoShareAccountDto.getId());
        return toDos.stream().map(ToDoDto::new).collect(Collectors.toList());
    }
}
