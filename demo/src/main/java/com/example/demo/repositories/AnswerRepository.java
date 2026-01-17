package com.example.demo.repositories;

import com.example.demo.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    List<Answer> findByQuestionId(Long questionId);
    
    List<Answer> findByExpertEmail(String expertEmail);
    
    Long countByQuestionId(Long questionId);
    
    List<Answer> findAllByOrderByAnsweredDateDesc();
}
