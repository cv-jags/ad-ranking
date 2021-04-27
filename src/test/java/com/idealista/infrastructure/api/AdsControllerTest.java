package com.idealista.infrastructure.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.collect.Lists;

@ExtendWith(MockitoExtension.class)
public class AdsControllerTest {

    private AdsController controller;

    @Mock
    private AdService adService;

    @BeforeEach
    public void beforeEach() {
        controller = new AdsController(adService);
    }

    @Test
    public void qualityListing_ReturnsOkWithQualityAdsFromAdService() {
        ArrayList<QualityAd> qualityAds = Lists.newArrayList(QualityAd.builder().id(1).build());
        when(adService.getQualityAds()).thenReturn(qualityAds);

        ResponseEntity<List<QualityAd>> result = controller.qualityListing();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(qualityAds, result.getBody());
        verify(adService).getQualityAds();
        verifyNoMoreInteractions(adService);
    }

    @Test
    public void publicListing_ReturnsOkWithPublicAdsFromAdService() {
        ArrayList<PublicAd> publicAds = Lists.newArrayList(PublicAd.builder().id(1).build());
        when(adService.getPublicAds()).thenReturn(publicAds);

        ResponseEntity<List<PublicAd>> result = controller.publicListing();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(publicAds, result.getBody());
        verify(adService).getPublicAds();
        verifyNoMoreInteractions(adService);
    }

    @Test
    public void calculateScore_ReturnsOkAndTriggerAdService() {

        ResponseEntity<Void> result = controller.calculateScore();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(null, result.getBody());
        verify(adService).triggerScoreUpdate();
        verifyNoMoreInteractions(adService);
    }
}
