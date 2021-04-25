package com.idealista.application.ranking;

import java.util.StringTokenizer;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;

import lombok.RequiredArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@RequiredArgsConstructor
public abstract class FlatProcessor implements RankingProcessor {

    protected final RankingConfiguration config;

    @Override
    public boolean accept(Ad ad) {
        return AdType.FLAT.equals(ad.getTypology());
    }

    @Override
    public abstract void process(Ad ad);

    protected int countWords(String description) {
        return isNotBlank(description) ? new StringTokenizer(description).countTokens() : 0;
    }

    protected boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }
}
