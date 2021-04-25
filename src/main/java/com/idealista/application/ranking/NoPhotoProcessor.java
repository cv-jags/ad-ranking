package com.idealista.application.ranking;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NoPhotoProcessor implements RankingProcessor {

    private final RankingConfiguration rankingConfiguration;

    @Override
    public boolean accept(Ad ad) {
        return Objects.isNull(ad.getPictures()) || ad.getPictures().isEmpty();
    }

    @Override
    public void process(Ad ad) {
        ad.addScore(rankingConfiguration.getNoPhotoScore());
    }

}
