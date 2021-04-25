package com.idealista.application.ranking;

import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;
import com.idealista.application.domain.AdType;

import lombok.RequiredArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@RequiredArgsConstructor
public abstract class ChaletProcessor implements RankingProcessor {

    protected final RankingConfiguration config;

    @Override
    public boolean accept(Ad ad) {
        return AdType.CHALET.equals(ad.getTypology());
    }

    @Override
    public abstract void process(Ad ad);

    protected boolean notBlank(String string) {
        return StringUtils.isNotBlank(string);
    }
}
