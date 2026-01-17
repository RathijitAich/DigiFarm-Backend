package com.example.demo.controller;

import com.example.demo.entity.Answer;
import com.example.demo.entity.AnswerVote;
import com.example.demo.repositories.AnswerRepository;
import com.example.demo.repositories.AnswerVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/answer-votes")
@CrossOrigin(origins = "http://localhost:3000")
public class AnswerVoteController {
    
    @Autowired
    private AnswerVoteRepository answerVoteRepository;
    
    @Autowired
    private AnswerRepository answerRepository;
    
    // Add an upvote
    @PostMapping
    public ResponseEntity<?> addUpvote(@RequestBody Map<String, String> request) {
        try {
            Long answerId = Long.parseLong(request.get("answerId"));
            String voterEmail = request.get("voterEmail");
            String voterType = request.get("voterType");
            
            // Fetch the Answer entity
            Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));
            
            // Check if user has already upvoted
            if (answerVoteRepository.existsByAnswerAndVoterEmail(answer, voterEmail)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "You have already upvoted this answer"));
            }
            
            // Create and save the vote
            AnswerVote vote = new AnswerVote(answer, voterEmail, voterType);
            answerVoteRepository.save(vote);
            
            // Return the new vote count
            Long voteCount = answerVoteRepository.countByAnswer(answer);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Upvote added successfully");
            response.put("voteCount", voteCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error adding upvote: " + e.getMessage()));
        }
    }
    
    // Remove an upvote
    @DeleteMapping("/{answerId}/{voterEmail}")
    @Transactional
    public ResponseEntity<?> removeUpvote(@PathVariable Long answerId, @PathVariable String voterEmail) {
        try {
            // Fetch the Answer entity
            Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));
            
            // Check if vote exists
            if (!answerVoteRepository.existsByAnswerAndVoterEmail(answer, voterEmail)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Vote not found"));
            }
            
            // Delete the vote
            answerVoteRepository.deleteByAnswerAndVoterEmail(answer, voterEmail);
            
            // Return the new vote count
            Long voteCount = answerVoteRepository.countByAnswer(answer);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Upvote removed successfully");
            response.put("voteCount", voteCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error removing upvote: " + e.getMessage()));
        }
    }
    
    // Get upvote count for an answer
    @GetMapping("/answer/{answerId}/count")
    public ResponseEntity<?> getUpvoteCount(@PathVariable Long answerId) {
        try {
            Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));
            
            Long voteCount = answerVoteRepository.countByAnswer(answer);
            return ResponseEntity.ok(Map.of("voteCount", voteCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error getting vote count: " + e.getMessage()));
        }
    }
    
    // Check if user has upvoted an answer
    @GetMapping("/answer/{answerId}/check/{voterEmail}")
    public ResponseEntity<?> checkUserVote(@PathVariable Long answerId, @PathVariable String voterEmail) {
        try {
            Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));
            
            boolean hasVoted = answerVoteRepository.existsByAnswerAndVoterEmail(answer, voterEmail);
            return ResponseEntity.ok(Map.of("hasVoted", hasVoted));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error checking vote status: " + e.getMessage()));
        }
    }
}
