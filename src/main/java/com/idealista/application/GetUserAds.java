package com.idealista.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.data.AdsSource;
import com.idealista.application.domain.Ad;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserAds implements GetAds {

    private final AdsSource adsSource;
    private final RankingConfiguration config;

    @Override
    public List<Ad> getAds() {
        return adsSource.findByScoreGreaterThanOrEqualTo(config.getRelevantScore());
    }

}
