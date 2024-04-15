package com.vrrom.euribor;

import com.vrrom.service.EuriborService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableCaching
public class EuriborServiceTest {

    @Autowired
    private EuriborService euriborService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testFetchEuriborRates_Caching() {

        String term = "6m";
        String firstResult = euriborService.fetchEuriborRates(term).block();
        String cachedResult = cacheManager.getCache("euriborRates").get(term, String.class);
        assertEquals(firstResult, cachedResult, "Cached data should match the first result");
        String secondResult = euriborService.fetchEuriborRates(term).block();
        assertEquals(firstResult, secondResult, "Second result should be fetched from cache and match the first result");

    }
}
