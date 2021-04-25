package com.idealista.application.ranking;

import com.idealista.application.domain.Ad;

public interface RankingProcessor {

    /**
     * Evaluate if the processor affects the ranking of the add
     * @param ad
     * @return true if the ad can rank in this processor
     */
    boolean accept(Ad ad);

    /**
     * Calculate an aspect of ranking on the Ad and updates the ad ranking
     * @param ad to evaluate and update
     */
    void process(Ad ad);

}
