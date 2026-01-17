package com.example.demo.repositories;

import com.example.demo.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepo extends JpaRepository<Field, Long> {
    List<Field> findByFarmer_FarmerId(Long farmerId);
}
