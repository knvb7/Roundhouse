package com.SportNexus.Athletiverse.repository;

import com.SportNexus.Athletiverse.model.Support;
import com.SportNexus.Athletiverse.model.User;
import com.SportNexus.Athletiverse.util.SupportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
    Page<Support> findByDonorOrderByCreatedAtDesc(User donor, Pageable pageable);
    
    Page<Support> findByAthleteOrderByCreatedAtDesc(User athlete, Pageable pageable);
    
    Page<Support> findByStatusOrderByCreatedAtDesc(SupportStatus status, Pageable pageable);
    
    List<Support> findByTransactionId(String transactionId);
    
    @Query(value = "SELECT SUM(s.amount) FROM supports s WHERE s.athlete_id = :athleteId AND s.status = 'COMPLETED'", nativeQuery = true)
    Double getTotalSupportAmountForAthlete(Long athleteId);
    
    @Query(value = "SELECT SUM(s.amount) FROM supports s WHERE s.donor_id = :donorId AND s.status = 'COMPLETED'", nativeQuery = true)
    Double getTotalSupportAmountByDonor(Long donorId);
    
    @Query(value = "SELECT COUNT(DISTINCT s.donor_id) FROM supports s WHERE s.athlete_id = :athleteId AND s.status = 'COMPLETED'", nativeQuery = true)
    Long getUniqueSupportsCountForAthlete(Long athleteId);
    
    @Query(value = "SELECT s.athlete_id, COUNT(s.id) as support_count FROM supports s WHERE s.status = 'COMPLETED' GROUP BY s.athlete_id ORDER BY support_count DESC LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopSupportedAthletes(int limit);
    
    @Query(value = "SELECT s.donor_id, SUM(s.amount) as total_amount FROM supports s WHERE s.status = 'COMPLETED' GROUP BY s.donor_id ORDER BY total_amount DESC LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopDonors(int limit);
}
