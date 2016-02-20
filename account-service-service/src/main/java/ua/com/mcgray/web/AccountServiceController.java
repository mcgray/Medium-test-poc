package ua.com.mcgray.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.com.mcgray.domain.ToDoShareAccountDto;
import ua.com.mcgray.exception.AccountServiceException;
import ua.com.mcgray.service.AccountService;

/**
 * @author orezchykov
 * @since 2/17/16
 */

@RestController
public class AccountServiceController {

    private static Logger logger = LoggerFactory.getLogger(AccountServiceController.class);

    @Qualifier("localAccountService")
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/account/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ToDoShareAccountDto getAccountByUserId(@PathVariable Long userId) {
        return accountService.getAccountByUserId(userId);
    }


    @ExceptionHandler(AccountServiceException.class)
    public ResponseEntity<String> handleAccountServiceException(AccountServiceException e) {
        logger.warn("App exception handler caught AccountServiceException: {}", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

    }







}
