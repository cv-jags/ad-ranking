package com.idealista;

import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Sets;

import lombok.Getter;
import lombok.Setter;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        configureSystem();
        SpringApplication.run(Main.class, args);
    }

    /**
     * Make sure the JVM is using UTC in Date
     */
    private static void configureSystem() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "add.ranking")
    public static class RankingConfiguration {

        private int relevantScore = 40;
        private int noPhotoScore = -10;
        private int sdPhotoScore = 10;
        private int hdPhotoScore = 20;
        private int hasDescription = 5;
        private int highlightWordsScore = 5;
        private int flatMediumDescription = 10;
        private int flatLargeDescription = 30;
        private int flatComplete = 40;
        private int chaletLargeDescription = 20;
        private int chaletComplete = 40;
        private int garageComplete = 40;
        private Set<String> highlightWords = Sets.newHashSet("Luminoso", "Nuevo", "Céntrico",
                "Reformado", "Ático");

        @PostConstruct
        public void setHighlightWordsLowerCase() {
            highlightWords = highlightWords.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        }
    }

}