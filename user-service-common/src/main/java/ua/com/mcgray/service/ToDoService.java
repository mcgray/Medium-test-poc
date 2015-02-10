package ua.com.mcgray.service;

import java.util.List;

import ua.com.mcgray.domain.ToDoDto;
import ua.com.mcgray.exception.ToDoServiceException;

/**
 * @author orezchykov
 * @since 04.12.14
 */

public interface ToDoService {


    List<ToDoDto> getByUserId(final Long userId) throws ToDoServiceException;
}
