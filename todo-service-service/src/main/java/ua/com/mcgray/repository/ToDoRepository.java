package ua.com.mcgray.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import ua.com.mcgray.domain.ToDo;
import ua.com.mcgray.domain.ToDoShareAccount;

/**
 * @author orezchykov
 * @since 04.12.14
 */

public interface ToDoRepository extends JpaRepository<ToDo, Long>, QueryDslPredicateExecutor<ToDo> {

    List<ToDo> findByCreatedBy(ToDoShareAccount createdBy);

    List<ToDo> findByCreatedById(Long toDoShareAccountId);
}
