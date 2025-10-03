package com.example.resumeProject.Repository;

import com.example.resumeProject.Entity.MatchEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    List<MatchEntity> findByJdIdOrderByOverallMatchPercentageDesc(Long jdId, Pageable pageable);

    Optional<MatchEntity> findByResumeIdAndJdId(Long resumeId, Long jdId);
}

//Spring Data JPA handles CRUD operations automatically
//you can save a new match just by calling matchRepository.save(match)
