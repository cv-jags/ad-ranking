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
public class FlatLargeDescriptionProcessorTest {

    private static final String ONE_TWO_THREE = "one two. threé!";

    private FlatLargeDescriptionProcessor processor;

    @Mock
    private RankingConfiguration config;
    @Mock
    private Ad mockAd;

    @BeforeEach
    public void beforeEach() {
        processor = new FlatLargeDescriptionProcessor(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdIsNotFlat() {
        assertFalse(processor.accept(Ad.builder().typology(null).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.GARAGE).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.CHALET).build()));
    }

    @Test
    public void accept_ReturnsFalseWhenDescriptionIsNull() {
        when(mockAd.getTypology()).thenReturn(AdType.FLAT);
        when(mockAd.getDescription()).thenReturn(null);
        when(config.getFlatLargeDescriptionMin()).thenReturn(4);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verifyNoMoreInteractions(mockAd, config);
    }

    @Test
    public void accept_ReturnsFalseWhenDescriptionIsShort() {
        when(mockAd.getTypology()).thenReturn(AdType.FLAT);
        when(mockAd.getDescription()).thenReturn(ONE_TWO_THREE);
        when(config.getFlatLargeDescriptionMin()).thenReturn(4);

        boolean result = processor.accept(mockAd);

        assertFalse(result);
        verify(mockAd).getDescription();
        verify(config).getFlatLargeDescriptionMin();
        verifyNoMoreInteractions(mockAd, config);
    }

    @Test
    public void accept_ReturnsTrueWhenDescriptionIsEnough() {
        when(mockAd.getTypology()).thenReturn(AdType.FLAT);
        when(mockAd.getDescription()).thenReturn(ONE_TWO_THREE);
        when(config.getFlatLargeDescriptionMin()).thenReturn(3);

        boolean result = processor.accept(mockAd);

        assertTrue(result);
        verify(mockAd).getDescription();
        verify(config).getFlatLargeDescriptionMin();
        verifyNoMoreInteractions(mockAd, config);
    }

    @Test
    public void accept_ReturnsTrueWhenDescriptionIsGreater() {
        when(mockAd.getTypology()).thenReturn(AdType.FLAT);
        when(mockAd.getDescription()).thenReturn(ONE_TWO_THREE);
        when(config.getFlatLargeDescriptionMin()).thenReturn(2);

        boolean result = processor.accept(mockAd);

        assertTrue(result);
        verify(mockAd).getDescription();
        verify(config).getFlatLargeDescriptionMin();
        verifyNoMoreInteractions(mockAd, config);
    }

    @Test
    public void process_AddsScoreFromConfig() {
        when(config.getFlatLargeDescriptionScore()).thenReturn(50);

        processor.process(mockAd);

        verify(mockAd).getId();
        verify(mockAd).addScore(50);
        verify(config).getFlatLargeDescriptionScore();
        verifyNoMoreInteractions(mockAd, config);
    }
}