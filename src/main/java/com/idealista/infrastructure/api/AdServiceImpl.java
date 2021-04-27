package com.idealista.infrastructure.api;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.idealista.application.GetAds;
import com.idealista.application.UpdateRanking;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.Picture;

@Service
public class AdServiceImpl implements AdService {

    private final GetAds getUserAds;
    private final GetAds getQualityAds;
    private final UpdateRanking updateRankingAds;

    public AdServiceImpl(@Qualifier("getUserAds") GetAds getUserAdds,
            @Qualifier("getQualityAds") GetAds getQualityAdds, UpdateRanking updateRankingAdds) {
        this.getUserAds = getUserAdds;
        this.getQualityAds = getQualityAdds;
        this.updateRankingAds = updateRankingAdds;
    }

    @Override
    public List<PublicAd> getPublicAds() {
        return transformToPublic(getUserAds.getAds());
    }

    @Override
    public List<QualityAd> getQualityAds() {
        return transformToQuality(getQualityAds.getAds());
    }

    @Override
    public void triggerScoreUpdate() {
        updateRankingAds.updateRanking();
    }

    private List<PublicAd> transformToPublic(List<Ad> ads) {
        return transformTo(ads, this::transformToPublic);
    }

    private List<QualityAd> transformToQuality(List<Ad> ads) {
        return transformTo(ads, this::transformToQuality);
    }

    protected <T> List<T> transformTo(List<Ad> ads, Function<? super Ad, ? extends T> function) {
        return Optional.ofNullable(ads)
                .orElse(Collections.emptyList())
                .stream()
                .map(function)
                .collect(Collectors.toList());
    }

    private PublicAd transformToPublic(Ad ad) {
        return PublicAd.builder()
                .id(ad.getId())
                .description(ad.getDescription())
                .typology(getTypology(ad))
                .houseSize(ad.getHouseSize())
                .gardenSize(ad.getGardenSize())
                .pictureUrls(extractPictureUrls(ad))
                .build();
    }

    private QualityAd transformToQuality(Ad ad) {
        return QualityAd.builder()
                .id(ad.getId())
                .description(ad.getDescription())
                .typology(getTypology(ad))
                .houseSize(ad.getHouseSize())
                .gardenSize(ad.getGardenSize())
                .pictureUrls(extractPictureUrls(ad))
                .score(ad.getScore())
                .irrelevantSince(getIrrelevantSince(ad))
                .build();
    }

    protected String getTypology(Ad ad) {
        return Objects.nonNull(ad.getTypology()) ? ad.getTypology().name() : null;
    }

    protected List<String> extractPictureUrls(Ad ad) {
        return Optional.ofNullable(ad.getPictures())
                .orElse(Collections.emptyList())
                .stream()
                .map(Picture::getUrl)
                .collect(Collectors.toList());
    }

    protected Date getIrrelevantSince(Ad ad) {
        return Objects.nonNull(ad.getIrrelevantSince())
                ? new Date(ad.getIrrelevantSince().toEpochMilli())
                : null;
    }
}
