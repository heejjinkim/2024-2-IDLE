package com.example.__2_IDLE.task.repository;

import com.example.__2_IDLE.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Item, Long> {

}
