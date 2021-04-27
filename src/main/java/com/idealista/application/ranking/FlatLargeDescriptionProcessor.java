package com.idealista.application.ranking;

import org.springframework.stereotype.Component;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FlatLargeDescriptionProcessor extends FlatProcessor implements RankingProcessor {

    public FlatLargeDescriptionProcessor(RankingConfiguration config) {
        super(config);
    }

    @Override
    public boolean accept(Ad ad) {
        return super.accept(ad) && isLargeSize(ad.getDescription());
    }

    @Override
    public void process(Ad ad) {
        int flatLargeDescriptionScore = config.getFlatLargeDescriptionScore();
        log.info("Scores {} for large flat description (ad: {})", flatLargeDescriptionScore,
                ad.getId());
        ad.addScore(flatLargeDescriptionScore);
    }

    private boolean isLargeSize(String description) {
        return countWords(description) >= config.getFlatLargeDescriptionMin();
    }

}
