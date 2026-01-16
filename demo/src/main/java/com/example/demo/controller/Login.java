package com.example.demo.controller;

import com.example.demo.entity.Expert;
import com.example.demo.entity.Farmer;
import com.example.demo.repositories.ExpertRepo;
import com.example.demo.repositories.FarmerRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class Login {

    private final FarmerRepo farmerRepo;
    private final ExpertRepo expertRepo;

    public Login(FarmerRepo farmerRepo, ExpertRepo expertRepo) {
        this.farmerRepo = farmerRepo;
        this.expertRepo = expertRepo;
    }

    public record LoginRequest(
            String email,
            String password
    ) {}

    @PostMapping("/login/farmer")
    public ResponseEntity<?> loginFarmer(@RequestBody LoginRequest req) {
        if (isBlank(req.email()) || isBlank(req.password())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Email and password are required"
            ));
        }

        Optional<Farmer> farmerOpt = farmerRepo.findByEmail(req.email().trim().toLowerCase());
        
        if (farmerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Invalid email or password"
            ));
        }

        Farmer farmer = farmerOpt.get();
        String hashedPassword = sha256(req.password());

        if (!hashedPassword.equals(farmer.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Invalid email or password"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "farmerId", farmer.getFarmerId(),
                "name", farmer.getName(),
                "email", farmer.getEmail(),
                "userType", "farmer"
        ));
    }

    @PostMapping("/login/expert")
    public ResponseEntity<?> loginExpert(@RequestBody LoginRequest req) {
        if (isBlank(req.email()) || isBlank(req.password())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Email and password are required"
            ));
        }

        Optional<Expert> expertOpt = expertRepo.findByEmail(req.email().trim().toLowerCase());
        
        if (expertOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Invalid email or password"
            ));
        }

        Expert expert = expertOpt.get();
        String hashedPassword = sha256(req.password());

        if (!hashedPassword.equals(expert.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Invalid email or password"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "expertId", expert.getExpertId(),
                "name", expert.getName(),
                "email", expert.getEmail(),
                "specialization", expert.getSpecialization() != null ? expert.getSpecialization() : "",
                "userType", "expert"
        ));
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String sha256(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash password", e);
        }
    }
}
