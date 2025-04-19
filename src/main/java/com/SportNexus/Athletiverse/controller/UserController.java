package com.SportNexus.Athletiverse.controller;

import com.SportNexus.Athletiverse.api.request.ChangePasswordRequest;
import com.SportNexus.Athletiverse.api.request.UserUpdateRequest;
import com.SportNexus.Athletiverse.api.response.UserProfileResponse;
import com.SportNexus.Athletiverse.api.request.UserRegistrationRequest;
import com.SportNexus.Athletiverse.exception.AthletiverseException;
import com.SportNexus.Athletiverse.security.UserPrincipal;
import com.SportNexus.Athletiverse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) throws AthletiverseException {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId) throws AthletiverseException {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserProfileResponse> getUserByUsername(@PathVariable String username) throws AthletiverseException {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequest request) throws AthletiverseException {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @GetMapping("/athletes")
    public ResponseEntity<List<UserProfileResponse>> getAllAthletes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllAthletes(page, size));
    }

    @PostMapping("/{userId}/follow/{targetUserId}")
    public ResponseEntity<Void> followUser(
            @PathVariable Long userId,
            @PathVariable Long targetUserId) {
        userService.followUser(userId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/unfollow/{targetUserId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable Long userId,
            @PathVariable Long targetUserId) {
        userService.unfollowUser(userId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserProfileResponse>> getUserFollowers(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUserFollowers(userId, page, size));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserProfileResponse>> getUserFollowing(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUserFollowing(userId, page, size));
    }

    @PostMapping("/{userId}/donate/{targetUserId}")
    public ResponseEntity<Void> donateToUser(
            @PathVariable Long userId,
            @PathVariable Long targetUserId,
            @RequestParam Double amount) {
        userService.processDonation(userId, targetUserId, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication) throws AthletiverseException {
        Long userId = ((UserPrincipal) authentication.getPrincipal()).getId();
        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }
}
