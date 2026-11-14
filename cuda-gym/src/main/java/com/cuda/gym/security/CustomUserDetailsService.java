package com.cuda.gym.security;

import com.cuda.gym.domain.model.User;
import com.cuda.gym.domain.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private static final String USER_NOT_FOUND_MSG = "User with email %s not found";

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

       @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email));
        }
        return new CustomUserDetails(user);
    }
}