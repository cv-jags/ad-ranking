package com.idealista.application.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ad {

    private Integer id;
    private AdType typology;
    private String description;
    private List<Picture> pictures;
    private Integer houseSize;
    private Integer gardenSize;
    private Integer score;
    private Instant irrelevantSince;

    public void addScore(int puntuation) {
        score = Objects.isNull(score) ? puntuation : (score + puntuation);
    }
}
