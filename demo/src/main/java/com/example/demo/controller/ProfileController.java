package com.example.demo.controller;

import com.example.demo.entity.Farmer;
import com.example.demo.entity.Field;
import com.example.demo.repositories.FarmerRepo;
import com.example.demo.repositories.FieldRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    private final FieldRepo fieldRepo;
    private final FarmerRepo farmerRepo;

    public ProfileController(FieldRepo fieldRepo, FarmerRepo farmerRepo) {
        this.fieldRepo = fieldRepo;
        this.farmerRepo = farmerRepo;
    }

    @PostMapping("/field/{farmerId}")
    public ResponseEntity<?> addField(
            @PathVariable Long farmerId,
            @RequestBody Field field
    ) {
        Farmer farmer = farmerRepo.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
        field.setFarmer(farmer);
        Field savedField = fieldRepo.save(field);
        
        // Return DTO instead of full entity to avoid serialization issues
        Map<String, Object> response = new HashMap<>();
        response.put("fieldId", savedField.getFieldId());
        response.put("fieldName", savedField.getFieldName());
        response.put("area", savedField.getArea());
        response.put("crop", savedField.getCrop());
        response.put("message", "Field added successfully");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fields/{farmerId}")
    public List<Map<String, Object>> getFields(@PathVariable Long farmerId) {
        List<Field> fields = fieldRepo.findByFarmer_FarmerId(farmerId);
        return fields.stream()
                .map(field -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("fieldId", field.getFieldId());
                    dto.put("fieldName", field.getFieldName());
                    dto.put("area", field.getArea());
                    dto.put("crop", field.getCrop());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
