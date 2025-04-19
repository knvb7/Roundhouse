package com.SportNexus.Athletiverse.controller;

import com.SportNexus.Athletiverse.api.response.AuthResponse;
import com.SportNexus.Athletiverse.exception.AthletiverseException;
import com.SportNexus.Athletiverse.model.User;
import com.SportNexus.Athletiverse.repository.UserRepository;
import com.SportNexus.Athletiverse.security.TokenProvider;
import com.SportNexus.Athletiverse.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final Logger logger = LogManager.getLogger(AuthController.class);
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() throws AthletiverseException {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new AthletiverseException("User not found", HttpStatus.NOT_FOUND, logger));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/token")
    public ResponseEntity<AuthResponse> getToken(Authentication authentication) {
        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
