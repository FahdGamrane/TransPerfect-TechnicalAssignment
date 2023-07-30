package com.project.recommendations.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static com.project.recommendations.holders.ApiConstants.MOVIE_BY_TYPE;

@EnableCaching
@EnableScheduling
@Configuration
public class ApplicationConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    @Value("${rest.template.timeout}")
    private int restTemplateTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(restTemplateTimeout))
                .setReadTimeout(Duration.ofMillis(restTemplateTimeout))
                .build();
    }

    @CacheEvict(allEntries = true, value = {MOVIE_BY_TYPE})
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 500)
    public void reportCacheEvict() {
        LOGGER.info("Flush cache of " + MOVIE_BY_TYPE);
    }

}
