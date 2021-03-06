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
public class ChaletCompleteProcessorTest {

    private ChaletCompleteProcessor processor;

    @Mock
    private RankingConfiguration config;
    @Mock
    private Ad mockAd;

    @BeforeEach
    public void beforeEach() {
        processor = new ChaletCompleteProcessor(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdIsNotChalet() {
        assertFalse(processor.accept(Ad.builder().typology(null).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.GARAGE).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.FLAT).build()));
    }

    @Test
    public void accept_ReturnsFalseWhenDescriptionIsNull() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn(null);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenDescriptionIsEmpty() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn("");

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdDescriptionHasTextButPicturesIsNull() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn("description");
        when(mockAd.getPictures()).thenReturn(null);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verify(mockAd).getPictures();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdDescriptionHasTextButPicturesIsEmpty() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn("description");
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList());

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verify(mockAd).getPictures();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdDescriptionHasTextAndPicturesButHouseSizeAndGardenSizeIsNull() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn("description");
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));
        when(mockAd.getHouseSize()).thenReturn(null);
        when(mockAd.getGardenSize()).thenReturn(null);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verify(mockAd).getGardenSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdDescriptionHasTextAndPicturesAndHouseSizeIsNotNullButGardenSizeIsNull() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn("description");
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));
        when(mockAd.getHouseSize()).thenReturn(120);
        when(mockAd.getGardenSize()).thenReturn(null);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verify(mockAd).getGardenSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdDescriptionHasTextAndPicturesAndGardenSizeIsNotNullButHouseSizeIsNull() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn("description");
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));
        when(mockAd.getHouseSize()).thenReturn(null);
        when(mockAd.getGardenSize()).thenReturn(15);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verify(mockAd).getGardenSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdDescriptionHasTextAndPicturesAndGardenSizeAndHouseSizeIs0() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn("description");
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));
        when(mockAd.getHouseSize()).thenReturn(0);
        when(mockAd.getGardenSize()).thenReturn(0);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verify(mockAd).getGardenSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void accept_ReturnsTrueWhenAdDescriptionHasTextAndPicturesAndGardenSizeAndHouseSizeIsGreaterThan0() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn("description");
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));
        when(mockAd.getHouseSize()).thenReturn(150);
        when(mockAd.getGardenSize()).thenReturn(20);

        boolean result = processor.accept(mockAd);

        assertTrue(result);
        verify(mockAd).getDescription();
        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verify(mockAd).getGardenSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void process_AddsCompleteScoreWhenChaletIsComplete() {
        when(config.getChaletCompleteScore()).thenReturn(40);

        processor.process(mockAd);

        verify(mockAd).getId();
        verify(mockAd).addScore(40);
        verify(config).getChaletCompleteScore();
        verifyNoMoreInteractions(config, mockAd);
    }
}