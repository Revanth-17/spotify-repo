package com.example.spotify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify.service.SpotifyDataService;

@RestController
@RequestMapping("/spotify")
public class SpotifyDataController {

    @Autowired
    private SpotifyDataService dataService;

    @GetMapping("/top-tracks")
    public Object getTopTracks() {
        return dataService.getTopTracks();
    }

    @GetMapping("/now-playing")
    public Object getNowPlaying() {
        return dataService.getCurrentlyPlaying();
    }

    @PutMapping("/pause")
    public void pausePlayback() {
        dataService.pausePlayback();
    }

    @PutMapping("/play/{trackId}")
    public void playTrack(@PathVariable String trackId) {
        dataService.playTrack(trackId);
    }

    @GetMapping("/followed-artists")
    public Object getFollowedArtists() {
        return dataService.getFollowedArtists();
    }
}
