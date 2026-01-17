package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
public class Answer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;
    
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "question_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_answer_question"))
    private Question question;
    
    @Column(name = "question_id", nullable = false, insertable = false, updatable = false)
    private Long questionId;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answerText;
    
    @ManyToOne
    @JoinColumn(name = "expert_email", referencedColumnName = "email", nullable = false,
                foreignKey = @ForeignKey(name = "fk_answer_expert"))
    private Expert expert;
    
    @Column(name = "expert_email", nullable = false, insertable = false, updatable = false)
    private String expertEmail;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime answeredDate;
    
    // Constructor
    public Answer() {
        this.answeredDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getAnswerId() {
        return answerId;
    }
    
    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }
    
    public Question getQuestion() {
        return question;
    }
    
    public void setQuestion(Question question) {
        this.question = question;
    }
    
    public Long getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    
    public String getAnswerText() {
        return answerText;
    }
    
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
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
    
    public LocalDateTime getAnsweredDate() {
        return answeredDate;
    }
    
    public void setAnsweredDate(LocalDateTime answeredDate) {
        this.answeredDate = answeredDate;
    }
}
