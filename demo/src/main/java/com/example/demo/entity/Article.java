package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @ManyToOne // this means many articles can be written by one expert
    @JoinColumn(name = "expertEmail", referencedColumnName = "email", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_article_expert"))
    private Expert expert;
    
    @Column(name = "expertEmail", nullable = false, insertable = false, updatable = false)
    private String expertEmail;
    
    @Column(nullable = false, length = 100)
    private String category;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    // Constructors
    public Article() {
        this.createdDate = LocalDateTime.now();
    }
    
    public Article(String title, String content, String expertEmail, String category) {
        this();
        this.title = title;
        this.content = content;
        this.expertEmail = expertEmail;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Expert getExpert() {
        return expert;
    }
    
    public void setExpert(Expert expert) {
        this.expert = expert;
    }
    
    public String getExpertEmail() {
        return expertEmail;
    }
    
    public void setExpertEmail(String expertEmail) {
        this.expertEmail = expertEmail;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
