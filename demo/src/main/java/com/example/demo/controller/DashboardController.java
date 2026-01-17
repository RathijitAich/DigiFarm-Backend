package com.example.demo.controller;

import com.example.demo.entity.Field;
import com.example.demo.repositories.FieldRepo;
import com.example.demo.repositories.TaskRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    private final FieldRepo fieldRepo;
    private final TaskRepo taskRepo;

    public DashboardController(FieldRepo fieldRepo, TaskRepo taskRepo) {
        this.fieldRepo = fieldRepo;
        this.taskRepo = taskRepo;
    }

    @GetMapping("/{farmerId}")
    public Map<String, Object> getDashboard(@PathVariable Long farmerId) {
        try {
            List<Field> fields = fieldRepo.findByFarmer_FarmerId(farmerId);
            int totalFields = fields.size();

            long totalTasks = fields.stream()
                    .flatMap(f -> taskRepo.findByField_FieldId(f.getFieldId()).stream())
                    .count();

            return Map.of(
                    "totalFields", totalFields,
                    "totalTasks", totalTasks,
                    "activeCrops", fields.stream().map(Field::getCrop).distinct().count()
            );
        } catch (Exception e) {
            return Map.of(
                    "totalFields", 0,
                    "totalTasks", 0,
                    "activeCrops", 0,
                    "error", "Failed to load dashboard: " + e.getMessage()
            );
        }
    }
}
