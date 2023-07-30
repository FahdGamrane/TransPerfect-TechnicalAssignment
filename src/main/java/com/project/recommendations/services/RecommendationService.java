package com.project.recommendations.services;

import com.project.recommendations.exceptions.BusinessException;
import com.project.recommendations.holders.Movie;

import java.util.List;

public interface RecommendationService {

    public List<Movie> getMovieRecommendationsByGenre(String genre) throws BusinessException;

}
