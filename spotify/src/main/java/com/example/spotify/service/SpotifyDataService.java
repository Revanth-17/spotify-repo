package com.example.spotify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyDataService {

    @Autowired
    private SpotifyAuthService authService;

    @Autowired
    private RestTemplate restTemplate;

    public Object getTopTracks() {
        String url = "https://api.spotify.com/v1/me/player/recently-played?limit=10";
        return sendGetRequest(url);
    }

    public Object getCurrentlyPlaying() {
        String url = "https://api.spotify.com/v1/me/player/currently-playing";
        return sendGetRequest(url);
    }

    public void pausePlayback() {
        String url = "https://api.spotify.com/v1/me/player/pause";
        sendPutRequest(url);
    }

    public void playTrack(String trackId) {
        String url = "https://api.spotify.com/v1/me/player/play";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"uris\": [\"spotify:track:" + trackId + "\"]}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
    }

    public Object getFollowedArtists() {
        String url = "https://api.spotify.com/v1/me/following?type=artist";
        return sendGetRequest(url);
    }

    private Object sendGetRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        authService.checkAndRefreshTokenIfRequired();
        headers.setBearerAuth(authService.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        return response.getBody();
    }

    private void sendPutRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        authService.checkAndRefreshTokenIfRequired();
        headers.setBearerAuth(authService.getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }
}
