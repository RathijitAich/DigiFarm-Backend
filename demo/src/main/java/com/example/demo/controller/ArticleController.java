package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.entity.Expert;
import com.example.demo.repositories.ArticleRepository;
import com.example.demo.repositories.ExpertRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*")
public class ArticleController {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private ExpertRepo expertRepo;
    
    // Create a new article
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        try {
            // Fetch the Expert entity from database using expertEmail
            String expertEmail = article.getExpertEmail();
            if (expertEmail == null || expertEmail.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            Optional<Expert> expertOpt = expertRepo.findByEmail(expertEmail);
            if (!expertOpt.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            // Set the Expert entity on the Article
            article.setExpert(expertOpt.get());
            
            Article savedArticle = articleRepository.save(article);
            return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all articles
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        try {
            List<Article> articles = articleRepository.findAllByOrderByCreatedDateDesc();
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get article by ID
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable("id") Long id) {
        Optional<Article> article = articleRepository.findById(id);
        return article.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Get articles by expert email
    @GetMapping("/expert/{email}")
    public ResponseEntity<List<Article>> getArticlesByExpertEmail(@PathVariable("email") String email) {
        try {
            List<Article> articles = articleRepository.findByExpertEmail(email);
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get articles by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Article>> getArticlesByCategory(@PathVariable("category") String category) {
        try {
            List<Article> articles = articleRepository.findByCategory(category);
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Search articles by title
    @GetMapping("/search")
    public ResponseEntity<List<Article>> searchArticles(@RequestParam("query") String query) {
        try {
            List<Article> articles = articleRepository.findByTitleContainingIgnoreCase(query);
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update article
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") Long id, @RequestBody Article article) {
        Optional<Article> articleData = articleRepository.findById(id);
        
        if (articleData.isPresent()) {
            Article existingArticle = articleData.get();
            existingArticle.setTitle(article.getTitle());
            existingArticle.setContent(article.getContent());
            existingArticle.setCategory(article.getCategory());
            
            return new ResponseEntity<>(articleRepository.save(existingArticle), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Delete article
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteArticle(@PathVariable("id") Long id) {
        try {
            articleRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
