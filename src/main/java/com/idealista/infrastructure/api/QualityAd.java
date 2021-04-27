package com.idealista.infrastructure.api;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityAd {

    private Integer id;
    private String typology;
    private String description;
    @Builder.Default
    private List<String> pictureUrls = Lists.newArrayList();
    private Integer houseSize;
    private Integer gardenSize;
    private Integer score;
    private Date irrelevantSince;
}
