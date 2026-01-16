package com.example.demo.repositories;

import com.example.demo.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepo extends JpaRepository<Farmer, Long> {
    Optional<Farmer> findByEmail(String email);
}
