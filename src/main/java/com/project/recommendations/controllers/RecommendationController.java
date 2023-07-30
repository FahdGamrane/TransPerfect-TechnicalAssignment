package com.project.recommendations.controllers;

import com.project.recommendations.exceptions.BusinessException;
import com.project.recommendations.holders.Movie;
import com.project.recommendations.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.project.recommendations.holders.ApiPaths.*;

@RestController
@RequestMapping(VERSION_1)
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping(MOVIES + RECOMMENDATIONS)
    public List<Movie> getRecommendations(@NonNull @RequestParam String genre) throws BusinessException {
        return recommendationService.getMovieRecommendationsByGenre(genre);
    }

}
