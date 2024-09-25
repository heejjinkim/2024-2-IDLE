package com.example.__2_IDLE.task.repository;

import com.example.__2_IDLE.task.entity.Station;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
  Optional<Station> findByName(String name);
}