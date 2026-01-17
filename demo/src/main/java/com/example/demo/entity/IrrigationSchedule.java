package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "irrigation_schedules")
public class IrrigationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @Column(nullable = false)
    private String startTime; // e.g., "06:00"

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private String frequency; // Daily, Every 2 days, etc.

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean skipIfRain = true;

    private LocalDateTime lastWatered;

    private LocalDateTime nextScheduled;

    @Column(nullable = false)
    private Integer soilMoisture = 0; // 0-100%

    // ---------- GETTERS ----------

    public Long getScheduleId() {
        return scheduleId;
    }

    public Field getField() {
        return field;
    }

    public String getStartTime() {
        return startTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public String getFrequency() {
        return frequency;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getSkipIfRain() {
        return skipIfRain;
    }

    public LocalDateTime getLastWatered() {
        return lastWatered;
    }

    public LocalDateTime getNextScheduled() {
        return nextScheduled;
    }

    public Integer getSoilMoisture() {
        return soilMoisture;
    }

    // ---------- SETTERS ----------

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setSkipIfRain(Boolean skipIfRain) {
        this.skipIfRain = skipIfRain;
    }

    public void setLastWatered(LocalDateTime lastWatered) {
        this.lastWatered = lastWatered;
    }

    public void setNextScheduled(LocalDateTime nextScheduled) {
        this.nextScheduled = nextScheduled;
    }

    public void setSoilMoisture(Integer soilMoisture) {
        this.soilMoisture = soilMoisture;
    }
}
