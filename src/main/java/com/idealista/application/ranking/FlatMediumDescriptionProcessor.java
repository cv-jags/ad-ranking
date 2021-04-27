package com.idealista.application.ranking;

import org.springframework.stereotype.Component;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FlatMediumDescriptionProcessor extends FlatProcessor implements RankingProcessor {

    public FlatMediumDescriptionProcessor(RankingConfiguration config) {
        super(config);
    }

    @Override
    public boolean accept(Ad ad) {
        return super.accept(ad) && isMediumSize(ad.getDescription());
    }

    @Override
    public void process(Ad ad) {
        int flatMediumDescriptionScore = config.getFlatMediumDescriptionScore();
        log.info("Scores {} for medium flat description (ad: {})", flatMediumDescriptionScore,
                ad.getId());
        ad.addScore(flatMediumDescriptionScore);
    }

    private boolean isMediumSize(String description) {
        int descSize = countWords(description);
        return descSize >= config.getFlatMediumDescriptionMin()
                && descSize < config.getFlatLargeDescriptionMin();
    }
}
