package com.idealista.application;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.data.AdsSource;
import com.idealista.application.domain.Ad;
import com.idealista.application.ranking.RankingProcessor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateAllAdScoreByProcessors implements UpdateRanking {

    private final AdsSource adsSource;
    private final List<RankingProcessor> processors;
    private final RankingConfiguration config;

    @Override
    public void updateRanking() {
        List<Ad> updatedAds = adsSource.findAll()
                .stream()
                .map(this::calculateNewScore)
                .collect(Collectors.toList());
        adsSource.updateScores(updatedAds);
    }

    private Ad calculateNewScore(Ad ad) {
        ad.setScore(0);
        processors.stream().filter(p -> p.accept(ad)).forEach(p -> p.process(ad));
        calculateIrrelevantSince(ad);
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

    private Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }
}
