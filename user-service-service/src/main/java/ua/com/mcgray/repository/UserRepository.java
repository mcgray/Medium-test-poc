package ua.com.mcgray.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.mcgray.domain.User;

/**
 * @author orezchykov
 * @since 03.12.14
 */

public interface UserRepository extends JpaRepository<User, Long> {



}
