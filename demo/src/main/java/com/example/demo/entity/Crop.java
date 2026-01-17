package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cropId;

    @Column(nullable = false)
    private String cropName;

    @Column(nullable = false)
    private String cropType; // e.g., "Wheat", "Corn", "Rice"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Farmer farmer;

    @Column(nullable = false)
    private LocalDate plantedDate;

    @Column(nullable = false)
    private LocalDate expectedHarvestDate;

    @Column(nullable = false)
    private Double area; // acres

    @Column(nullable = false)
    private String stage; // e.g., "Germination", "Vegetative Growth", "Flowering", "Fruiting"

    @Column(nullable = false)
    private String health; // "Excellent", "Good", "Fair", "Poor"

    @Column(nullable = false)
    private Double expectedYield; // tons/acre

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate createdDate = LocalDate.now();

    // ---------- CONSTRUCTORS ----------

    public Crop() {
    }

    public Crop(String cropName, String cropType, Field field, Farmer farmer, LocalDate plantedDate, 
                LocalDate expectedHarvestDate, Double area, String stage, String health, Double expectedYield) {
        this.cropName = cropName;
        this.cropType = cropType;
        this.field = field;
        this.farmer = farmer;
        this.plantedDate = plantedDate;
        this.expectedHarvestDate = expectedHarvestDate;
        this.area = area;
        this.stage = stage;
        this.health = health;
        this.expectedYield = expectedYield;
    }

    // ---------- GETTERS ----------

    public Long getCropId() {
        return cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public String getCropType() {
        return cropType;
    }

    public Field getField() {
        return field;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public LocalDate getPlantedDate() {
        return plantedDate;
    }

    public LocalDate getExpectedHarvestDate() {
        return expectedHarvestDate;
    }

    public Double getArea() {
        return area;
    }

    public String getStage() {
        return stage;
    }

    public String getHealth() {
        return health;
    }

    public Double getExpectedYield() {
        return expectedYield;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    // ---------- SETTERS ----------

    public void setCropId(Long cropId) {
        this.cropId = cropId;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public void setPlantedDate(LocalDate plantedDate) {
        this.plantedDate = plantedDate;
    }

    public void setExpectedHarvestDate(LocalDate expectedHarvestDate) {
        this.expectedHarvestDate = expectedHarvestDate;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public void setExpectedYield(Double expectedYield) {
        this.expectedYield = expectedYield;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
