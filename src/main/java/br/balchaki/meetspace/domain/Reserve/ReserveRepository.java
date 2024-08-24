package br.balchaki.meetspace.domain.Reserve;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    Optional<Object> findByUserId(Long userId);
}
