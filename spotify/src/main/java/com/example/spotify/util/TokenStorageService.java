package com.example.spotify.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenStorageService {

 private static final String FILE_PATH = "token.json";
 private final ObjectMapper mapper = new ObjectMapper();

 public void saveTokens(String accessToken, String refreshToken, long expiresAt) {
     Map<String, Object> data = new HashMap<>();
     data.put("access_token", accessToken);
     data.put("refresh_token", refreshToken);
     data.put("expires_at", expiresAt);
     try {
         mapper.writeValue(new File(FILE_PATH), data);
     } catch (IOException e) {
         throw new RuntimeException("Error saving tokens", e);
     }
 }

 public Map<String, Object> readTokens() {
     try {
         return mapper.readValue(new File(FILE_PATH), Map.class);
     } catch (IOException e) {
         return new HashMap<>();
     }
 }
}