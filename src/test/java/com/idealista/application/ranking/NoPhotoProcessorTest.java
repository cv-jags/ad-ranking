package com.idealista.application.ranking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.Picture;

@ExtendWith(MockitoExtension.class)
public class NoPhotoProcessorTest {

    private NoPhotoProcessor processor;

    @Mock
    private RankingConfiguration config;
    @Mock
    private Ad mockAd;

    @BeforeEach
    public void beforeEach() {
        processor = new NoPhotoProcessor(config);
    }

    @Test
    public void accept_ReturnsFalseWhenPictures() {
        assertFalse(processor.accept(
                Ad.builder().pictures(Lists.newArrayList(Picture.builder().build())).build()));
    }

    @Test
    public void accept_ReturnsTrueWhenNoPictures() {
        assertTrue(processor.accept(Ad.builder().pictures(null).build()));
        assertTrue(processor.accept(Ad.builder().pictures(Collections.emptyList()).build()));
    }

    @Test
    public void process_AddsScoreWithConfigValue() {
        when(config.getNoPhotoScore()).thenReturn(-10);

        processor.process(mockAd);

        verify(mockAd).getId();
        verify(mockAd).addScore(-10);
        verify(config).getNoPhotoScore();
        verifyNoMoreInteractions(config, mockAd);
    }
}
