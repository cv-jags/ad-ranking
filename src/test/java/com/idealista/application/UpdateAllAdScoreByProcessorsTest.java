package com.idealista.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
import com.idealista.Main.RankingConfiguration;
import com.idealista.application.data.AdsSource;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;
import com.idealista.application.domain.Picture;
import com.idealista.application.domain.PictureQuality;
import com.idealista.application.ranking.RankingProcessor;

@ExtendWith(MockitoExtension.class)
public class UpdateAllAdScoreByProcessorsTest {

    private static final Integer SCORE_VALUE = 50;

    private UpdateAllAdScoreByProcessors updateAllAdsScore;

    @Mock
    private AdsSource adsSource;
    @Mock
    private RankingProcessor processor1;
    @Mock
    private RankingProcessor processor2;
    @Mock
    private RankingConfiguration config;

    @BeforeEach
    public void beforeEach() {
        List<RankingProcessor> processors = Lists.newArrayList(processor1, processor2);
        updateAllAdsScore = new UpdateAllAdScoreByProcessors(adsSource, processors, config);
    }

    @Test
    public void updateRanking_SetScoreAndIrrelevanceSinceWhenScoreIsBelowRelevant() {
        Instant startingTime = Instant.now();
        Ad ad = buildCompleteAd(null);
        ArrayList<Ad> allAds = Lists.newArrayList(ad);
        when(adsSource.findAll()).thenReturn(allAds);
        when(processor1.accept(any())).thenReturn(false);
        when(processor2.accept(any())).thenReturn(true);
        doAnswer(updateScore(SCORE_VALUE)).when(processor2).process(any());
        when(config.getRelevantScore()).thenReturn(SCORE_VALUE + 1);

        updateAllAdsScore.updateRanking();

        assertEquals(SCORE_VALUE, ad.getScore());
        assertTrue(ad.getIrrelevantSince().isAfter(startingTime));
        verify(adsSource).findAll();
        verify(adsSource).updateScores(allAds);
        verify(processor1).accept(ad);
        verify(processor2).accept(ad);
        verify(processor2).process(ad);
        verify(config).getRelevantScore();
        verifyNoMoreInteractions(processor1, processor2, config, adsSource);
    }

    @Test
    public void updateRanking_SetScoreAndIrrelevanceSinceWhenScoreIsBelowRelevantAndPreviousIrrelevantSince() {
        Instant startingTime = Instant.now();
        Ad ad = buildCompleteAd(startingTime);
        ArrayList<Ad> allAds = Lists.newArrayList(ad);
        when(adsSource.findAll()).thenReturn(allAds);
        when(processor1.accept(any())).thenReturn(false);
        when(processor2.accept(any())).thenReturn(true);
        doAnswer(updateScore(SCORE_VALUE)).when(processor2).process(any());
        when(config.getRelevantScore()).thenReturn(SCORE_VALUE + 1);

        updateAllAdsScore.updateRanking();

        assertEquals(SCORE_VALUE, ad.getScore());
        assertSame(startingTime, ad.getIrrelevantSince());
        verify(adsSource).findAll();
        verify(adsSource).updateScores(allAds);
        verify(processor1).accept(ad);
        verify(processor2).accept(ad);
        verify(processor2).process(ad);
        verify(config).getRelevantScore();
        verifyNoMoreInteractions(processor1, processor2, config, adsSource);
    }

    @Test
    public void updateRanking_SetScoreAndIrrelevanceSinceWhenScoreIsEqualToRelevant() {
        Ad ad = buildCompleteAd(null);
        ArrayList<Ad> allAds = Lists.newArrayList(ad);
        when(adsSource.findAll()).thenReturn(allAds);
        when(processor1.accept(any())).thenReturn(false);
        when(processor2.accept(any())).thenReturn(true);
        doAnswer(updateScore(SCORE_VALUE)).when(processor2).process(any());
        when(config.getRelevantScore()).thenReturn(SCORE_VALUE);

        updateAllAdsScore.updateRanking();

        assertEquals(SCORE_VALUE, ad.getScore());
        assertNull(ad.getIrrelevantSince());
        verify(adsSource).findAll();
        verify(adsSource).updateScores(allAds);
        verify(processor1).accept(ad);
        verify(processor2).accept(ad);
        verify(processor2).process(ad);
        verify(config).getRelevantScore();
        verifyNoMoreInteractions(processor1, processor2, config, adsSource);
    }

