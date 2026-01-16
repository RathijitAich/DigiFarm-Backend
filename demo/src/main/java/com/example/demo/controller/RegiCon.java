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
import java.util.HexFormat;
import java.util.Map;

@RestController

@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class RegiCon {

    private final FarmerRepo farmerRepo;
    private final ExpertRepo expertRepo;

    public RegiCon(FarmerRepo farmerRepo, ExpertRepo expertRepo) {
        this.farmerRepo = farmerRepo;
        this.expertRepo = expertRepo;
    }

    public record FarmerRegisterRequest(
            String name,
            String email,
            String password,
            String phone,
            String village,
            String city,
            String district,
            String country
    ) {}

    public record ExpertRegisterRequest(
            String name,
            String email,
            String password,
            String phone,
            String village,
            String city,
            String district,
            String country,
            String specialization
    ) {}

    @PostMapping("/register/farmer")
    public ResponseEntity<?> registerFarmer(@RequestBody FarmerRegisterRequest req) {
        if (isBlank(req.name()) || isBlank(req.email()) || isBlank(req.password())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "name, email, and password are required"
            ));
        }

        if (farmerRepo.findByEmail(req.email().trim()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", "Email already registered as farmer"
            ));
        }

        Farmer farmer = new Farmer();
        farmer.setName(req.name().trim());
        farmer.setEmail(req.email().trim().toLowerCase());
        farmer.setPassword(sha256(req.password())); // replace with BCrypt later if you add Spring Security
        farmer.setPhone(trimToNull(req.phone()));
        farmer.setVillage(trimToNull(req.village()));
        farmer.setCity(trimToNull(req.city()));
        farmer.setDistrict(trimToNull(req.district()));
        farmer.setCountry(trimToNull(req.country()));

        Farmer saved = farmerRepo.save(farmer);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Farmer registered",
                "farmerId", saved.getFarmerId(),
                "email", saved.getEmail()
        ));
    }

    @PostMapping("/register/expert")
    public ResponseEntity<?> registerExpert(@RequestBody ExpertRegisterRequest req) {
        if (isBlank(req.name()) || isBlank(req.email()) || isBlank(req.password())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "name, email, and password are required"
            ));
        }

        if (expertRepo.findByEmail(req.email().trim()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", "Email already registered as expert"
            ));
        }

        Expert expert = new Expert();
        expert.setName(req.name().trim());
        expert.setEmail(req.email().trim().toLowerCase());
        expert.setPassword(sha256(req.password())); // replace with BCrypt later if you add Spring Security
        expert.setPhone(trimToNull(req.phone()));
        expert.setVillage(trimToNull(req.village()));
        expert.setCity(trimToNull(req.city()));
        expert.setDistrict(trimToNull(req.district()));
        expert.setCountry(trimToNull(req.country()));
        expert.setSpecialization(trimToNull(req.specialization()));

        Expert saved = expertRepo.save(expert);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Expert registered",
                "expertId", saved.getExpertId(),
                "email", saved.getEmail()
        ));
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String sha256(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash password", e);
        }
    }
}

