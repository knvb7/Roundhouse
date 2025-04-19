package com.SportNexus.Athletiverse.service;

import com.SportNexus.Athletiverse.api.request.FighterRegistrationRequest;
import com.SportNexus.Athletiverse.api.request.UserRegistrationRequest;
import com.SportNexus.Athletiverse.api.request.UserUpdateRequest;
import com.SportNexus.Athletiverse.api.response.FighterProfileResponse;
import com.SportNexus.Athletiverse.api.response.UserProfileResponse;
import com.SportNexus.Athletiverse.exception.AthletiverseException;

import java.util.List;

public interface UserService {
    UserProfileResponse registerUser(UserRegistrationRequest request) throws AthletiverseException;
    FighterProfileResponse registerFighter(FighterRegistrationRequest request) throws AthletiverseException;
    UserProfileResponse getUserProfile(Long userId) throws AthletiverseException;
    FighterProfileResponse getFighterProfile(Long userId) throws AthletiverseException;
    UserProfileResponse getUserByUsername(String username) throws AthletiverseException;
    UserProfileResponse updateUser(Long userId, UserUpdateRequest request) throws AthletiverseException;
    List<UserProfileResponse> getAllAthletes(int page, int size);
    void followUser(Long userId, Long targetUserId);
    void unfollowUser(Long userId, Long targetUserId);
    List<UserProfileResponse> getUserFollowers(Long userId, int page, int size);
    List<UserProfileResponse> getUserFollowing(Long userId, int page, int size);
    void processDonation(Long userId, Long targetUserId, Double amount);
    void changePassword(Long userId, String oldPassword, String newPassword) throws AthletiverseException;
}
