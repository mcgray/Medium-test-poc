package ua.com.mcgray.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.mcgray.domain.User;
import ua.com.mcgray.dto.UserDto;
import ua.com.mcgray.exception.UserServiceException;
import ua.com.mcgray.repository.UserRepository;

/**
 * @author orezchykov
 * @since 03.12.14
 *
 */

@Service("localUserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }

    @Override
    public UserDto get(final Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new UserServiceException("There is no user with such id:" + userId);
        }
        return new UserDto(user);
    }


}
