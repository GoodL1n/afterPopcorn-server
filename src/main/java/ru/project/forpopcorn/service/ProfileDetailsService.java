package ru.project.forpopcorn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.forpopcorn.entity.User;
import ru.project.forpopcorn.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByNickname(username).orElseThrow(()-> new UsernameNotFoundException("User with nickname " + username + " not found"));
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getNickname())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
        return userDetails;
//        return new User(user.getId(),
//                user.getUsername(),
//                user.getPassword(),
//                user.getRoles());
    }

    public User loadUserById(int id) {
        return userRepository.findUserById(id).orElse(null);
    }
}
