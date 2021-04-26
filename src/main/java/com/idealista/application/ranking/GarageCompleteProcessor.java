package com.idealista.application.ranking;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;
import com.idealista.application.domain.Picture;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GarageCompleteProcessor implements RankingProcessor {

    private final RankingConfiguration config;

    @Override
    public boolean accept(Ad ad) {
        return AdType.GARAGE.equals(ad.getTypology()) && isComplete(ad);
    }

    @Override
    public void process(Ad ad) {
        ad.addScore(config.getGarageCompleteScore());
    }

    private boolean isComplete(Ad ad) {
        return hasPicture(ad) && hasHouseSize(ad);
    }

    private boolean hasPicture(Ad ad) {
        List<Picture> pictures = ad.getPictures();
        return Objects.nonNull(pictures) && !pictures.isEmpty();
    }

    private boolean hasHouseSize(Ad ad) {
        Integer houseSize = ad.getHouseSize();
        return Objects.nonNull(houseSize) && houseSize > 0;
    }
}
