package ua.com.mcgray.service;

import java.util.List;

import ua.com.mcgray.domain.ToDoDto;

/**
 * @author orezchykov
 * @since 04.12.14
 */

public interface ToDoService {


    List<ToDoDto> getByAccountId(final Long userId);
}