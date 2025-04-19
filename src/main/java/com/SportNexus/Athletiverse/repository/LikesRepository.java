package com.SportNexus.Athletiverse.repository;

import com.SportNexus.Athletiverse.model.Content;
import com.SportNexus.Athletiverse.model.Likes;
import com.SportNexus.Athletiverse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserAndContent(User user, Content content);
    
    boolean existsByUserAndContent(User user, Content content);
    
    long countByContent(Content content);
    
    void deleteByUserAndContent(User user, Content content);
}
