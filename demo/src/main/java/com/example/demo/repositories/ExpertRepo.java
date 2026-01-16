package com.example.demo.repositories;

import com.example.demo.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpertRepo extends JpaRepository<Expert, Long> {
    Optional<Expert> findByEmail(String email);
}
