package com.idealista.application.ranking;

import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;

@Component
public class ChaletDescriptionProcessor extends ChaletProcessor {

    public ChaletDescriptionProcessor(RankingConfiguration config) {
        super(config);
    }

    @Override
    public boolean accept(Ad ad) {
        return super.accept(ad) && isLargeDescription(ad.getDescription());
    }

    public void process(Ad ad) {
        ad.addScore(config.getChaletLargeDescriptionScore());
    }

    private boolean isLargeDescription(String description) {
        return notBlank(description)
                && countWords(description) >= config.getChaletLargeDescriptionMin();
    }

    private int countWords(String description) {
        return new StringTokenizer(description).countTokens();
    }

}
