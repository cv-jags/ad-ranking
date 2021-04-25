package com.idealista.application.ranking;

import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.idealista.Main.RankingConfiguration;
import com.idealista.application.domain.Ad;

import lombok.RequiredArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Component
@RequiredArgsConstructor
public class DescriptionProcessor implements RankingProcessor {

    private static final String EMPTY_STRING = "";
    private static final Pattern NON_WORD_REGEX = Pattern.compile("[^\\w\\sáéíóú]");

    private final RankingConfiguration config;

    @Override
    public boolean accept(Ad ad) {
        return StringUtils.isNotBlank(ad.getDescription());
    }

    @Override
    public void process(Ad ad) {
        ad.addScore(config.getHasDescription());
        ad.addScore(calculateHighlightWords(ad));
    }

    private int calculateHighlightWords(Ad ad) {
        int occurrence = calculateHighlightWordsOccurrence(ad);
        return occurrence > 0 ? occurrence * config.getHighlightWordsScore() : 0;
    }

    private int calculateHighlightWordsOccurrence(Ad ad) {
        Set<String> descriptionSet = Sets.newHashSet();
        try (Scanner sc = new Scanner(cleanText(ad.getDescription()))) {
            while (sc.hasNext()) {
                descriptionSet.add(sc.next());
            }
        }
        descriptionSet.retainAll(config.getHighlightWords());
        return descriptionSet.size();
    }

    private String cleanText(String next) {
        return NON_WORD_REGEX.matcher(next).replaceAll(EMPTY_STRING).toLowerCase();
    }
}
