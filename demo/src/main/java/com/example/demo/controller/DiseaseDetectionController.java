package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/disease-detection")
@CrossOrigin(origins = "*")
public class DiseaseDetectionController {

    @Value("${ml.service.url:http://localhost:5000}")
    private String mlServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<?> checkHealth() {
        try {
            String url = mlServiceUrl + "/health";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "unhealthy");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
    }

    /**
     * Predict disease from uploaded image
     */
    @PostMapping(value = "/predict", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> predictDisease(@RequestParam("image") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "No file uploaded");
                return ResponseEntity.badRequest().body(error);
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || 
                (!contentType.equals("image/jpeg") && 
                 !contentType.equals("image/png") && 
                 !contentType.equals("image/jpg"))) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid file type. Only JPEG and PNG are allowed");
                return ResponseEntity.badRequest().body(error);
            }

            // Validate file size (max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "File size too large. Maximum 10MB allowed");
                return ResponseEntity.badRequest().body(error);
            }

            // Forward to Flask ML service
            String url = mlServiceUrl + "/predict";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to process image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all available disease classes
     */
    @GetMapping("/classes")
    public ResponseEntity<?> getClasses() {
        try {
            String url = mlServiceUrl + "/classes";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve classes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}