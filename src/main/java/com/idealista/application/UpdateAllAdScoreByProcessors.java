package com.idealista.application;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.data.AdsSource;
import com.idealista.application.domain.Ad;
import com.idealista.application.ranking.RankingProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateAllAdScoreByProcessors implements UpdateRanking {

    private final AdsSource adsSource;
    private final List<RankingProcessor> processors;
    private final RankingConfiguration config;

    @Override
    public void updateRanking() {
        log.info("Calculating ranking scores");
        List<Ad> updatedAds = adsSource.findAll()
                .stream()
                .map(this::calculateNewScore)
                .collect(Collectors.toList());
        adsSource.updateScores(updatedAds);
    }

    private Ad calculateNewScore(Ad ad) {
        log.info("Calculating score for ad: {}", ad);
        ad.setScore(0);
        processors.stream().filter(p -> p.accept(ad)).forEach(p -> p.process(ad));
        calculateIrrelevantSince(ad);
        adjustScoreToRank(ad);
        return ad;
    }

    private void calculateIrrelevantSince(Ad ad) {
        if (ad.getScore() < config.getRelevantScore()) {
            if (Objects.isNull(ad.getIrrelevantSince())) {
                ad.setIrrelevantSince(now());
            }
        } else if (Objects.nonNull(ad.getIrrelevantSince())) {
            ad.setIrrelevantSince(null);
        }
    }

    private void adjustScoreToRank(Ad ad) {
        Integer score = ad.getScore();
        if(score > 100) {
            ad.setScore(100);
        } else if(score < 0) {
            ad.setScore(0);
        }
    }

    private Instant now() {
        return Instant.ofEpochMilli(System.currentTimeMillis());
    }
}
