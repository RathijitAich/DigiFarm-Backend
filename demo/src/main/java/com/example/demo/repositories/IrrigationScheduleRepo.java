package com.example.demo.repositories;

import com.example.demo.entity.IrrigationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IrrigationScheduleRepo extends JpaRepository<IrrigationSchedule, Long> {
    List<IrrigationSchedule> findByField_Farmer_FarmerId(Long farmerId);
    List<IrrigationSchedule> findByField_FieldId(Long fieldId);
}
