package com.example.spotify.service;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.spotify.util.TokenStorageService;

import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyAuthService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    private String accessToken;
    private String refreshToken;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TokenStorageService tokenStorageService; 
    public String handleCallback(String code) {
        String url = "https://accounts.spotify.com/api/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        Map<String, Object> responseBody = response.getBody();

        accessToken = (String) responseBody.get("access_token");
        refreshToken = (String) responseBody.get("refresh_token");
        tokenStorageService.saveTokens(accessToken, refreshToken, System.currentTimeMillis()+ 180000);
        return "Tokens updated successfully";
    }


    public void refreshAccessToken() {
        String url = "https://accounts.spotify.com/api/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", refreshToken);
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        Map<String, Object> responseBody = response.getBody();
        accessToken = (String) responseBody.get("access_token");
        tokenStorageService.saveTokens(accessToken, refreshToken, System.currentTimeMillis()+ 180000);
    }

    public String getAccessToken() {
        return accessToken;
    }
    public void checkAndRefreshTokenIfRequired() {
        Map<String, Object> tokens = tokenStorageService.readTokens();

        if (tokens == null || !tokens.containsKey("access_token") || !tokens.containsKey("expires_at")) {
            throw new RuntimeException("Tokens not initialized. Please authenticate via /spotify/auth/callback");
        }

        long expiresAt = ((Number) tokens.get("expires_at")).longValue();
        long currentTime = System.currentTimeMillis();

        if (currentTime >= (expiresAt - 6000)) {
            refreshAccessToken(); // existing method in your class
        }
    }
}