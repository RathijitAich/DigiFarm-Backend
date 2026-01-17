package com.example.demo.controller;

import com.example.demo.entity.Feedback;
import com.example.demo.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Add feedback
    @PostMapping
    public ResponseEntity<?> addFeedback(@RequestBody Feedback feedback) {
        try {
            Long articleId = feedback.getArticle().getId();
            String email = feedback.getReviewerEmail();

            // Check if feedback already exists
            if (feedbackRepository.existsByArticle_IdAndReviewerEmail(articleId, email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("You already submitted feedback for this article");
            }

            Feedback savedFeedback = feedbackRepository.save(feedback);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedback);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save feedback");
        }
    }

    // Get all feedback for an article
    @GetMapping("/article/{articleId}")
    public ResponseEntity<?> getFeedbackByArticle(@PathVariable Long articleId) {
        try {
            List<Feedback> feedbackList = feedbackRepository.findByArticle_Id(articleId);
            return ResponseEntity.ok(feedbackList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch feedback");
        }
    }

    // Get feedback counts for an article
    @GetMapping("/article/{articleId}/counts")
    public ResponseEntity<?> getFeedbackCounts(@PathVariable Long articleId) {
        try {
            Map<String, Long> counts = new HashMap<>();
            counts.put("LIKE", feedbackRepository.countByArticle_IdAndFeedbackType(articleId, "LIKE"));
            counts.put("HELPFUL", feedbackRepository.countByArticle_IdAndFeedbackType(articleId, "HELPFUL"));
            counts.put("INFORMATIVE", feedbackRepository.countByArticle_IdAndFeedbackType(articleId, "INFORMATIVE"));

            return ResponseEntity.ok(counts);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch feedback counts");
        }
    }

    // Check if user has given feedback
    @GetMapping("/article/{articleId}/user/{email}")
    public ResponseEntity<?> hasUserGivenFeedback(
            @PathVariable Long articleId,
            @PathVariable String email) {

        try {
            boolean hasFeedback = feedbackRepository.existsByArticle_IdAndReviewerEmail(articleId, email);
            return ResponseEntity.ok(hasFeedback);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to check feedback status");
        }
    }

    // Get all feedback by reviewer
    @GetMapping("/reviewer/{email}")
    public ResponseEntity<?> getFeedbackByReviewer(@PathVariable String email) {
        try {
            List<Feedback> feedbackList = feedbackRepository.findByReviewerEmail(email);
            return ResponseEntity.ok(feedbackList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch reviewer feedback");
        }
    }

    // Delete feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        try {
            if (!feedbackRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Feedback not found");
            }

            feedbackRepository.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete feedback");
        }
    }
}
