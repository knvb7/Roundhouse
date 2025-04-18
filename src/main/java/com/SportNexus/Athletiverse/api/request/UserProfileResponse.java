package com.SportNexus.Athletiverse.api.request;

import com.SportNexus.Athletiverse.util.UserRole;
import lombok.Data;

@Data
public class UserProfileResponse {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String location;
    private String bio;
    private UserRole role;
    private String profilePictureUrl;
    private String walkoutSongUrl;
    private String avatarVideoUrl;
    private Double totalDonationsReceived;
    private long followersCount;
    private long followingCount;
}
