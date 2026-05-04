package com.employee.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Using HTTP Data API to find user - Firewall Proof
            String url = supabaseUrl + "/rest/v1/users?email=eq." + username + "&select=*";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);
            headers.set("Authorization", "Bearer " + supabaseKey);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            
            List<Map<String, Object>> users = response.getBody();
            if (users == null || users.isEmpty()) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            Map<String, Object> user = users.get(0);
            String email = (String) user.get("email");
            String password = (String) user.get("password");
            String role = (String) user.get("role");

            return org.springframework.security.core.userdetails.User.builder()
                    .username(email)
                    .password(password)
                    .roles(role != null ? role : "USER")
                    .build();
                    
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error fetching user via HTTP: " + e.getMessage());
        }
    }
}
