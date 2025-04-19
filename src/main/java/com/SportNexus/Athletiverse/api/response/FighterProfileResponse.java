package com.SportNexus.Athletiverse.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FighterProfileResponse {
    // User basic information
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String location;
    private String bio;
    private String profilePicUrl;
    private int followersCount;
    private int followingCount;
    
    // Fighter specific information
    private String nickname;
    private String weightClass;
    private Double heightCm;
    private Double weightKg;
    private Double reach;
    private String stance;
    private String gymName;
    private String profileMusicUrl;
    private String aiVideoUrl;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private String record; // Formatted as "W-L-D"
    
    private Instant createdAt;
    private Instant updatedAt;
}
