package com.SportNexus.Athletiverse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "fighter_stats")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FighterStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "profile_music_url")
    private String profileMusicUrl;

    @Column(name = "ai_video_url")
    private String aiVideoUrl;

    @Column(name = "weight_class")
    private String weightClass;

    @Column(name = "height_cm")
    private Double heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "wins")
    private Integer wins = 0;

    @Column(name = "losses")
    private Integer losses = 0;

    @Column(name = "draws")
    private Integer draws = 0;

    @Column(name = "gym_name")
    private String gymName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "stance")
    private String stance;

    @Column(name = "reach")
    private Double reach;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}