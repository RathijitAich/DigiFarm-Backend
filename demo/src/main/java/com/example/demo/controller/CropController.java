package com.example.demo.controller;

import com.example.demo.entity.Crop;
import com.example.demo.entity.Field;
import com.example.demo.entity.Farmer;
import com.example.demo.repositories.CropRepo;
import com.example.demo.repositories.FieldRepo;
import com.example.demo.repositories.FarmerRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "http://localhost:3000")
public class CropController {

    private final CropRepo cropRepo;
    private final FieldRepo fieldRepo;
    private final FarmerRepo farmerRepo;

    public CropController(CropRepo cropRepo, FieldRepo fieldRepo, FarmerRepo farmerRepo) {
        this.cropRepo = cropRepo;
        this.fieldRepo = fieldRepo;
        this.farmerRepo = farmerRepo;
    }

    // Get all crops for a farmer
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Map<String, Object>>> getCropsByFarmer(@PathVariable Long farmerId) {
        try {
            List<Crop> crops = cropRepo.findByFarmer_FarmerId(farmerId);
            
            List<Map<String, Object>> response = crops.stream().map(crop -> {
                Map<String, Object> cropData = new HashMap<>();
                cropData.put("cropId", crop.getCropId());
                cropData.put("cropName", crop.getCropName());
                cropData.put("cropType", crop.getCropType());
                cropData.put("fieldName", crop.getField().getFieldName());
                cropData.put("fieldId", crop.getField().getFieldId());
                cropData.put("area", crop.getArea());
                cropData.put("stage", crop.getStage());
                cropData.put("health", crop.getHealth());
                cropData.put("plantedDate", crop.getPlantedDate());
                cropData.put("expectedHarvestDate", crop.getExpectedHarvestDate());
                cropData.put("daysToHarvest", ChronoUnit.DAYS.between(LocalDate.now(), crop.getExpectedHarvestDate()));
                cropData.put("expectedYield", crop.getExpectedYield());
                cropData.put("notes", crop.getNotes());
                return cropData;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

    // Get single crop details
    @GetMapping("/{cropId}")
    public ResponseEntity<Map<String, Object>> getCropDetails(@PathVariable Long cropId) {
        try {
            Optional<Crop> crop = cropRepo.findById(cropId);
            if (crop.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Crop not found"));
            }

            Crop c = crop.get();
            Map<String, Object> cropData = new HashMap<>();
            cropData.put("cropId", c.getCropId());
            cropData.put("cropName", c.getCropName());
            cropData.put("cropType", c.getCropType());
            cropData.put("fieldName", c.getField().getFieldName());
            cropData.put("fieldId", c.getField().getFieldId());
            cropData.put("area", c.getArea());
            cropData.put("stage", c.getStage());
            cropData.put("health", c.getHealth());
            cropData.put("plantedDate", c.getPlantedDate());
            cropData.put("expectedHarvestDate", c.getExpectedHarvestDate());
            cropData.put("daysToHarvest", ChronoUnit.DAYS.between(LocalDate.now(), c.getExpectedHarvestDate()));
            cropData.put("expectedYield", c.getExpectedYield());
            cropData.put("notes", c.getNotes());

            return ResponseEntity.ok(cropData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch crop"));
        }
    }

    // Create a new crop
    @PostMapping("/{farmerId}")
    public ResponseEntity<Map<String, Object>> createCrop(
            @PathVariable Long farmerId,
            @RequestBody Map<String, Object> requestBody) {
        try {
            Optional<Farmer> farmer = farmerRepo.findById(farmerId);
            if (farmer.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Farmer not found"));
            }

            Long fieldId = Long.parseLong(requestBody.get("fieldId").toString());
            Optional<Field> field = fieldRepo.findById(fieldId);
            if (field.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Field not found"));
            }

            Crop crop = new Crop();
            crop.setCropName((String) requestBody.get("cropName"));
            crop.setCropType((String) requestBody.get("cropType"));
            crop.setField(field.get());
            crop.setFarmer(farmer.get());
            crop.setPlantedDate(LocalDate.parse((String) requestBody.get("plantedDate")));
            crop.setExpectedHarvestDate(LocalDate.parse((String) requestBody.get("expectedHarvestDate")));
            crop.setArea(Double.parseDouble(requestBody.get("area").toString()));
            crop.setStage((String) requestBody.getOrDefault("stage", "Germination"));
            crop.setHealth((String) requestBody.getOrDefault("health", "Good"));
            crop.setExpectedYield(Double.parseDouble(requestBody.getOrDefault("expectedYield", "5.0").toString()));
            crop.setNotes((String) requestBody.getOrDefault("notes", ""));

            Crop savedCrop = cropRepo.save(crop);

            Map<String, Object> response = new HashMap<>();
            response.put("cropId", savedCrop.getCropId());
            response.put("message", "Crop created successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Failed to create crop: " + e.getMessage()));
        }
    }

    // Update crop
    @PutMapping("/{cropId}")
    public ResponseEntity<Map<String, Object>> updateCrop(
            @PathVariable Long cropId,
            @RequestBody Map<String, Object> requestBody) {
        try {
            Optional<Crop> crop = cropRepo.findById(cropId);
            if (crop.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Crop not found"));
            }

            Crop c = crop.get();
            if (requestBody.containsKey("cropName")) {
                c.setCropName((String) requestBody.get("cropName"));
            }
            if (requestBody.containsKey("stage")) {
                c.setStage((String) requestBody.get("stage"));
            }
            if (requestBody.containsKey("health")) {
                c.setHealth((String) requestBody.get("health"));
            }
            if (requestBody.containsKey("expectedHarvestDate")) {
                c.setExpectedHarvestDate(LocalDate.parse((String) requestBody.get("expectedHarvestDate")));
            }
            if (requestBody.containsKey("expectedYield")) {
                c.setExpectedYield(Double.parseDouble(requestBody.get("expectedYield").toString()));
            }
            if (requestBody.containsKey("notes")) {
                c.setNotes((String) requestBody.get("notes"));
            }

            cropRepo.save(c);

            return ResponseEntity.ok(Map.of("message", "Crop updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Failed to update crop: " + e.getMessage()));
        }
    }

    // Delete crop
    @DeleteMapping("/{cropId}")
    public ResponseEntity<Map<String, Object>> deleteCrop(@PathVariable Long cropId) {
        try {
            Optional<Crop> crop = cropRepo.findById(cropId);
            if (crop.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Crop not found"));
            }

            cropRepo.deleteById(cropId);
            return ResponseEntity.ok(Map.of("message", "Crop deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Failed to delete crop: " + e.getMessage()));
        }
    }

    // Get crop monitoring dashboard
    @GetMapping("/dashboard/{farmerId}")
    public ResponseEntity<Map<String, Object>> getCropDashboard(@PathVariable Long farmerId) {
        try {
            List<Crop> crops = cropRepo.findByFarmer_FarmerId(farmerId);

            int totalCrops = crops.size();
            long excellentHealth = crops.stream().filter(c -> "Excellent".equals(c.getHealth())).count();
            long goodHealth = crops.stream().filter(c -> "Good".equals(c.getHealth())).count();
            
            // Calculate days to next harvest
            LocalDate nextHarvest = crops.stream()
                    .map(Crop::getExpectedHarvestDate)
                    .min(LocalDate::compareTo)
                    .orElse(LocalDate.now());
            
            long daysToNextHarvest = ChronoUnit.DAYS.between(LocalDate.now(), nextHarvest);

            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("totalCrops", totalCrops);
            dashboard.put("excellentHealth", excellentHealth);
            dashboard.put("goodHealth", goodHealth);
            dashboard.put("daysToNextHarvest", Math.max(0, daysToNextHarvest));

            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch dashboard"));
        }
    }
}
