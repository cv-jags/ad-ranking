package com.idealista.infrastructure.persistence;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdVO implements Cloneable, Serializable {

    private static final long serialVersionUID = 8315670645925437214L;

    private Integer id;
    private String typology;
    private String description;
    private List<Integer> pictures;
    private Integer houseSize;
    private Integer gardenSize;
    private Integer score;
    private Instant irrelevantSince;

    public AdVO clone() {
        try {
            return (AdVO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
