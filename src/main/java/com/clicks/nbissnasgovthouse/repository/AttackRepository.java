package com.clicks.nbissnasgovthouse.repository;

import com.clicks.nbissnasgovthouse.model.Attack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttackRepository extends JpaRepository<Attack, Long> {

    List<Attack> findByTimestampIsBetween(LocalDateTime start, LocalDateTime end);
    List<Attack> findByTimestampBefore(LocalDateTime end);

    List<Attack> findByThreat_Name(String threatName);
}
