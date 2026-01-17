package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @ManyToOne
    @JoinColumn(name = "farmer_email", referencedColumnName = "email", nullable = false,
                foreignKey = @ForeignKey(name = "fk_question_farmer"))
    private Farmer farmer;
    
    @Column(name = "farmer_email", nullable = false, insertable = false, updatable = false)
    private String farmerEmail;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime askedDate;
    
    // Constructor
    public Question() {
        this.askedDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Farmer getFarmer() {
        return farmer;
    }
    
    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }
    
    public String getFarmerEmail() {
        return farmerEmail;
    }
    
    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }
    
    public LocalDateTime getAskedDate() {
        return askedDate;
    }
    
    public void setAskedDate(LocalDateTime askedDate) {
        this.askedDate = askedDate;
    }
}
