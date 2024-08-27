package br.balchaki.meetspace.domain.Reserve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    @Query("SELECT r FROM Reserve r WHERE r.userId = :userId")
    Optional<List<Reserve>> findByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Reserve r WHERE r.roomId = :roomId AND " +
            "((r.startDate <= :endTime AND r.endDate >= :startTime) OR " +
            "(r.startDate >= :startTime AND r.startDate < :endTime))")
    List<Reserve> findOverlappingReservations(@Param("roomId") Long roomId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);


}
