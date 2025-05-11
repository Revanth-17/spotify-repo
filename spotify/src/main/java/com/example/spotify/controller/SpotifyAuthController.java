package com.example.spotify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify.service.SpotifyAuthService;

@RestController
@RequestMapping("/spotify/auth")
public class SpotifyAuthController {

    @Autowired
    private SpotifyAuthService authService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {
        return authService.handleCallback(code);
    }

    @GetMapping("/refresh")
    public String refreshToken() {
        authService.refreshAccessToken();
        return "Access token refreshed successfully";
    }
}
