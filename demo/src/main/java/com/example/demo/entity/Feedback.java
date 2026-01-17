package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_feedback_article"))
    private Article article;

    @Column(name = "feedback_type", nullable = false, length = 50)
    private String feedbackType;

    @Column(name = "reviewer_email", nullable = false)
    private String reviewerEmail;

    @Column(name = "reviewer_type", nullable = false, length = 20)
    private String reviewerType;

    public Feedback() {}

    public Feedback(Article article, String feedbackType, String reviewerEmail, String reviewerType) {
        this.article = article;
        this.feedbackType = feedbackType;
        this.reviewerEmail = reviewerEmail;
        this.reviewerType = reviewerType;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public String getReviewerType() {
        return reviewerType;
    }

    public void setReviewerType(String reviewerType) {
        this.reviewerType = reviewerType;
    }
}

