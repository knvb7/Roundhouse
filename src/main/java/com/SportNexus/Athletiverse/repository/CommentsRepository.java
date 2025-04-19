package com.SportNexus.Athletiverse.repository;

import com.SportNexus.Athletiverse.model.Comments;
import com.SportNexus.Athletiverse.model.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Page<Comments> findByContentAndParentCommentIsNullAndDeletedFalseOrderByCreatedAtDesc(Content content, Pageable pageable);
    
    List<Comments> findByParentCommentIdAndDeletedFalseOrderByCreatedAtAsc(Long parentCommentId);
    
    long countByContentAndDeletedFalse(Content content);
}
