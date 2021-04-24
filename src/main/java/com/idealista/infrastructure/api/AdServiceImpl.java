package com.idealista.infrastructure.api;

import java.util.List;

import com.idealista.application.GetAds;
import com.idealista.application.UpdateRanking;
import com.idealista.application.domain.Ad;

public class AdServiceImpl implements AdService {

    private GetAds getUserAdds;
    private GetAds getAdds;
    private UpdateRanking updateRankingAdds;

    @Override
    public void triggerScoreUpdate() {
        updateRankingAdds.updateRanking();
    }

    @Override
    public List<PublicAd> getPublicAds() {
        return transformToPublic(getUserAdds.getAds());
    }

    @Override
    public List<QualityAd> getQualityAds() {
        return transformToQuality(getAdds.getAds());
    }

    private List<PublicAd> transformToPublic(List<Ad> ads) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<QualityAd> transformToQuality(List<Ad> ads) {
        // TODO Auto-generated method stub
        return null;
    }
}
