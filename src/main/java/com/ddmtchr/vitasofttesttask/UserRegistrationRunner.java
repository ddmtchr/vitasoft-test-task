package com.ddmtchr.vitasofttesttask;

import com.ddmtchr.vitasofttesttask.dto.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRegistrationRunner implements CommandLineRunner {
    private final RestTemplate restTemplate;

    @Override
    public void run(String... args) throws Exception {
        registerUser("John", "qwerty", Set.of("USER"));
        registerUser("Paul", "123456", Set.of("OPERATOR"));
        registerUser("Andrew", "asdfgh", Set.of("ADMIN"));
    }

    private void registerUser(String username, String password, Set<String> roles) {
        String url = "http://localhost:8080/api/auth/register";
        RegisterRequest request = new RegisterRequest(username, password, roles);

        try {
            ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("User " + username + " registered with status: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("Failed to register user " + username + ": " + e.getMessage());
        }
    }
}
