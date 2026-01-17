package com.example.demo.repositories;

import com.example.demo.entity.Answer;
import com.example.demo.entity.AnswerVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerVoteRepository extends JpaRepository<AnswerVote, Long> {
    
    // Count total upvotes for an answer
    Long countByAnswer(Answer answer);
    
    // Check if a specific user has already upvoted an answer
    boolean existsByAnswerAndVoterEmail(Answer answer, String voterEmail);
    
    // Get all votes for a specific answer
    List<AnswerVote> findByAnswer(Answer answer);
    
    // Find a specific vote by answer and voter email (for deletion)
    Optional<AnswerVote> findByAnswerAndVoterEmail(Answer answer, String voterEmail);
    
    // Delete a specific vote (for un-upvoting)
    void deleteByAnswerAndVoterEmail(Answer answer, String voterEmail);
}
