package com.idealista.application.ranking;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.Picture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChaletCompleteProcessor extends ChaletProcessor {

    public ChaletCompleteProcessor(RankingConfiguration config) {
        super(config);
    }

    @Override
    public boolean accept(Ad ad) {
        return super.accept(ad) && isComplete(ad);
    }

    @Override
    public void process(Ad ad) {
        int chaletCompleteScore = config.getChaletCompleteScore();
        log.info("Scores {} for complete Chalet (ad {})", chaletCompleteScore, ad.getId());
        ad.addScore(chaletCompleteScore);
    }

    private boolean isComplete(Ad ad) {
        return notBlank(ad.getDescription()) && hasPicture(ad) && hasHouseAndGardenSize(ad);
    }

    private boolean hasPicture(Ad ad) {
        List<Picture> pictures = ad.getPictures();
        return Objects.nonNull(pictures) && !pictures.isEmpty();
    }

    private boolean hasHouseAndGardenSize(Ad ad) {
        Integer houseSize = ad.getHouseSize();
        Integer gardenSize = ad.getGardenSize();
        return Objects.nonNull(houseSize) && houseSize > 0 && Objects.nonNull(gardenSize)
                && gardenSize > 0;
    }

}
