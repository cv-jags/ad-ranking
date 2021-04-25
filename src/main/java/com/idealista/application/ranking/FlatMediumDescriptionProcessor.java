package com.idealista.application.ranking;

import org.springframework.stereotype.Component;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;

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
        ad.addScore(config.getFlatMediumDescriptionScore());
    }

    private boolean isMediumSize(String description) {
        int descSize = countWords(description);
        return descSize >= config.getFlatMediumDescriptionMin()
                && descSize < config.getFlatLargeDescriptionMin();
    }
}
