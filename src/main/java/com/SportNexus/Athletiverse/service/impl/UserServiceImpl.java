package com.SportNexus.Athletiverse.service.impl;

import com.SportNexus.Athletiverse.api.request.FighterRegistrationRequest;
import com.SportNexus.Athletiverse.api.request.UserRegistrationRequest;
import com.SportNexus.Athletiverse.api.request.UserUpdateRequest;
import com.SportNexus.Athletiverse.api.response.FighterProfileResponse;
import com.SportNexus.Athletiverse.api.response.UserProfileResponse;
import com.SportNexus.Athletiverse.exception.AthletiverseException;
import com.SportNexus.Athletiverse.model.FighterStats;
import com.SportNexus.Athletiverse.model.User;
import com.SportNexus.Athletiverse.repository.FighterStatsRepository;
import com.SportNexus.Athletiverse.repository.UserRepository;
import com.SportNexus.Athletiverse.service.UserService;
import com.SportNexus.Athletiverse.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final FighterStatsRepository fighterStatsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserProfileResponse registerUser(UserRegistrationRequest request) throws AthletiverseException {
        logger.info("Registering user with email: {}", request.getEmail());
        // Check if email already exists
        if (userRepository.findByEmailId(request.getEmail()).isPresent()) {
            throw new AthletiverseException("Email already in use", HttpStatus.BAD_REQUEST, logger);
        }

        // Check if the username is already taken
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AthletiverseException("Username already taken", HttpStatus.BAD_REQUEST, logger);
        }

        // Set the default role if not provided
        UserRole role = request.getRole();
        if (role == null) {
            role = UserRole.FAN;
        }

        // Create a new user
        User user = User.builder()
                .username(request.getUsername())
                .emailId(request.getEmail())
                .fullName(request.getFullName())
                .location(request.getLocation())
                .bio(request.getBio())
                .active(true)
                .role(role)
                .build();

        // Hash the password before saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmailId());
        return mapUserToProfileResponse(savedUser);
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId) throws AthletiverseException {
        User user = getUserById(userId);
        return mapUserToProfileResponse(user);
    }

    @Override
    public UserProfileResponse getUserByUsername(String username) throws AthletiverseException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AthletiverseException("User not found", HttpStatus.NOT_FOUND, logger));
        return mapUserToProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateUser(Long userId, UserUpdateRequest request) throws AthletiverseException {
        User user = getUserById(userId);

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getProfilePicUrl() != null) {
            user.setProfilePicUrl(request.getProfilePicUrl());
        }

        User updatedUser = userRepository.save(user);
        return mapUserToProfileResponse(updatedUser);
    }

    @Override
    public List<UserProfileResponse> getAllAthletes(int page, int size) {
        // Implementation will depend on how athletes are identified
        // For now, assuming athletes have role ATHLETE
        return Collections.emptyList(); // Placeholder
    }

    @Override
    @Transactional
    public void followUser(Long userId, Long targetUserId) {
        // Implementation will depend on how followers are tracked
        // Placeholder for now
    }

    @Override
    @Transactional
    public void unfollowUser(Long userId, Long targetUserId) {
        // Implementation will depend on how followers are tracked
        // Placeholder for now
    }

    @Override
    public List<UserProfileResponse> getUserFollowers(Long userId, int page, int size) {
        // Implementation will depend on how followers are tracked
        // Placeholder for now
        return Collections.emptyList();
    }

    @Override
    public List<UserProfileResponse> getUserFollowing(Long userId, int page, int size) {
        // Implementation will depend on how following is tracked
        // Placeholder for now
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public void processDonation(Long userId, Long targetUserId, Double amount) {
        // Implementation will depend on how donations are processed
        // Placeholder for now
    }

    @Override
    @Transactional
    public FighterProfileResponse registerFighter(FighterRegistrationRequest request) throws AthletiverseException {
        logger.info("Registering fighter with email: {}", request.getEmail());
        // Check if email already exists
        if (userRepository.findByEmailId(request.getEmail()).isPresent()) {
            throw new AthletiverseException("Email already in use", HttpStatus.BAD_REQUEST, logger);
        }

        // Check if username is already taken
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AthletiverseException("Username already taken", HttpStatus.BAD_REQUEST, logger);
        }

        // Create new user with ATHLETE role
        User user = User.builder()
                .username(request.getUsername())
                .emailId(request.getEmail())
                .fullName(request.getFullName())
                .location(request.getLocation())
                .bio(request.getBio())
                .active(true)
                .role(UserRole.ATHLETE)
                .build();

        // Hash the password before saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);
        logger.info("Fighter registered successfully: {}", user.getEmailId());

        // Create fighter stats
        FighterStats fighterStats = FighterStats.builder()
                .user(savedUser)
                .nickname(request.getNickname())
                .weightClass(request.getWeightClass())
                .heightCm(request.getHeightCm())
                .weightKg(request.getWeightKg())
                .reach(request.getReach())
                .stance(request.getStance())
                .gymName(request.getGymName())
                .profileMusicUrl(request.getProfileMusicUrl())
                .aiVideoUrl(request.getAiVideoUrl())
                .wins(request.getWins() != null ? request.getWins() : 0)
                .losses(request.getLosses() != null ? request.getLosses() : 0)
                .draws(request.getDraws() != null ? request.getDraws() : 0)
                .build();

        FighterStats savedFighterStats = fighterStatsRepository.save(fighterStats);

        return mapToFighterProfileResponse(savedUser, savedFighterStats);
    }

    @Override
    public FighterProfileResponse getFighterProfile(Long userId) throws AthletiverseException {
        User user = getUserById(userId);

        if (user.getRole() != UserRole.ATHLETE) {
            throw new AthletiverseException("User is not a fighter", HttpStatus.BAD_REQUEST, logger);
        }

        FighterStats fighterStats = fighterStatsRepository.findByUser(user)
                .orElseThrow(() -> new AthletiverseException("Fighter stats not found", HttpStatus.NOT_FOUND, logger));

        return mapToFighterProfileResponse(user, fighterStats);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) throws AthletiverseException {
        User user = getUserById(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            logger.warn("Failed password change attempt for user {}: old password does not match.", userId);
            throw new AthletiverseException("Old password is incorrect", HttpStatus.BAD_REQUEST, logger);
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        logger.info("Password changed successfully for user {}", userId);
    }

    // Helper methods
    private User getUserById(Long userId) throws AthletiverseException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AthletiverseException("User not found", HttpStatus.NOT_FOUND, logger));
    }

    private UserProfileResponse mapUserToProfileResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmailId());
        response.setFullName(user.getFullName());
        response.setLocation(user.getLocation());
        response.setBio(user.getBio());
        response.setActive(user.isActive());
        response.setRole(user.getRole());
        response.setProfilePicUrl(user.getProfilePicUrl());
        response.setFollowersCount(0); // Placeholder, implement actual count
        response.setFollowingCount(0); // Placeholder, implement actual count
        response.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toInstant() : null);
        response.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toInstant() : null);
        return response;
    }

    private FighterProfileResponse mapToFighterProfileResponse(User user, FighterStats stats) {
        String record = String.format("%d-%d-%d",
                stats.getWins() != null ? stats.getWins() : 0,
                stats.getLosses() != null ? stats.getLosses() : 0,
                stats.getDraws() != null ? stats.getDraws() : 0);

        return FighterProfileResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmailId())
                .fullName(user.getFullName())
                .location(user.getLocation())
                .bio(user.getBio())
                .profilePicUrl(user.getProfilePicUrl())
                .followersCount(0) // Placeholder, implement actual count
                .followingCount(0) // Placeholder, implement actual count
                .nickname(stats.getNickname())
                .weightClass(stats.getWeightClass())
                .heightCm(stats.getHeightCm())
                .weightKg(stats.getWeightKg())
                .reach(stats.getReach())
                .stance(stats.getStance())
                .gymName(stats.getGymName())
                .profileMusicUrl(stats.getProfileMusicUrl())
                .aiVideoUrl(stats.getAiVideoUrl())
                .wins(stats.getWins())
                .losses(stats.getLosses())
                .draws(stats.getDraws())
                .record(record)
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toInstant() : null)
                .updatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toInstant() : null)
                .build();
    }
}
