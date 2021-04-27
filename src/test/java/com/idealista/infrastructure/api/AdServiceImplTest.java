package com.idealista.infrastructure.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import com.idealista.application.GetAds;
import com.idealista.application.UpdateRanking;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;
import com.idealista.application.domain.Picture;
import com.idealista.application.domain.PictureQuality;

@ExtendWith(MockitoExtension.class)
public class AdServiceImplTest {

    private static final int ID = 1;
    private static final String DESCRIPTION = "description";
    private static final String FLAT = "FLAT";
    private static final int HOUSE_SIZE = 90;
    private static final int GARDEN_SIZE = 5;
    private static final String PICTURE_URL_1 = "picture-url/1";
    private static final String PICTURE_URL_2 = "picture-url/2";

    private AdServiceImpl service;

    @Mock
    private GetAds getUserAds;
    @Mock
    private GetAds getQualityAds;
    @Mock
    private UpdateRanking updateRankingAds;

    @BeforeEach
    public void beforeEach() {
        service = new AdServiceImpl(getUserAds, getQualityAds, updateRankingAds);
    }

    @Test
    public void getPublicAds_ReturnsEmptyWhenGetPublicAdsReturnsEmpty() throws Exception {
        when(getUserAds.getAds()).thenReturn(Lists.newArrayList());

        List<PublicAd> result = service.getPublicAds();

        assertTrue(result.isEmpty());
        verify(getUserAds).getAds();
        verifyNoMoreInteractions(getUserAds);
        verifyNoInteractions(getQualityAds, updateRankingAds);
    }

    @Test
    public void getPublicAds_ReturnsPublicAdsWhenGetPublicAdsReturnsAds() throws Exception {
        Ad completeAd = buildCompleteAd(now());
        when(getUserAds.getAds()).thenReturn(Lists.newArrayList(completeAd, completeAd));

        List<PublicAd> result = service.getPublicAds();

        PublicAd publicAd = buildPublicAd();
        assertEquals(Lists.newArrayList(publicAd, publicAd), result);
        verify(getUserAds).getAds();
        verifyNoMoreInteractions(getUserAds);
        verifyNoInteractions(getQualityAds, updateRankingAds);
    }

    @Test
    public void getQualityAds_ReturnsEmptyWhenGetQualityAdsReturnsEmpty() throws Exception {
        when(getQualityAds.getAds()).thenReturn(Lists.newArrayList());

        List<QualityAd> result = service.getQualityAds();

        assertTrue(result.isEmpty());
        verify(getQualityAds).getAds();
        verifyNoMoreInteractions(getQualityAds);
        verifyNoInteractions(getUserAds, updateRankingAds);
    }

    @Test
    public void getPublicAds_ReturnsQaAdsWhenGetPublicAdsReturnsMinimalAd() throws Exception {
        Ad emptyAd = buildEmptyAd();
        when(getUserAds.getAds()).thenReturn(Lists.newArrayList(emptyAd));

        List<PublicAd> result = service.getPublicAds();

        assertEquals(Lists.newArrayList(buildEmptyPublicAd()), result);
        verify(getUserAds).getAds();
        verifyNoMoreInteractions(getUserAds);
        verifyNoInteractions(getQualityAds, updateRankingAds);
    }

    @Test
    public void getQualityAds_ReturnsQaAdsWhenGetQualityAdsReturnsAds() throws Exception {
        Timestamp now = now();
        Ad completeAd = buildCompleteAd(now);
        when(getQualityAds.getAds()).thenReturn(Lists.newArrayList(completeAd, completeAd));

        List<QualityAd> result = service.getQualityAds();

        QualityAd qaAd = buildQualityAd(now);
        assertEquals(Lists.newArrayList(qaAd, qaAd), result);
        verify(getQualityAds).getAds();
        verifyNoMoreInteractions(getQualityAds);
        verifyNoInteractions(getUserAds, updateRankingAds);
    }

    @Test
    public void getQualityAds_ReturnsQaAdsWhenGetQualityAdsReturnsMinimalAd() throws Exception {
        Ad emptyAd = buildEmptyAd();
        when(getQualityAds.getAds()).thenReturn(Lists.newArrayList(emptyAd));

        List<QualityAd> result = service.getQualityAds();

        assertEquals(Lists.newArrayList(buildEmptyQualityAd()), result);
        verify(getQualityAds).getAds();
        verifyNoMoreInteractions(getQualityAds);
        verifyNoInteractions(getUserAds, updateRankingAds);
    }

    @Test
    public void triggerScoreUpdate() throws Exception {

        service.triggerScoreUpdate();

        verify(updateRankingAds).updateRanking();
        verifyNoMoreInteractions(updateRankingAds);
        verifyNoInteractions(getUserAds, getQualityAds);
    }

    protected Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    private Ad buildEmptyAd() {
        return Ad.builder().build();
    }

    private PublicAd buildEmptyPublicAd() {
        return PublicAd.builder().build();
    }

    private QualityAd buildEmptyQualityAd() {
        return QualityAd.builder().build();
    }

    private Ad buildCompleteAd(Timestamp irrelevantSince) {
        return Ad.builder()
                .id(1)
                .description(DESCRIPTION)
                .typology(AdType.FLAT)
                .houseSize(HOUSE_SIZE)
                .gardenSize(GARDEN_SIZE)
                .irrelevantSince(irrelevantSince)
                .score(50)
                .pictures(Lists.newArrayList(
                        Picture.builder()
                                .id(1)
                                .url(PICTURE_URL_1)
                                .quality(PictureQuality.SD)
                                .build(),
                        Picture.builder()
                                .id(2)
                                .url(PICTURE_URL_2)
                                .quality(PictureQuality.HD)
                                .build()))
                .build();
    }

    private PublicAd buildPublicAd() {
        return PublicAd.builder()
                .id(ID)
                .description(DESCRIPTION)
                .typology(FLAT)
                .houseSize(HOUSE_SIZE)
                .gardenSize(GARDEN_SIZE)
                .pictureUrls(Lists.newArrayList(PICTURE_URL_1, PICTURE_URL_2))
                .build();
    }

    private QualityAd buildQualityAd(Timestamp irrelevantSince) {
        return QualityAd.builder()
                .id(ID)
                .description(DESCRIPTION)
                .typology(FLAT)
                .houseSize(HOUSE_SIZE)
                .gardenSize(GARDEN_SIZE)
                .pictureUrls(Lists.newArrayList(PICTURE_URL_1, PICTURE_URL_2))
                .score(50)
                .irrelevantSince(new Date(irrelevantSince.getTime()))
                .build();
    }
}
