package br.balchaki.meetspace.domain.Reserve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    Optional<Object> findByUserId(Long userId);

    @Query("SELECT r FROM Reserve r WHERE r.roomId = :roomId AND " +
            "((r.start_date <= :endTime AND r.end_date >= :startTime) OR " +
            "(r.start_date >= :startTime AND r.start_date < :endTime))")
    List<Reserve> findOverlappingReservations(@Param("roomId") Long roomId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);
}
