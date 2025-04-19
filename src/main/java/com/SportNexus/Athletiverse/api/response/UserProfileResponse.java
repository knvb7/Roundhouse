package com.SportNexus.Athletiverse.api.response;

import com.SportNexus.Athletiverse.util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
public class UserProfileResponse {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String location;
    private String bio;
    private boolean active;
    private UserRole role;
    private String profilePicUrl;
    private int followersCount;
    private int followingCount;
    private Instant createdAt;
    private Instant updatedAt;
}
