package com.idealista.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idealista.application.data.AdsSource;
import com.idealista.application.domain.Ad;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetQualityAds implements GetAds {

    private final AdsSource adsSource;

    @Override
    public List<Ad> getAds() {
        return adsSource.findAll();
    }
}
