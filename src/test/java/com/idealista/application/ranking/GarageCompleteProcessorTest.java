package com.idealista.application.ranking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;
import com.idealista.application.domain.Picture;

@ExtendWith(MockitoExtension.class)
public class GarageCompleteProcessorTest {


    private GarageCompleteProcessor processor;

    @Mock
    private RankingConfiguration config;
    @Mock
    private Ad mockAd;

    @BeforeEach
    public void beforeEach() {
        processor = new GarageCompleteProcessor(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdIsNotGarage() {
        assertFalse(processor.accept(Ad.builder().typology(null).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.CHALET).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.FLAT).build()));
    }


    @Test
    public void accept_ReturnsFalseWhenPicturesIsNull() {
        when(mockAd.getTypology()).thenReturn(AdType.GARAGE);
        when(mockAd.getPictures()).thenReturn(null);


        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getPictures();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenPicturesIsEmpty() {
        when(mockAd.getTypology()).thenReturn(AdType.GARAGE);
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList());

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getPictures();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdHasPicturesButHouseSizeIsNull() {
        when(mockAd.getTypology()).thenReturn(AdType.GARAGE);
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));
        when(mockAd.getHouseSize()).thenReturn(null);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdHasPicturesAndHouseSizeIs0() {
        when(mockAd.getTypology()).thenReturn(AdType.GARAGE);
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));
        when(mockAd.getHouseSize()).thenReturn(0);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsTrueWhenAdHasPicturesAndHouseSizeIsGreaterThan0() {
        when(mockAd.getTypology()).thenReturn(AdType.GARAGE);
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));
        when(mockAd.getHouseSize()).thenReturn(150);

        boolean result = processor.accept(mockAd);

        assertTrue(result);
        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void process_AddsCompleteScoreWhenIsComplete() {
        when(config.getGarageCompleteScore()).thenReturn(40);

        processor.process(mockAd);

        verify(mockAd).getId();
        verify(mockAd).addScore(40);
        verify(config).getGarageCompleteScore();
        verifyNoMoreInteractions(config, mockAd);
    }
}
