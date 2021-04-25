package com.idealista.application.data;

import java.util.List;

import com.idealista.application.domain.Ad;

public interface AdsSource {

    List<Integer> updateScores(Iterable<Ad> newScores);

    List<Ad> findAll();

    List<Ad> findByScoreGreaterThanOrEqualTo(int score);
}
