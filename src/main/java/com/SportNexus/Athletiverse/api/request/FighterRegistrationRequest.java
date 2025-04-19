package com.SportNexus.Athletiverse.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class FighterRegistrationRequest {
    // User basic information
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Username can only contain letters, numbers, underscores and hyphens")
    private String username;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", 
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;
    
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    // Fighter-specific information
    @Size(max = 50, message = "Nickname cannot exceed 50 characters")
    private String nickname;
    
    @Size(max = 30, message = "Weight class cannot exceed 30 characters")
    private String weightClass;
    
    @Min(value = 100, message = "Height must be at least 100 cm")
    @Max(value = 250, message = "Height cannot exceed 250 cm")
    private Double heightCm;
    
    @Min(value = 40, message = "Weight must be at least 40 kg")
    @Max(value = 200, message = "Weight cannot exceed 200 kg")
    private Double weightKg;
    
    @Min(value = 100, message = "Reach must be at least 100 cm")
    @Max(value = 250, message = "Reach cannot exceed 250 cm")
    private Double reach;
    
    @Size(max = 30, message = "Stance cannot exceed 30 characters")
    private String stance;
    
    @Size(max = 100, message = "Gym name cannot exceed 100 characters")
    private String gymName;
    
    @URL(message = "Profile music URL must be a valid URL")
    @Size(max = 2000, message = "URL cannot exceed 2000 characters")
    private String profileMusicUrl;
    
    @URL(message = "AI video URL must be a valid URL")
    @Size(max = 2000, message = "URL cannot exceed 2000 characters")
    private String aiVideoUrl;
    
    @Min(value = 0, message = "Wins cannot be negative")
    private Integer wins;
    
    @Min(value = 0, message = "Losses cannot be negative")
    private Integer losses;
    
    @Min(value = 0, message = "Draws cannot be negative")
    private Integer draws;
}