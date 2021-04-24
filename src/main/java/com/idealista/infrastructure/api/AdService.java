package com.idealista.infrastructure.api;

import java.util.List;

public interface AdService {

    void triggerScoreUpdate();

    List<PublicAd> getPublicAds();

    List<QualityAd> getQualityAds();
}
