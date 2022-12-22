package ru.project.forpopcorn.service;

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
@Transactional(readOnly = true)
public class ProfileDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public ProfileDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByNickname(username).orElseThrow(()-> new UsernameNotFoundException("User with nickname " + username + " not found"));
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        return new User(user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    public User loadUserById(int id) {
        return userRepository.findUserById(id).orElse(null);
    }
}
