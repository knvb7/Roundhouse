package com.SportNexus.Athletiverse.repository;

import com.SportNexus.Athletiverse.model.Content;
import com.SportNexus.Athletiverse.model.User;
import com.SportNexus.Athletiverse.util.ContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Page<Content> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Content> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Content> findByContentTypeOrderByCreatedAtDesc(ContentType contentType, Pageable pageable);

    Page<Content> findByUserAndContentTypeOrderByCreatedAtDesc(User user, ContentType contentType, Pageable pageable);

    List<Content> findTop5ByUserOrderByLikesCountDesc(User user);

    long countByUser(User user);
}