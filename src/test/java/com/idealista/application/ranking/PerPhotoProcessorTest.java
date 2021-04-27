package com.idealista.application.ranking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
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
import com.idealista.application.domain.PictureQuality;

@ExtendWith(MockitoExtension.class)
public class PerPhotoProcessorTest {

    private PerPhotoProcessor processor;

    @Mock
    private RankingConfiguration config;
    @Mock
    private Ad mockAd;

    @BeforeEach
    public void beforeEach() {
        processor = new PerPhotoProcessor(config);
    }

    @Test
    public void accept_ReturnsFalseWhenNoPictures() {
        assertFalse(processor.accept(Ad.builder().pictures(null).build()));
        assertFalse(processor.accept(Ad.builder().pictures(Collections.emptyList()).build()));
    }

    @Test
    public void accept_ReturnsTrueWhenPictures() {
        assertTrue(processor.accept(
                Ad.builder().pictures(Lists.newArrayList(Picture.builder().build())).build()));
    }

    @Test
    public void process_AddsScoreWithConfigValue() {
        when(config.getSdPhotoScore()).thenReturn(10);
        when(config.getHdPhotoScore()).thenReturn(20);
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(
                buildPicture(PictureQuality.SD),
                buildPicture(PictureQuality.HD),
                buildPicture(PictureQuality.SD),
                buildPicture(null)));

        processor.process(mockAd);

        verify(mockAd).getId();
        verify(mockAd).addScore(50);
        verify(config, times(3)).getSdPhotoScore();
        verify(config, times(1)).getHdPhotoScore();
        verifyNoMoreInteractions(config, mockAd);
    }

    private Picture buildPicture(PictureQuality quality) {
        return Picture.builder().quality(quality).build();
    }
}
