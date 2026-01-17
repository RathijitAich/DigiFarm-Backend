package com.example.demo.controller;

import com.example.demo.entity.Question;
import com.example.demo.entity.Farmer;
import com.example.demo.repositories.QuestionRepository;
import com.example.demo.repositories.FarmerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private FarmerRepo farmerRepo;
    
    // Create a new question
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        try {
            // Fetch the Farmer entity from database using farmerEmail
            String farmerEmail = question.getFarmerEmail();
            if (farmerEmail == null || farmerEmail.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            Optional<Farmer> farmerOpt = farmerRepo.findByEmail(farmerEmail);
            if (!farmerOpt.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            // Set the Farmer entity on the Question
            question.setFarmer(farmerOpt.get());
            
            Question savedQuestion = questionRepository.save(question);
            return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all questions (ordered by most recent)
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            List<Question> questions = questionRepository.findAllByOrderByAskedDateDesc();
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get question by ID
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") Long id) {
        Optional<Question> question = questionRepository.findById(id);
        return question.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Get questions by farmer email
    @GetMapping("/farmer/{email}")
    public ResponseEntity<List<Question>> getQuestionsByFarmerEmail(@PathVariable("email") String email) {
        try {
            List<Question> questions = questionRepository.findByFarmerEmail(email);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete question
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteQuestion(@PathVariable("id") Long id) {
        try {
            questionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
