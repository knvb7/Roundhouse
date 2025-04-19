package com.SportNexus.Athletiverse.controller;

import com.SportNexus.Athletiverse.api.request.CommentRequest;
import com.SportNexus.Athletiverse.api.response.CommentResponse;
import com.SportNexus.Athletiverse.exception.AthletiverseException;
import com.SportNexus.Athletiverse.security.UserPrincipal;
import com.SportNexus.Athletiverse.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContentController {

    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    private final ContentService contentService;

    // The like-toggle API remains

    @PostMapping("/{contentId}/like-toggle")
    public ResponseEntity<?> toggleLike(@PathVariable Long contentId, Authentication authentication) {
        Long userId = ((UserPrincipal) authentication.getPrincipal()).getId();
        logger.info("API called: POST /api/content/{}/like-toggle by user {}", contentId, userId);
        boolean liked = contentService.toggleLike(contentId, userId);
        logger.info("toggleLike result for contentId={}, userId={}: liked={}", contentId, userId, liked);
        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        response.put("contentId", contentId);
        // Optionally add like count
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> addComment(
            @Valid @RequestBody CommentRequest request,
            @RequestParam Long userId) throws AthletiverseException {
        CommentResponse comment = contentService.addComment(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/{contentId}/comments")
    public ResponseEntity<List<CommentResponse>> getContentComments(
            @PathVariable Long contentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long currentUserId) throws AthletiverseException {
        List<CommentResponse> comments = contentService.getContentComments(contentId, page, size, currentUserId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) throws AthletiverseException {
        contentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
