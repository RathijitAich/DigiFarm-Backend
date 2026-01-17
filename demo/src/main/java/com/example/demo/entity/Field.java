package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "fields")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fieldId;

    @Column(nullable = false)
    private String fieldName;

    @Column(nullable = false)
    private Double area; // acres

    @Column(nullable = false)
    private String crop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Farmer farmer;

    // ---------- GETTERS ----------

    public Long getFieldId() {
        return fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Double getArea() {
        return area;
    }

    public String getCrop() {
        return crop;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    // ---------- SETTERS ----------

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }
}
