package com.project.recommendations.services;

import com.project.recommendations.exceptions.BusinessException;
import com.project.recommendations.holders.Movie;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.project.recommendations.holders.ApiConstants.EMPTY_GENRE;
import static com.project.recommendations.holders.ApiConstants.UNABLE_TO_FETCH_MOVIES;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    @Value("${movies.endpoint.url}")
    private String moviesEndpointURL;

    private final RestTemplate restTemplate;

    @Override
    public List<Movie> getMovieRecommendationsByGenre(String genre) throws BusinessException {
        LOGGER.info("Getting movies from /movies by genre : [{}]",genre);
        if(genre == null || genre.isEmpty()){
            LOGGER.error("Genre is empty");
            throw new BusinessException(HttpStatus.BAD_REQUEST, EMPTY_GENRE);
        }
        String formattedGenre = genre.replaceAll("\\s+", "");
        List<Movie> moviesList = getMovies();
        return moviesList.stream()
                .filter(movie -> movie.getGenre().equalsIgnoreCase(formattedGenre))
                .sorted(Comparator.comparing(Movie::getReleaseYear).reversed())
                .toList();
    }

    protected List<Movie> getMovies() throws BusinessException {
        ResponseEntity<Movie[]> responseEntity;
        try {
            responseEntity = restTemplate
                    .getForEntity(moviesEndpointURL, Movie[].class);
        }catch (RestClientException exception) {
            LOGGER.error("Unable to fetch movies",exception);
            throw new BusinessException(HttpStatus.BAD_REQUEST,UNABLE_TO_FETCH_MOVIES);
        }
        if (responseEntity.getBody() == null){
            throw new BusinessException(HttpStatus.BAD_REQUEST,UNABLE_TO_FETCH_MOVIES);
        }
        return Arrays.stream(responseEntity.getBody()).toList();
    }
}
