package com.idealista.application;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.data.AdsSource;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;
import com.idealista.application.domain.Picture;
import com.idealista.application.domain.PictureQuality;

@ExtendWith(MockitoExtension.class)
public class GetUserAdsTest {

    private static final int RELEVANT_SCORE = 40;

    private GetUserAds getUserAds;

    @Mock
    private AdsSource adsSource;

    @BeforeEach
    public void beforeEach() {
        RankingConfiguration config = new RankingConfiguration();
        config.setRelevantScore(RELEVANT_SCORE);
        getUserAds = new GetUserAds(adsSource, config);
    }

    @Test
    public void getAds_ReturnsAdsSourceFindAllResponse() {
        Ad ad = buildCompleteAd(null);
        ArrayList<Ad> response = Lists.newArrayList(ad, ad);
        when(adsSource.findByScoreGreaterThanOrEqualTo(anyInt())).thenReturn(response);

        List<Ad> result = getUserAds.getAds();

        assertSame(response, result);
        verify(adsSource).findByScoreGreaterThanOrEqualTo(RELEVANT_SCORE);
        verifyNoMoreInteractions(adsSource);
    }

    private Ad buildCompleteAd(Timestamp now) {
        Ad ad = Ad.builder()
                .id(1)
                .description("description")
                .typology(AdType.FLAT)
                .houseSize(90)
                .gardenSize(5)
                .irrelevantSince(now)
                .score(50)
                .pictures(Lists.newArrayList(
                        Picture.builder()
                                .id(1)
                                .url("picture-url/1")
                                .quality(PictureQuality.SD)
                                .build(),
                        Picture.builder()
                                .id(2)
                                .url("picture-url/2")
                                .quality(PictureQuality.HD)
                                .build()))
                .build();
        return ad;
    }
}

