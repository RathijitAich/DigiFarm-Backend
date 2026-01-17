package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false)
    private String taskType;   // Irrigation, Fertilization, etc

    @Column(nullable = false)
    private String status;     // PENDING / COMPLETED

    @Column(nullable = false)
    private String dueDate;    // yyyy-MM-dd

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "farmer"})
    private Field field;

    // ---------- GETTERS ----------

    public Long getTaskId() {
        return taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getStatus() {
        return status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public Field getField() {
        return field;
    }

    // ---------- SETTERS ----------

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
