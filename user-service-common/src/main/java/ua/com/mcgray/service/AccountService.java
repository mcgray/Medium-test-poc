package ua.com.mcgray.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.exception.AccountServiceException;

/**
 * @author orezchykov
 * @since 07.02.15
 */

@FeignClient("accountService")
public interface AccountService {

    @RequestMapping(value = "/account/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ToDoShareAccountDto getAccountByUserId(@PathVariable("userId") Long userId) throws AccountServiceException;

}
