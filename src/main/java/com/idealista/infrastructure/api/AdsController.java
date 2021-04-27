package com.idealista.infrastructure.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdsController {

    private final AdService adService;

    @GetMapping("/qa")
    public ResponseEntity<List<QualityAd>> qualityListing() {
        return ResponseEntity.ok(adService.getQualityAds());
    }

    @GetMapping("/")
    public ResponseEntity<List<PublicAd>> publicListing() {
        return ResponseEntity.ok(adService.getPublicAds());
    }

    @PostMapping("/score")
    public ResponseEntity<Void> calculateScore() {
        adService.triggerScoreUpdate();
        return ResponseEntity.ok().build();
    }
}
