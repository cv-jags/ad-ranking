package com.idealista.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;
import com.idealista.application.domain.Picture;
import com.idealista.application.domain.PictureQuality;
import com.idealista.testing.FirstArgumentAnswer;

@ExtendWith(MockitoExtension.class)
public class InMemoryPersistenceAdapterTest {

    private static final int RELEVANT_SCORE = 40;

    private InMemoryPersistenceAdapter adapter;

    @Mock
    private InMemoryPersistence persistence;

    FirstArgumentAnswer<AdVO> firstArgument = new FirstArgumentAnswer<>();

    @BeforeEach
    public void beforeEach() {
        adapter = new InMemoryPersistenceAdapter(persistence);
    }

    @Test
    public void updateScore_ThrowsExceptionWhenNewScoreAdDoesNotExist() {
        when(persistence.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> adapter.updateScore(buildUpdateScoreAd(1, 33)));

        verify(persistence).findById(1);
        verifyNoMoreInteractions(persistence);
    }

    @Test
    public void updateScore_UpdatesScoreWhenNewScoreAdDoesExist() {
        AdVO adVoToUpdate = buildAdVO(1, "FLAT", 20, null);
        when(persistence.findById(any())).thenReturn(Optional.of(adVoToUpdate));
        when(persistence.save(any())).then(firstArgument);

        adapter.updateScore(buildUpdateScoreAd(1, 33));

        verify(persistence).findById(1);
        verify(persistence).save(adVoToUpdate);
        assertEquals(new Integer(33), adVoToUpdate.getScore());
        verifyNoMoreInteractions(persistence);
    }

    @Test
    public void updateScores_ReturnsEmptyListWhenNewScoresAdDoNotExist() {
        when(persistence.findById(any())).thenReturn(Optional.empty());

        List<Integer> result = adapter.updateScores(
                Lists.newArrayList(buildUpdateScoreAd(1, 33), buildUpdateScoreAd(2, 45)));

        assertTrue(result.isEmpty());
        verify(persistence).findById(1);
        verify(persistence).findById(2);
        verifyNoMoreInteractions(persistence);
    }

    @Test
    public void updateScores_UpdatesScoresWhenNewScoreAdDoesExist() {
        AdVO adVoToUpdate = buildAdVO(1, "FLAT", 20, null);
        when(persistence.findById(any())).thenReturn(Optional.of(adVoToUpdate))
                .thenReturn(Optional.empty());
        when(persistence.save(any())).then(firstArgument);

        List<Integer> result = adapter.updateScores(
                Lists.newArrayList(buildUpdateScoreAd(1, 33), buildUpdateScoreAd(2, 50)));

        assertEquals(Lists.newArrayList(1), result);
        verify(persistence).findById(1);
        verify(persistence).findById(2);
        verify(persistence).save(adVoToUpdate);
        assertEquals(new Integer(33), adVoToUpdate.getScore());
        verifyNoMoreInteractions(persistence);
    }

    @Test
    public void findAll_ReturnsAllTransformedResultsFromPersistence() {
        Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
        AdVO adVo = buildCompleteAdVO(now);
        when(persistence.findAll()).thenReturn(Lists.newArrayList(adVo, adVo));
        when(persistence.findPictureById(any()))
                .thenReturn(Lists.newArrayList(buildPicture(1, "SD"), buildPicture(2, "HD")));

        List<Ad> result = adapter.findAll();

        Ad ad = buildCompleteAd(now);
        assertEquals(Lists.newArrayList(ad, ad), result);
        verify(persistence).findAll();
        verify(persistence, times(2)).findPictureById(Lists.newArrayList(1, 2));
        verifyNoMoreInteractions(persistence);
    }

    @Test
    public void findRelevant_ReturnsAllTransformedResultsFromPersistenceWith40OrMoreScore() {
        Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
        AdVO adVo = buildCompleteAdVO(now);
        when(persistence.findByScoreGreaterThanOrEqualTo(40)).thenReturn(Lists.newArrayList(adVo));
        when(persistence.findPictureById(any()))
                .thenReturn(Lists.newArrayList(buildPicture(1, "SD"), buildPicture(2, "HD")));

        List<Ad> result = adapter.findByScoreGreaterThanOrEqualTo(RELEVANT_SCORE);

        Ad ad = buildCompleteAd(now);
        assertEquals(Lists.newArrayList(ad), result);
        verify(persistence).findByScoreGreaterThanOrEqualTo(40);
        verify(persistence).findPictureById(Lists.newArrayList(1, 2));
        verifyNoMoreInteractions(persistence);
    }

    private Ad buildUpdateScoreAd(Integer id, Integer score) {
        return buildAd(id, null, score);
    }

    private PictureVO buildPicture(int i, String quality) {
        return PictureVO.builder().id(i).url("picture-url/" + i).quality(quality).build();
    }

    private AdVO buildAdVO(Integer id, String type, Integer score, List<Integer> pictureIds) {
        return AdVO.builder().id(id).typology(type).score(33).pictures(pictureIds).build();
    }

    private Ad buildAd(Integer id, AdType type, Integer score, Picture... pictures) {
        return Ad.builder()
                .id(id)
                .typology(type)
                .score(33)
                .pictures(Lists.newArrayList(pictures))
                .build();
    }

    private AdVO buildCompleteAdVO(Instant now) {
        AdVO adVo = AdVO.builder()
                .id(1)
                .description("description")
                .typology("FLAT")
                .houseSize(90)
                .gardenSize(5)
                .irrelevantSince(now)
                .score(50)
                .pictures(Lists.newArrayList(1, 2))
                .build();
        return adVo;
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
