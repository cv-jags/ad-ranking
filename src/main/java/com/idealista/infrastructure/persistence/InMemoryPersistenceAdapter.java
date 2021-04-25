package com.idealista.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import com.idealista.application.data.AdsSource;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;
import com.idealista.application.domain.Picture;
import com.idealista.application.domain.PictureQuality;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryPersistenceAdapter implements AdsSource {

    private final InMemoryPersistence persistence;

    @Override
    public List<Integer> updateScores(Iterable<Ad> newScores) {
        log.debug("Updating ad scores: ", newScores);
        return StreamSupport.stream(newScores.spliterator(), false)
                .map(this::updateScoreSafe)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ad> findAll() {
        log.debug("Finding all adds");
        return transformFrom(persistence.findAll());
    }

    @Override
    public List<Ad> findByScoreGreaterThanOrEqualTo(int score) {
        log.debug("Finding by score greater than or equal to {}", score);
        return transformFrom(persistence.findByScoreGreaterThanOrEqualTo(score));
    }

    public Integer updateScore(Ad newScore) {
        log.debug("Updating ad score: ", newScore);
        AdVO adToUpdate = persistence.findById(newScore.getId())
                .orElseThrow(() -> new IllegalArgumentException("No ad found"));
        adToUpdate.setScore(newScore.getScore());
        adToUpdate.setIrrelevantSince(newScore.getIrrelevantSince());
        return persistence.save(adToUpdate).getId();
    }

    private Optional<Integer> updateScoreSafe(Ad ad) {
        try {
            return Optional.of(updateScore(ad));
        } catch (IllegalArgumentException e) {
            log.error("Error updating ad score for: {}", ad, e);
            return Optional.empty();
        }
    }

    private List<Ad> transformFrom(Iterable<AdVO> ads) {
        return StreamSupport.stream(ads.spliterator(), false)
                .map(this::transformFrom)
                .collect(Collectors.toList());
    }

    private Ad transformFrom(AdVO ad) {
        return Ad.builder()
                .id(ad.getId())
                .description(ad.getDescription())
                .houseSize(ad.getHouseSize())
                .gardenSize(ad.getGardenSize())
                .irrelevantSince(ad.getIrrelevantSince())
                .pictures(transformFromIntegerToPicture(ad.getPictures()))
                .score(ad.getScore())
                .typology(AdType.valueOf(ad.getTypology()))
                .build();
    }

    private List<Picture> transformFromIntegerToPicture(Iterable<Integer> pirctureIds) {
        return persistence.findPictureById(pirctureIds)
                .stream()
                .map(this::transformFrom)
                .collect(Collectors.toList());
    }

    private Picture transformFrom(PictureVO picture) {
        return Picture.builder()
                .id(picture.getId())
                .url(picture.getUrl())
                .quality(PictureQuality.valueOf(picture.getQuality()))
                .build();
    }
}