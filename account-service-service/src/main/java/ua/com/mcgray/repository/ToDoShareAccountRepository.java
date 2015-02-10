package ua.com.mcgray.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.mcgray.domain.ToDoShareAccount;

/**
 * @author orezchykov
 * @since 04.12.14
 */

public interface ToDoShareAccountRepository extends JpaRepository<ToDoShareAccount, Long> {



}
