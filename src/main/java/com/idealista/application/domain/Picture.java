package com.idealista.application.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture {

    private Integer id;
    private String url;
    private PictureQuality quality;
}
