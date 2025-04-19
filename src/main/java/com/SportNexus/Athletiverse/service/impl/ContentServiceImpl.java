package com.SportNexus.Athletiverse.service.impl;

import com.SportNexus.Athletiverse.api.request.CommentRequest;
import com.SportNexus.Athletiverse.api.response.CommentResponse;
import com.SportNexus.Athletiverse.exception.AthletiverseException;
import com.SportNexus.Athletiverse.model.Comments;
import com.SportNexus.Athletiverse.model.Content;
import com.SportNexus.Athletiverse.model.Likes;
import com.SportNexus.Athletiverse.model.User;
import com.SportNexus.Athletiverse.repository.CommentsRepository;
import com.SportNexus.Athletiverse.repository.ContentRepository;
import com.SportNexus.Athletiverse.repository.LikesRepository;
import com.SportNexus.Athletiverse.repository.UserRepository;
import com.SportNexus.Athletiverse.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final Logger logger = LogManager.getLogger(ContentServiceImpl.class);
    private static final org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(ContentServiceImpl.class);
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final CommentsRepository commentsRepository;

    @Override
    @Transactional
    public void likeContent(Long contentId, Long userId) throws AthletiverseException {
        User user = getUserById(userId);
        Content content = getContentById(contentId);

        // Check if already liked
        if (likesRepository.existsByUserAndContent(user, content)) {
            throw new AthletiverseException("Content already liked by user", HttpStatus.BAD_REQUEST, logger);
        }

        // Create like
        Likes like = new Likes();
        like.setUser(user);
        like.setContent(content);
        likesRepository.save(like);

        // Update content likes count
        content.setLikesCount(content.getLikesCount() + 1);
        contentRepository.save(content);
    }

    @Override
    @Transactional
    public void unlikeContent(Long contentId, Long userId) throws AthletiverseException {
        User user = getUserById(userId);
        Content content = getContentById(contentId);

        // Check if liked
        Likes like = likesRepository.findByUserAndContent(user, content)
                .orElseThrow(() -> new AthletiverseException("Content not liked by user", HttpStatus.BAD_REQUEST, logger));

        // Delete like
        likesRepository.delete(like);

        // Update content likes count
        if (content.getLikesCount() > 0) {
            content.setLikesCount(content.getLikesCount() - 1);
            contentRepository.save(content);
        }
    }

    @Override
    public boolean isContentLikedByUser(Long contentId, Long userId) throws AthletiverseException {
        User user = getUserById(userId);
        Content content = getContentById(contentId);
        return likesRepository.existsByUserAndContent(user, content);
    }

    @Override
    @Transactional
    public CommentResponse addComment(CommentRequest request, Long userId) throws AthletiverseException {
        User user = getUserById(userId);
        Content content = getContentById(request.getContentId());

        Comments comment = new Comments();
        comment.setUser(user);
        comment.setContent(content);
        comment.setText(request.getText());

        // Set parent comment if it exists
        if (request.getParentCommentId() != null) {
            Comments parentComment = commentsRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new AthletiverseException("Parent comment not found", HttpStatus.NOT_FOUND, logger));
            comment.setParentComment(parentComment);
        }

        Comments savedComment = commentsRepository.save(comment);

        // Update content comments count
        content.setCommentsCount(content.getCommentsCount() + 1);
        contentRepository.save(content);

        return mapToCommentResponse(savedComment, userId);
    }

    @Override
    public List<CommentResponse> getContentComments(Long contentId, int page, int size, Long currentUserId) throws AthletiverseException {
        Content content = getContentById(contentId);
        Pageable pageable = PageRequest.of(page, size);

        // Get top-level comments
        Page<Comments> commentPage = commentsRepository.findByContentAndParentCommentIsNullAndDeletedFalseOrderByCreatedAtDesc(content, pageable);

        // Map to response objects
        List<CommentResponse> commentResponses = commentPage.getContent().stream()
                .map(comment -> mapToCommentResponse(comment, currentUserId))
                .collect(Collectors.toList());

        // For each comment, get its replies
        for (CommentResponse commentResponse : commentResponses) {
            List<Comments> replies = commentsRepository.findByParentCommentIdAndDeletedFalseOrderByCreatedAtAsc(commentResponse.getId());
            List<CommentResponse> replyResponses = replies.stream()
                    .map(reply -> mapToCommentResponse(reply, currentUserId))
                    .collect(Collectors.toList());
            commentResponse.setReplies(replyResponses);
        }

        return commentResponses;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) throws AthletiverseException {
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new AthletiverseException("Comment not found", HttpStatus.NOT_FOUND, logger));

        // Check if user is authorized to delete this comment
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new AthletiverseException("Not authorized to delete this comment", HttpStatus.FORBIDDEN, logger);
        }

        // Soft delete
        comment.setDeleted(true);
        commentsRepository.save(comment);

        // Update comment count on the content
        Content content = comment.getContent();
        if (content.getCommentsCount() > 0) {
            content.setCommentsCount(content.getCommentsCount() - 1);
            contentRepository.save(content);
        }
    }

    @Override
    @Transactional
    public void likeComment(Long commentId, Long userId) throws AthletiverseException {
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new AthletiverseException("Comment not found", HttpStatus.NOT_FOUND, logger));

        // Increment likes count
        comment.setLikesCount(comment.getLikesCount() + 1);
        commentsRepository.save(comment);

        // Note: In a real implementation, you might want to track which users liked which comments
        // This would require a CommentLike entity similar to the Likes entity for content
    }

    @Override
    @Transactional
    public void unlikeComment(Long commentId, Long userId) throws AthletiverseException {
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new AthletiverseException("Comment not found", HttpStatus.NOT_FOUND, logger));

        // Decrement likes count if greater than 0
        if (comment.getLikesCount() > 0) {
            comment.setLikesCount(comment.getLikesCount() - 1);
            commentsRepository.save(comment);
        }

        // Note: In a real implementation, you would remove the CommentLike record
    }

    @Override
    @Transactional
    public boolean toggleLike(Long contentId, Long userId) {
        slf4jLogger.info("toggleLike called for contentId={} by userId={}", contentId, userId);
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Content> contentOpt = contentRepository.findById(contentId);
        if (userOpt.isEmpty() || contentOpt.isEmpty()) {
            slf4jLogger.warn("User or Content not found. userId={}, contentId={}", userId, contentId);
            throw new IllegalArgumentException("User or Content not found");
        }
        User user = userOpt.get();
        Content content = contentOpt.get();
        Optional<Likes> existingLike = likesRepository.findByUserAndContent(user, content);
        if (existingLike.isPresent()) {
            slf4jLogger.debug("Existing like found. Removing like for userId={}, contentId={}", userId, contentId);
            likesRepository.delete(existingLike.get());
            if (content.getLikesCount() != null && content.getLikesCount() > 0) {
                content.setLikesCount(content.getLikesCount() - 1);
                contentRepository.save(content);
                slf4jLogger.info("Decremented likesCount for contentId={} to {}", contentId, content.getLikesCount());
            }
            slf4jLogger.info("User {} unliked content {}", userId, contentId);
            return false; // Now unliked
        } else {
            Likes like = new Likes();
            like.setUser(user);
            like.setContent(content);
            like.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            likesRepository.save(like);
            if (content.getLikesCount() == null) {
                content.setLikesCount(1);
            } else {
                content.setLikesCount(content.getLikesCount() + 1);
            }
            contentRepository.save(content);
            slf4jLogger.info("User {} liked content {}. likesCount={}", userId, contentId, content.getLikesCount());
            return true; // Now liked
        }
    }

    // Helper methods
    private User getUserById(Long userId) throws AthletiverseException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AthletiverseException("User not found", HttpStatus.NOT_FOUND, logger));
    }

    private Content getContentById(Long contentId) throws AthletiverseException {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new AthletiverseException("Content not found", HttpStatus.NOT_FOUND, logger));
    }

    private CommentResponse mapToCommentResponse(Comments comment, Long currentUserId) {
        String displayText = comment.isDeleted() ? "[Comment deleted]" : comment.getText();

        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUser().getUserId())
                .username(comment.getUser().getUsername())
                .userProfilePic(comment.getUser().getProfilePicUrl())
                .text(displayText)
                .contentId(comment.getContent().getContentId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .likesCount(comment.getLikesCount())
                .deleted(comment.isDeleted())
                .isLikedByCurrentUser(false) // Would need a CommentLike entity to track this properly
                .createdAt(comment.getCreatedAt() != null ? comment.getCreatedAt().toInstant() : null)
                .updatedAt(comment.getUpdatedAt() != null ? comment.getUpdatedAt().toInstant() : null)
                .replies(new ArrayList<>()) // Will be populated later for top-level comments
                .build();
    }
}
