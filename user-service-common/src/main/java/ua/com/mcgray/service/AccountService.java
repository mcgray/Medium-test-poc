package ua.com.mcgray.service;

import ua.com.mcgray.domain.ToDoShareAccount;
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.exception.AccountServiceException;

/**
 * @author orezchykov
 * @since 07.02.15
 */
public interface AccountService {


    ToDoShareAccountDto getByUserId(Long userId) throws AccountServiceException;

}
