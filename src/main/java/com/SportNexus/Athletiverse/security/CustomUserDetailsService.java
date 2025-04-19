package com.SportNexus.Athletiverse.security;

import com.SportNexus.Athletiverse.exception.AthletiverseException;
import com.SportNexus.Athletiverse.model.User;
import com.SportNexus.Athletiverse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger logger = LogManager.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailId(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) throws AthletiverseException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AthletiverseException("User not found with id : " + id, HttpStatus.NOT_FOUND, logger));

        return UserPrincipal.create(user);
    }
}
