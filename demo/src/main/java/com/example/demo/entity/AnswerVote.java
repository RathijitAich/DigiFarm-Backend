package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "answer_votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"answer_id", "voter_email"})
})
public class AnswerVote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;
    
    @ManyToOne
    @JoinColumn(name = "answer_id", referencedColumnName = "answer_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_vote_answer"))
    private Answer answer;
    
    @Column(name = "answer_id", nullable = false, insertable = false, updatable = false)
    private Long answerId;
    
    @Column(name = "voter_email", nullable = false)
    private String voterEmail;
    
    @Column(name = "voter_type", nullable = false)
    private String voterType; // "FARMER" or "EXPERT"
    
    @Column(name = "voted_date", nullable = false)
    private LocalDateTime votedDate;
    
    @PrePersist
    protected void onCreate() {
        votedDate = LocalDateTime.now();
    }
    
    // Constructors
    public AnswerVote() {
    }
    
    public AnswerVote(Answer answer, String voterEmail, String voterType) {
        this.answer = answer;
        this.voterEmail = voterEmail;
        this.voterType = voterType;
    }
    
    // Getters and Setters
    public Long getVoteId() {
        return voteId;
    }
    
    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }
    
    public Answer getAnswer() {
        return answer;
    }
    
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
    
    public Long getAnswerId() {
        return answerId;
    }
    
    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }
    
    public String getVoterEmail() {
        return voterEmail;
    }
    
    public void setVoterEmail(String voterEmail) {
        this.voterEmail = voterEmail;
    }
    
    public String getVoterType() {
        return voterType;
    }
    
    public void setVoterType(String voterType) {
        this.voterType = voterType;
    }
    
    public LocalDateTime getVotedDate() {
        return votedDate;
    }
    
    public void setVotedDate(LocalDateTime votedDate) {
        this.votedDate = votedDate;
    }
}
