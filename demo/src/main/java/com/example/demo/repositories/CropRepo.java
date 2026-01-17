package com.example.demo.repositories;

import com.example.demo.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRepo extends JpaRepository<Crop, Long> {
    List<Crop> findByFarmer_FarmerId(Long farmerId);
    List<Crop> findByField_FieldId(Long fieldId);
    List<Crop> findByFarmer_FarmerIdOrderByExpectedHarvestDateAsc(Long farmerId);
}
