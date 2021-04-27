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
public class FlatCompleteProcessor extends FlatProcessor implements RankingProcessor {

    public FlatCompleteProcessor(RankingConfiguration config) {
        super(config);
    }

    @Override
    public boolean accept(Ad ad) {
        return super.accept(ad) && isComplete(ad);
    }

    @Override
    public void process(Ad ad) {
        int flatCompleteScore = config.getFlatCompleteScore();
        log.info("Scores {} for complete flat (ad: {})", flatCompleteScore, ad.getId());
        ad.addScore(flatCompleteScore);
    }

    private boolean isComplete(Ad ad) {
        return hasDescription(ad) && hasPicture(ad) && hasHouseSize(ad);
    }

    private boolean hasDescription(Ad ad) {
        return isNotBlank(ad.getDescription());
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
