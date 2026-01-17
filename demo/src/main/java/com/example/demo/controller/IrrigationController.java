package com.example.demo.controller;

import com.example.demo.entity.Field;
import com.example.demo.entity.IrrigationSchedule;
import com.example.demo.repositories.FieldRepo;
import com.example.demo.repositories.IrrigationScheduleRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/irrigation")
@CrossOrigin(origins = "http://localhost:3000")
public class IrrigationController {

    private final IrrigationScheduleRepo scheduleRepo;
    private final FieldRepo fieldRepo;

    public IrrigationController(IrrigationScheduleRepo scheduleRepo, FieldRepo fieldRepo) {
        this.scheduleRepo = scheduleRepo;
        this.fieldRepo = fieldRepo;
    }

    @GetMapping("/dashboard/{farmerId}")
    public Map<String, Object> getIrrigationDashboard(@PathVariable Long farmerId) {
        try {
            List<IrrigationSchedule> schedules = scheduleRepo.findByField_Farmer_FarmerId(farmerId);
            
            long activeZones = schedules.stream().filter(IrrigationSchedule::getActive).count();
            
            // Calculate total water used (simplified calculation)
            int totalWaterUsed = schedules.stream()
                    .filter(s -> s.getLastWatered() != null)
                    .mapToInt(s -> s.getDurationMinutes() * 50) // 50L per minute
                    .sum();

            // Calculate average soil moisture
            double avgMoisture = schedules.stream()
                    .mapToInt(IrrigationSchedule::getSoilMoisture)
                    .average()
                    .orElse(0.0);

            return Map.of(
                    "waterUsedThisMonth", totalWaterUsed,
                    "systemEfficiency", 95,
                    "waterSaved", 15,
                    "activeZones", activeZones,
                    "avgSoilMoisture", Math.round(avgMoisture)
            );
        } catch (Exception e) {
            return Map.of(
                    "waterUsedThisMonth", 0,
                    "systemEfficiency", 0,
                    "waterSaved", 0,
                    "activeZones", 0,
                    "error", "Failed to load irrigation dashboard: " + e.getMessage()
            );
        }
    }

    @GetMapping("/schedules/{farmerId}")
    public List<Map<String, Object>> getSchedules(@PathVariable Long farmerId) {
        List<IrrigationSchedule> schedules = scheduleRepo.findByField_Farmer_FarmerId(farmerId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a");
        
        return schedules.stream()
                .map(schedule -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("scheduleId", schedule.getScheduleId());
                    dto.put("fieldId", schedule.getField().getFieldId());
                    dto.put("fieldName", schedule.getField().getFieldName());
                    dto.put("crop", schedule.getField().getCrop());
                    dto.put("lastWatered", schedule.getLastWatered() != null ? 
                            schedule.getLastWatered().format(formatter) : "Never");
                    dto.put("nextScheduled", schedule.getNextScheduled() != null ? 
                            schedule.getNextScheduled().format(formatter) : "-");
                    dto.put("duration", schedule.getDurationMinutes());
                    dto.put("active", schedule.getActive());
                    dto.put("frequency", schedule.getFrequency());
                    dto.put("startTime", schedule.getStartTime());
                    dto.put("skipIfRain", schedule.getSkipIfRain());
                    dto.put("soilMoisture", schedule.getSoilMoisture());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/schedule/{farmerId}")
    public ResponseEntity<?> createSchedule(
            @PathVariable Long farmerId,
            @RequestBody Map<String, Object> requestData
    ) {
        try {
            Long fieldId = Long.valueOf(requestData.get("fieldId").toString());
            Field field = fieldRepo.findById(fieldId)
                    .orElseThrow(() -> new RuntimeException("Field not found"));

            if (!field.getFarmer().getFarmerId().equals(farmerId)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Field does not belong to this farmer"));
            }

            IrrigationSchedule schedule = new IrrigationSchedule();
            schedule.setField(field);
            schedule.setStartTime(requestData.get("startTime").toString());
            schedule.setDurationMinutes(Integer.valueOf(requestData.get("durationMinutes").toString()));
            schedule.setFrequency(requestData.get("frequency").toString());
            schedule.setSkipIfRain(Boolean.valueOf(requestData.get("skipIfRain").toString()));
            schedule.setActive(true);
            schedule.setSoilMoisture(70); // Default value

            // Calculate next scheduled time
            schedule.setNextScheduled(calculateNextScheduled(schedule.getStartTime(), schedule.getFrequency()));

            IrrigationSchedule saved = scheduleRepo.save(schedule);

            Map<String, Object> response = new HashMap<>();
            response.put("scheduleId", saved.getScheduleId());
            response.put("message", "Schedule created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/schedule/{scheduleId}")
    public ResponseEntity<?> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody Map<String, Object> requestData
    ) {
        try {
            IrrigationSchedule schedule = scheduleRepo.findById(scheduleId)
                    .orElseThrow(() -> new RuntimeException("Schedule not found"));

            if (requestData.containsKey("startTime")) {
                schedule.setStartTime(requestData.get("startTime").toString());
            }
            if (requestData.containsKey("durationMinutes")) {
                schedule.setDurationMinutes(Integer.valueOf(requestData.get("durationMinutes").toString()));
            }
            if (requestData.containsKey("frequency")) {
                schedule.setFrequency(requestData.get("frequency").toString());
            }
            if (requestData.containsKey("skipIfRain")) {
                schedule.setSkipIfRain(Boolean.valueOf(requestData.get("skipIfRain").toString()));
            }
            if (requestData.containsKey("active")) {
                schedule.setActive(Boolean.valueOf(requestData.get("active").toString()));
            }

            // Recalculate next scheduled time
            schedule.setNextScheduled(calculateNextScheduled(schedule.getStartTime(), schedule.getFrequency()));

            scheduleRepo.save(schedule);
            return ResponseEntity.ok(Map.of("message", "Schedule updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/water-now/{scheduleId}")
    public ResponseEntity<?> waterNow(@PathVariable Long scheduleId) {
        try {
            IrrigationSchedule schedule = scheduleRepo.findById(scheduleId)
                    .orElseThrow(() -> new RuntimeException("Schedule not found"));

            schedule.setLastWatered(LocalDateTime.now());
            schedule.setNextScheduled(calculateNextScheduled(schedule.getStartTime(), schedule.getFrequency()));
            scheduleRepo.save(schedule);

            return ResponseEntity.ok(Map.of("message", "Irrigation started for " + schedule.getField().getFieldName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/schedule/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        try {
            scheduleRepo.deleteById(scheduleId);
            return ResponseEntity.ok(Map.of("message", "Schedule deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private LocalDateTime calculateNextScheduled(String startTime, String frequency) {
        LocalDateTime now = LocalDateTime.now();
        String[] timeParts = startTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        LocalDateTime next = now.withHour(hour).withMinute(minute).withSecond(0);
        
        if (next.isBefore(now)) {
            next = next.plusDays(1);
        }

        switch (frequency.toLowerCase()) {
            case "every 2 days":
                next = next.plusDays(1);
                break;
            case "every 3 days":
                next = next.plusDays(2);
                break;
            case "weekly":
                next = next.plusDays(6);
                break;
            default: // "daily"
                break;
        }

        return next;
    }
}
