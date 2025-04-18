package com.SportNexus.Athletiverse.api.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;
    private String location;
    private String bio;
    private String profilePictureUrl;
    private String walkoutSongUrl;
    private String avatarVideoUrl;
}
