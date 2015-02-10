package ua.com.mcgray.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.AccountServiceException;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.repository.ToDoShareAccountRepository;

/**
 * @author orezchykov
 * @since 07.02.15
 */

@Service
public class AccountServiceImpl implements AccountService {

    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ToDoShareAccountRepository toDoShareAccountRepository;

    @Override
    public ToDoShareAccountDto getByUserId(final Long userId) throws AccountServiceException {
        if (userId == null) {
            String msg = "UserId cannot be null";
            logger.error(msg);
            throw new AccountServiceException(msg);
        }
        UserDto userDto;
        try {
            userDto = userService.get(userId);
        } catch (UserServiceException e) {
            String msg = "There is no user with id: " + userId;
            logger.error(msg);
            throw new AccountServiceException(msg, e);
        }
        ToDoShareAccount toDoShareAccount = toDoShareAccountRepository.findOne(userDto.getToDoShareAccountId());

        if (toDoShareAccount == null) {
            throw new AccountServiceException("There is no account with id:" + userDto.getToDoShareAccountId());
        }

        return new ToDoShareAccountDto(toDoShareAccount);
    }
}
