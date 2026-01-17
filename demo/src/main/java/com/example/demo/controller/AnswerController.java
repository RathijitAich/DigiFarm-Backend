package com.example.demo.controller;

import com.example.demo.entity.Answer;
import com.example.demo.entity.Question;
import com.example.demo.entity.Expert;
import com.example.demo.repositories.AnswerRepository;
import com.example.demo.repositories.QuestionRepository;
import com.example.demo.repositories.ExpertRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/answers")
@CrossOrigin(origins = "*")
public class AnswerController {
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private ExpertRepo expertRepo;
    
    // Create a new answer
    @PostMapping
    public ResponseEntity<Answer> createAnswer(@RequestBody Answer answer) {
        try {
            // Fetch the Question entity
            Long questionId = answer.getQuestionId();
            if (questionId == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            Optional<Question> questionOpt = questionRepository.findById(questionId);
            if (!questionOpt.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            // Fetch the Expert entity
            String expertEmail = answer.getExpertEmail();
            if (expertEmail == null || expertEmail.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            Optional<Expert> expertOpt = expertRepo.findByEmail(expertEmail);
            if (!expertOpt.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            // Set the entities on the Answer
            answer.setQuestion(questionOpt.get());
            answer.setExpert(expertOpt.get());
            
            Answer savedAnswer = answerRepository.save(answer);
            return new ResponseEntity<>(savedAnswer, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all answers for a specific question
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable("questionId") Long questionId) {
        try {
            List<Answer> answers = answerRepository.findByQuestionId(questionId);
            return new ResponseEntity<>(answers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all answers by a specific expert
    @GetMapping("/expert/{email}")
    public ResponseEntity<List<Answer>> getAnswersByExpertEmail(@PathVariable("email") String email) {
        try {
            List<Answer> answers = answerRepository.findByExpertEmail(email);
            return new ResponseEntity<>(answers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get answer count for a question
    @GetMapping("/question/{questionId}/count")
    public ResponseEntity<Long> getAnswerCount(@PathVariable("questionId") Long questionId) {
        try {
            Long count = answerRepository.countByQuestionId(questionId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all answers (ordered by most recent)
    @GetMapping
    public ResponseEntity<List<Answer>> getAllAnswers() {
        try {
            List<Answer> answers = answerRepository.findAllByOrderByAnsweredDateDesc();
            return new ResponseEntity<>(answers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete answer
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAnswer(@PathVariable("id") Long id) {
        try {
            answerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
