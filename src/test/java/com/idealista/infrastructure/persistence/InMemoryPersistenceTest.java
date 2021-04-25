package com.idealista.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;

public class InMemoryPersistenceTest {

    private InMemoryPersistence persistence;

    private List<AdVO> adsStore;

    @Captor
    private ArgumentCaptor<AdVO> adVoCaptor;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void beforeEach() {
        persistence = new InMemoryPersistence();
        adsStore = (List<AdVO>) ReflectionTestUtils.getField(persistence, "ads");
    }

    @Test
    public void save_AddsANewClonedElementIfIdIsNotPresentAndReturnsTheSame() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        AdVO newAdVo = buildCompleteAdVO(10, now);

        AdVO result = persistence.save(newAdVo);

        assertSame(newAdVo, result);
        assertEquals(9, adsStore.size());
        assertEquals(newAdVo, adsStore.get(8));
        assertNotSame(result, adsStore.get(8));
    }

    @Test
    public void save_ReplaceWithClonedElementIfIdIsPresentAndReturnsTheSame() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        AdVO newAdVo = buildCompleteAdVO(1, now);

        AdVO result = persistence.save(newAdVo);

        assertSame(newAdVo, result);
        assertEquals(8, adsStore.size());
        assertEquals(newAdVo, adsStore.get(7));
        assertNotSame(result, adsStore.get(7));
    }

    @Test
    void findById_ReturnsEmptyIfIdIsNotPresent() {

        assertEquals(Optional.empty(), persistence.findById(15));

    }

    @Test
    void findById_ReturnsElementIfIdIsIsPresent() {
        AdVO existingAdVO = buildExistingAdVO();

        Optional<AdVO> result = persistence.findById(existingAdVO.getId());

        assertEquals(Optional.of(existingAdVO), result);
        assertNotSame(existingAdVO, result.get());
    }

    @Test
    void findAll_ReturnsPresentElements() {

        List<AdVO> result = persistence.findAll();

        for (int i = 0; i < result.size(); i++) {
            assertEquals(adsStore.get(i), result.get(i));
            assertNotSame(adsStore.get(i), result.get(i));
        }
    }

    @Test
    void findByScoreGreaterThanOrEqual_ReturnsEmptyIfNoScore() {

        List<AdVO> result = persistence.findByScoreGreaterThanOrEqual(10);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByScoreGreaterThanOrEqual_ReturnsElementsWithEqualAndGreaterScores() {
        adsStore.get(1).setScore(40);
        adsStore.get(7).setScore(80);

        List<AdVO> result = persistence.findByScoreGreaterThanOrEqual(10);

        assertEquals(2, result.size());
        assertEquals(adsStore.get(1), result.get(0));
        assertNotSame(adsStore.get(1), result.get(0));
        assertEquals(adsStore.get(7), result.get(1));
        assertNotSame(adsStore.get(7), result.get(1));
    }

    @Test
    void findPicturesById_ReturnsEmptyIfNoId() {

        assertTrue(persistence.findPictureById(Lists.newArrayList()).isEmpty());
        assertTrue(persistence.findPictureById(Lists.newArrayList(9, 10, 15)).isEmpty());

    }

    @Test
    void findPicturesById_ReturnsPicturesWithIdsOrderedByPicturesOrder() {
        ArrayList<Integer> ids = Lists.newArrayList(5, 9, 3);
        @SuppressWarnings("unchecked")
        List<PictureVO> pictures = (List<PictureVO>) ReflectionTestUtils.getField(persistence,
                "pictures");

        List<PictureVO> result = persistence.findPictureById(ids);

        assertEquals(2, result.size());
        assertEquals(2, result.size());
        assertEquals(pictures.get(2), result.get(0));
        assertNotSame(pictures.get(2), result.get(0));
        assertEquals(pictures.get(4), result.get(1));
        assertNotSame(pictures.get(4), result.get(1));
    }

    private AdVO buildCompleteAdVO(Integer id, Timestamp now) {
        AdVO adVo = AdVO.builder()
                .id(id)
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

    private AdVO buildExistingAdVO() {
        AdVO adVo = AdVO.builder()
                .id(5)
                .description("Pisazo,")
                .typology("FLAT")
                .pictures(Lists.newArrayList(3, 8))
                .houseSize(300)
                .build();
        return adVo;
    }
}
