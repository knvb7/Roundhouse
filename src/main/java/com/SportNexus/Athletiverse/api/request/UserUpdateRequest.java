package com.SportNexus.Athletiverse.api.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UserUpdateRequest {
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;
    
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
    
    @URL(message = "Profile picture URL must be a valid URL")
    @Size(max = 2000, message = "URL cannot exceed 2000 characters")
    private String profilePicUrl;
    
    @URL(message = "Walkout song URL must be a valid URL")
    @Size(max = 2000, message = "URL cannot exceed 2000 characters")
    private String walkoutSongUrl;
    
    @URL(message = "Avatar video URL must be a valid URL")
    @Size(max = 2000, message = "URL cannot exceed 2000 characters")
    private String avatarVideoUrl;
}
