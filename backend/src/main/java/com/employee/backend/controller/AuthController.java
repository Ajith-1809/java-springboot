package com.employee.backend.controller;

import com.employee.backend.dto.AuthResponse;
import com.employee.backend.dto.LoginRequest;
import com.employee.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private final RestTemplate restTemplate;

    public AuthController() {
        this.restTemplate = new RestTemplate(new org.springframework.http.client.HttpComponentsClientHttpRequestFactory());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            System.out.println("HTTP LOGIN ATTEMPT: " + req.getEmail());
            
            String url = supabaseUrl + "/rest/v1/users?email=eq." + req.getEmail() + "&select=*";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);
            headers.set("Authorization", "Bearer " + supabaseKey);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            
            List<Map<String, Object>> users = response.getBody();
            
            if (users != null && !users.isEmpty()) {
                Map<String, Object> user = users.get(0);
                String dbPassword = (String) user.get("password");
                
                if (req.getPassword().equals(dbPassword)) {
                    final UserDetails userDetails = userDetailsService.loadUserByUsername(req.getEmail());
                    final String token = jwtUtil.generateToken(userDetails);
                    return ResponseEntity.ok(new AuthResponse(token));
                }
            }
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            System.err.println("HTTP AUTH FAILED: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Network error: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String tokenHeader) {
        try {
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring(7);
                String username = jwtUtil.extractUsername(token);
                
                String url = supabaseUrl + "/rest/v1/users?email=eq." + username + "&select=*";
                HttpHeaders headers = new HttpHeaders();
                headers.set("apikey", supabaseKey);
                headers.set("Authorization", "Bearer " + supabaseKey);
                
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
                
                List<Map<String, Object>> users = response.getBody();
                if (users != null && !users.isEmpty()) {
                    Map<String, Object> user = users.get(0);
                    user.remove("password");
                    return ResponseEntity.ok(user);
                }
            }
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
