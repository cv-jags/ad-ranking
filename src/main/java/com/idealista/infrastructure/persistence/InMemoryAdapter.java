package com.idealista.infrastructure.persistence;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import com.idealista.application.data.AdsSource;
import com.idealista.application.domain.Ad;

@Component
public class InMemoryAdapter implements AdsSource {

    private InMemoryPersistence persistence;

    @Override
    public Ad persist(Ad ad) {
        return transformFrom(persistence.save(transformTo(ad)));
    }

    @Override
    public Iterable<Ad> persistAll(Iterable<Ad> ads) {
        return transformFrom(persistence.saveAll(transformTo(ads)));
    }

    @Override
    public List<Ad> findAll() {
        return transformFrom(persistence.findAll());
    }

    @Override
    public List<Ad> findRelevant() {
        return transformFrom(persistence.findByScoreGreaterThan(40));
    }

    private List<AdVO> transformTo(Iterable<Ad> ads) {
        return StreamSupport.stream(ads.spliterator(), false)
                .map(this::transformTo)
                .collect(Collectors.toList());
    }

    private List<Ad> transformFrom(Iterable<AdVO> ads) {
        return StreamSupport.stream(ads.spliterator(), false)
                .map(this::transformFrom)
                .collect(Collectors.toList());
    }

    private AdVO transformTo(Ad ad) {
        // TODO Auto-generated method stub
        return null;
    }

    private Ad transformFrom(AdVO ad) {
        // TODO Auto-generated method stub
        return null;
    }
}
