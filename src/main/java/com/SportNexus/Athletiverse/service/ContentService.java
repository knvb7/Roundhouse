package com.SportNexus.Athletiverse.service;

import com.SportNexus.Athletiverse.api.request.CommentRequest;
import com.SportNexus.Athletiverse.api.response.CommentResponse;
import com.SportNexus.Athletiverse.exception.AthletiverseException;

import java.util.List;

public interface ContentService {
    void likeContent(Long contentId, Long userId) throws AthletiverseException;
    
    void unlikeContent(Long contentId, Long userId) throws AthletiverseException;
    
    boolean isContentLikedByUser(Long contentId, Long userId) throws AthletiverseException;
    
    CommentResponse addComment(CommentRequest request, Long userId) throws AthletiverseException;
    
    List<CommentResponse> getContentComments(Long contentId, int page, int size, Long currentUserId) throws AthletiverseException;
    
    void deleteComment(Long commentId, Long userId) throws AthletiverseException;
    
    void likeComment(Long commentId, Long userId) throws AthletiverseException;
    
    void unlikeComment(Long commentId, Long userId) throws AthletiverseException;
    
    boolean toggleLike(Long contentId, Long userId);
}