    @Test
    public void updateRanking_SetScoreAndIrrelevanceSinceWhenScoreIsGreaterThanRelevantAndPreviousIrrelevantSince() {
        Instant startingTime = Instant.now();
        Ad ad = buildCompleteAd(startingTime);
        ArrayList<Ad> allAds = Lists.newArrayList(ad);
        when(adsSource.findAll()).thenReturn(allAds);
        when(processor1.accept(any())).thenReturn(false);
        when(processor2.accept(any())).thenReturn(true);
        doAnswer(updateScore(SCORE_VALUE)).when(processor2).process(any());
        when(config.getRelevantScore()).thenReturn(SCORE_VALUE - 1);

        updateAllAdsScore.updateRanking();

        assertEquals(SCORE_VALUE, ad.getScore());
        assertNull(ad.getIrrelevantSince());
        verify(adsSource).findAll();
        verify(adsSource).updateScores(allAds);
        verify(processor1).accept(ad);
        verify(processor2).accept(ad);
        verify(processor2).process(ad);
        verify(config).getRelevantScore();
        verifyNoMoreInteractions(processor1, processor2, config, adsSource);
    }

    @Test
    public void updateRanking_SetScoreAndIrrelevanceSinceWhenScoreIsGreaterThan100AndPreviousIrrelevantSince() {
        Instant startingTime = Instant.now();
        Ad ad = buildCompleteAd(startingTime);
        ArrayList<Ad> allAds = Lists.newArrayList(ad);
        when(adsSource.findAll()).thenReturn(allAds);
        when(processor1.accept(any())).thenReturn(false);
        when(processor2.accept(any())).thenReturn(true);
        doAnswer(updateScore(150)).when(processor2).process(any());
        when(config.getRelevantScore()).thenReturn(SCORE_VALUE - 1);

        updateAllAdsScore.updateRanking();

        assertEquals(new Integer(100), ad.getScore());
        assertNull(ad.getIrrelevantSince());
        verify(adsSource).findAll();
        verify(adsSource).updateScores(allAds);
        verify(processor1).accept(ad);
        verify(processor2).accept(ad);
        verify(processor2).process(ad);
        verify(config).getRelevantScore();
        verifyNoMoreInteractions(processor1, processor2, config, adsSource);
    }


    @Test
    public void updateRanking_SetScoreAndIrrelevanceSinceWhenScoreIsBelow0() {
        Instant startingTime = Instant.now();
        Ad ad = buildCompleteAd(null);
        ArrayList<Ad> allAds = Lists.newArrayList(ad);
        when(adsSource.findAll()).thenReturn(allAds);
        when(processor1.accept(any())).thenReturn(false);
        when(processor2.accept(any())).thenReturn(true);
        doAnswer(updateScore(-15)).when(processor2).process(any());
        when(config.getRelevantScore()).thenReturn(SCORE_VALUE + 1);

        updateAllAdsScore.updateRanking();

        assertEquals(new Integer(0), ad.getScore());
        assertTrue(ad.getIrrelevantSince().isAfter(startingTime));
        verify(adsSource).findAll();
        verify(adsSource).updateScores(allAds);
        verify(processor1).accept(ad);
        verify(processor2).accept(ad);
        verify(processor2).process(ad);
        verify(config).getRelevantScore();
        verifyNoMoreInteractions(processor1, processor2, config, adsSource);
    }

    private Answer<Void> updateScore(int score) {
        return new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock arg0) throws Throwable {
                Ad.class.cast(arg0.getArgument(0)).addScore(score);
                return null;
            }
        };
    }

    private Ad buildCompleteAd(Instant Instant) {
        Ad ad = Ad.builder()
                .id(1)
                .description("description")
                .typology(AdType.FLAT)
                .houseSize(90)
                .gardenSize(5)
                .irrelevantSince(Instant)
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
