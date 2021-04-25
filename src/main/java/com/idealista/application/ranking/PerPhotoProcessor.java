package com.idealista.application.ranking;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.Picture;
import com.idealista.application.domain.PictureQuality;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PerPhotoProcessor implements RankingProcessor {

    private final RankingConfiguration rankingConfiguration;

    @Override
    public boolean accept(Ad ad) {
        return Objects.nonNull(ad.getPictures()) && !ad.getPictures().isEmpty();
    }

    @Override
    public void process(Ad ad) {
        ad.addScore(calculatePerPhotoScore(ad));
    }

    private int calculatePerPhotoScore(Ad ad) {
        return ad.getPictures().stream().mapToInt(this::getPhotoScore).sum();
    }

    private int getPhotoScore(Picture picture) {
        return isHdPicture(picture) ? hdScore() : sdScore();
    }

    private boolean isHdPicture(Picture picture) {
        return PictureQuality.HD.equals(picture.getQuality());
    }

    private int sdScore() {
        return rankingConfiguration.getSdPhotoScore();
    }

    private int hdScore() {
        return rankingConfiguration.getHdPhotoScore();
    }
}
