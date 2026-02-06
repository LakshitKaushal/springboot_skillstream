package com.skillstream.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
            "message", "SkillStream API is running",
            "version", "1.0.0",
            "endpoints", Map.of(
                "auth", "/api/v1/auth",
                "courses", "/api/v1/courses",
                "favourites", "/api/v1/favourites",
                "admin", "/api/v1/admin"
            )
        );
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}

