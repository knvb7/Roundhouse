package com.SportNexus.Athletiverse.repository;

import com.SportNexus.Athletiverse.model.FighterStats;
import com.SportNexus.Athletiverse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FighterStatsRepository extends JpaRepository<FighterStats, Long> {
    Optional<FighterStats> findByUser(User user);
}
