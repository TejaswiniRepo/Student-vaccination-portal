package com.schoolvaccination.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @PostMapping("login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        Map<String, String> response = new HashMap<>();
        if ("admin".equals(username) && "admin123".equals(password)) {
            response.put("token", "fake-jwt-token");
            return response;
        } else {
            response.put("error", "Invalid credentials");
            return response;
        }
    }
}
