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
public class GarageProcessorTest {

    private GarageProcessor processor;

    @Mock
    private RankingConfiguration config;
    @Mock
    private Ad mockAd;

    @BeforeEach
    public void beforeEach() {
        processor = new GarageProcessor(config);
    }

    @Test
    public void accept_ReturnsFalseWhenAdIsNotChalet() {
        assertFalse(processor.accept(Ad.builder().typology(null).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.CHALET).build()));
        assertFalse(processor.accept(Ad.builder().typology(AdType.FLAT).build()));
    }

    @Test
    public void accept_ReturnsTrueWhenAdHasDescriptionText() {
        assertTrue(processor.accept(Ad.builder().typology(AdType.GARAGE).build()));
    }

    @Test
    public void process_DoesNotAddsScoreWhenGarageAndPicturesIsNull() {

        processor.process(mockAd);

        verify(mockAd).getPictures();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void process_DoesNotAddsScoreWhenGarageDoesNotHaveAnyPicture() {
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList());

        processor.process(mockAd);

        verify(mockAd).getPictures();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void process_DoesNotAddsScoreWhenGarageDoesNotHaveSize() {
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));

        processor.process(mockAd);

        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void process_DoesNotAddsScoreWhenGarageHave0Size() {
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));
        when(mockAd.getHouseSize()).thenReturn(0);

        processor.process(mockAd);

        verify(mockAd).getPictures();
        verify(mockAd).getHouseSize();
        verifyNoMoreInteractions(mockAd);
        verifyNoInteractions(config);
    }

    @Test
    public void process_AddsCompleteScoreWhenGarageIsComplete() {
        when(config.getGarageCompleteScore()).thenReturn(40);
        when(mockAd.getHouseSize()).thenReturn(15);
        when(mockAd.getPictures()).thenReturn(Lists.newArrayList(Picture.builder().build()));

        processor.process(mockAd);

        verify(mockAd).addScore(40);
        verify(config).getGarageCompleteScore();
        verifyNoMoreInteractions(config, mockAd);
    }
}
