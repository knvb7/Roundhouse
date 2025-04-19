package com.SportNexus.Athletiverse.repository;

import com.SportNexus.Athletiverse.model.Follower;
import com.SportNexus.Athletiverse.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Optional<Follower> findByFollowerAndFollowing(User follower, User following);
    
    boolean existsByFollowerAndFollowing(User follower, User following);
    
    Page<Follower> findByFollowing(User following, Pageable pageable);
    
    Page<Follower> findByFollower(User follower, Pageable pageable);
    
    long countByFollowing(User following);
    
    long countByFollower(User follower);
    
    @Query(value = "SELECT f.following_id FROM followers f WHERE f.follower_id = :userId", nativeQuery = true)
    List<Long> findFollowingUserIds(Long userId, Pageable pageable);
    
    @Query(value = "SELECT f.follower_id FROM followers f WHERE f.following_id = :userId", nativeQuery = true)
    List<Long> findFollowerUserIds(Long userId, Pageable pageable);
    
    void deleteByFollowerAndFollowing(User follower, User following);
}
