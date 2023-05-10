package ru.project.forpopcorn.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.forpopcorn.dto.UserDTO;
import ru.project.forpopcorn.entity.User;
import ru.project.forpopcorn.entity.enums.Role;
import ru.project.forpopcorn.exceptions.UserExistException;
import ru.project.forpopcorn.payload.request.SignupRequest;
import ru.project.forpopcorn.repository.UserRepository;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User createUser(SignupRequest signupRequest){
        User user = new User();
        user.setNickname(signupRequest.getNickname());
        user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        try {
            LOG.info("User with nickname " + user.getNickname() + " created");
            return userRepository.save(user);
        } catch (Exception e){
            LOG.info("Error after registration ", e.getMessage());
            throw new UserExistException("The user with nickname " + user.getNickname() + " already exist");
        }
    }

    @Transactional
    public User updateUser(UserDTO userDTO, Principal principal){
        User user = userRepository.findUserByNickname(principal.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User with this nickname not found"));
        user.setNickname(userDTO.getNickname());
        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal){
        return userRepository.findUserByNickname(principal.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User with this nickname not found"));
    }

    public User getUserById(int id){
        return userRepository.findUserById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
