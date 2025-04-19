package com.SportNexus.Athletiverse.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull
    private Long contentId;
    
    @NotBlank
    @Size(min = 1, max = 1000)
    private String text;
    
    private Long parentCommentId;
}
