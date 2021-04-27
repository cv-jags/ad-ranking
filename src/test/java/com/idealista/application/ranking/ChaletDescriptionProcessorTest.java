package com.idealista.application.ranking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;

@ExtendWith(MockitoExtension.class)
public class ChaletDescriptionProcessorTest {

    private static final String ONE_TWO_THREE = "one two. threé!";

    private ChaletDescriptionProcessor processor;

    @Mock
    private RankingConfiguration config;
    @Mock
    private Ad mockAd;

    @BeforeEach
    public void beforeEach() {
        processor = new ChaletDescriptionProcessor(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdIsNotChalet() {
        assertFalse(processor.accept(Ad.builder().typology(null).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.GARAGE).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.FLAT).build()));
    }

    @Test
    public void accept_ReturnsFalseWhenChaletDescriptionIsNull() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn(null);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verifyNoMoreInteractions(mockAd, config);
    }

    @Test
    public void accept_ReturnsFalseWhenChaletDescriptionIsShort() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn(ONE_TWO_THREE);
        when(config.getChaletLargeDescriptionMin()).thenReturn(4);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verify(config).getChaletLargeDescriptionMin();
        verifyNoMoreInteractions(mockAd, config);
    }

    @Test
    public void accept_ReturnsTrueWhenChaletDescriptionIsEnough() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn(ONE_TWO_THREE);
        when(config.getChaletLargeDescriptionMin()).thenReturn(3);

        boolean result = processor.accept(mockAd);

        assertTrue(result);
        verify(mockAd).getDescription();
        verify(config).getChaletLargeDescriptionMin();
        verifyNoMoreInteractions(mockAd, config);
    }

    @Test
    public void accept_ReturnsTrueWhenChaletDescriptionIsGreater() {
        when(mockAd.getTypology()).thenReturn(AdType.CHALET);
        when(mockAd.getDescription()).thenReturn(ONE_TWO_THREE);
        when(config.getChaletLargeDescriptionMin()).thenReturn(2);

        boolean result = processor.accept(mockAd);

        assertTrue(result);
        verify(mockAd).getDescription();
        verify(config).getChaletLargeDescriptionMin();
        verifyNoMoreInteractions(mockAd, config);
    }

    @Test
    public void process_AddsScoreFromConfig() {
        when(config.getChaletLargeDescriptionScore()).thenReturn(20);

        processor.process(mockAd);

        verify(mockAd).getId();
        verify(mockAd).addScore(20);
        verify(config).getChaletLargeDescriptionScore();
        verifyNoMoreInteractions(mockAd, config);
    }
}