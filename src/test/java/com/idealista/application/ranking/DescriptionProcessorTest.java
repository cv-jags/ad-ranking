package com.idealista.application.ranking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Sets;
import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;

@ExtendWith(MockitoExtension.class)
public class DescriptionProcessorTest {

    private DescriptionProcessor processor;

    @Mock
    private RankingConfiguration config;
    @Mock
    private Ad mockAd;

    @BeforeEach
    public void beforeEach() {
        processor = new DescriptionProcessor(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdHasNotDescription() {
        assertFalse(processor.accept(Ad.builder().description(null).build()));
        assertFalse(processor.accept(Ad.builder().description("").build()));
        assertFalse(processor.accept(Ad.builder().description("  ").build()));
        assertFalse(processor.accept(Ad.builder().description("  \n\t").build()));
    }

    @Test
    public void accept_ReturnsTrueWhenAdHasDescriptionText() {
        assertTrue(processor.accept(Ad.builder().description(" . ").build()));
    }

    @Test
    public void process_AddsAlwaysDescriptionScore() {
        when(config.getHasDescriptionScore()).thenReturn(5);
        when(config.getHighlightWords()).thenReturn(Sets.newHashSet("one", "two"));
        when(mockAd.getDescription()).thenReturn("on e owt");

        processor.process(mockAd);

        verify(mockAd).getId();
        verify(mockAd).addScore(5);
        verify(config).getHasDescriptionScore();
        verifyNoMoreInteractions(config, mockAd);
    }

    @Test
    public void process_AddsHighlightWordsScoreOneTimeOnly() {
        when(config.getHasDescriptionScore()).thenReturn(5);
        when(config.getHighlightWordsScore()).thenReturn(4);
        when(config.getHighlightWords()).thenReturn(Sets.newHashSet("one", "two", "áéíóú", "none"));
        when(mockAd.getDescription()).thenReturn(" one asd áéíóú asd TWo asd. asd two");

        processor.process(mockAd);

        verify(mockAd, times(2)).getId();
        verify(mockAd).addScore(5);
        verify(mockAd).addScore(12);
        verify(config).getHasDescriptionScore();
        verifyNoMoreInteractions(config, mockAd);
    }
}
