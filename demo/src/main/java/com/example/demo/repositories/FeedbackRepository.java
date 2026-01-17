package com.example.demo.repositories;

import com.example.demo.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Find all feedback for an article
    List<Feedback> findByArticle_Id(Long articleId);

    // Find feedback by article and reviewer email
    Optional<Feedback> findByArticle_IdAndReviewerEmail(Long articleId, String reviewerEmail);

    // Find all feedback by reviewer email
    List<Feedback> findByReviewerEmail(String reviewerEmail);

    // Count feedback by article ID and feedback type
    long countByArticle_IdAndFeedbackType(Long articleId, String feedbackType);

    // Check if feedback exists for article and reviewer
    boolean existsByArticle_IdAndReviewerEmail(Long articleId, String reviewerEmail);
}
