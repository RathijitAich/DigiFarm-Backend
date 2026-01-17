package com.example.demo.repositories;

import com.example.demo.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    // Find all articles by expert email
    List<Article> findByExpertEmail(String expertEmail);
    
    // Find articles by category
    List<Article> findByCategory(String category);
    
    // Find all articles ordered by created date
    List<Article> findAllByOrderByCreatedDateDesc();
    
    // Find articles by title containing (search)
    List<Article> findByTitleContainingIgnoreCase(String title);
}
