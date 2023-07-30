package com.project.recommendations.services;

import com.project.recommendations.config.ApiMessageSource;
import com.project.recommendations.exceptions.BusinessException;
import com.project.recommendations.holders.Movie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.project.recommendations.holders.ApiConstants.EMPTY_GENRE;
import static com.project.recommendations.holders.ApiConstants.UNABLE_TO_FETCH_MOVIES;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class RecommendationServiceTests {
    public static final String EMPTY_GENRE_MESSAGE = "Please specify a genre";
    public static final String UNABLE_TO_FETCH_MOVIES_MESSAGE = "Unable to get Movies List";

    @Value("${movies.endpoint.url}")
    private String moviesEndpointURL;
    private ResponseEntity<Movie[]> responseEntity;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ApiMessageSource apiMessageSource;
    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    @Before
    public void init(){
        Movie movie1 = Movie.builder()
                .id(1L)
                .title("The Shawshank Redemption")
                .genre("Drama")
                .releaseYear(1994)
                .director("Frank Darabont")
                .build();
        Movie movie2 = Movie.builder()
                .id(1L)
                .title("The Godfather")
                .genre("Drama")
                .releaseYear(1972)
                .director("Francis Ford Coppola")
                .build();
        Movie movie3 = Movie.builder()
                .id(3L)
                .title("The Dark Knight")
                .genre("Action")
                .releaseYear(2008)
                .director("Christopher Nolan")
                .build();

        Movie[] movies = {movie1, movie2, movie3};
        responseEntity = new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Test
    public void getMovieRecommendationsWithEmptyGenre() {
        //PREPARATION
        Mockito.when(apiMessageSource.getMessage(EMPTY_GENRE)).thenReturn(EMPTY_GENRE_MESSAGE);

        //ACTION
        //ASSERTION
        BusinessException businessException = assertThrows(BusinessException.class, () -> recommendationService.getMovieRecommendationsByGenre(""));
        assertEquals(EMPTY_GENRE_MESSAGE, businessException.getMessage());

    }

    @Test
    public void getMovieRecommendationsWithNullGenre() {
        //PREPARATION
        Mockito.when(apiMessageSource.getMessage(EMPTY_GENRE)).thenReturn(EMPTY_GENRE_MESSAGE);

        //ACTION
        //ASSERTION
        BusinessException businessException = assertThrows(BusinessException.class, () -> recommendationService.getMovieRecommendationsByGenre(null));
        assertEquals(EMPTY_GENRE_MESSAGE, businessException.getMessage());
    }

    @Test
    public void getMovieRecommendationsWithGenre() throws BusinessException {
        //PREPARATION
        Mockito.when(restTemplate.getForEntity(moviesEndpointURL, Movie[].class)).thenReturn(responseEntity);
        Movie[] movies = responseEntity.getBody();
        assert movies != null;

        //ACTION
        List<Movie> dramaMovies = recommendationService.getMovieRecommendationsByGenre("Drama");

        //ASSERTION
        assertEquals(2,dramaMovies.size());
        assertEquals(movies[0].getTitle(),dramaMovies.get(0).getTitle());
        assertEquals(movies[1].getTitle(),dramaMovies.get(1).getTitle());
        assertTrue(dramaMovies.get(0).getReleaseYear() > dramaMovies.get(1).getReleaseYear());
    }

    @Test
    public void getMovieRecommendationsWithGenreWithEmptyResponse() throws BusinessException {
        //PREPARATION
        Mockito.when(restTemplate.getForEntity(moviesEndpointURL, Movie[].class)).thenReturn(responseEntity);

        //ACTION
        List<Movie> dramaMovies = recommendationService.getMovieRecommendationsByGenre("Comedy");

        //ASSERTION
        assertEquals(0,dramaMovies.size());
    }

    @Test
    public void getMovieRecommendationsWithGenreWithSpaces() throws BusinessException {
        //PREPARATION
        Mockito.when(restTemplate.getForEntity(moviesEndpointURL, Movie[].class)).thenReturn(responseEntity);
        Movie[] movies = responseEntity.getBody();
        assert movies != null;

        //ACTION
        List<Movie> dramaMovies = recommendationService.getMovieRecommendationsByGenre("Dra m a   ");

        //ASSERTION
        assertEquals(2,dramaMovies.size());
        assertEquals(movies[0].getTitle(),dramaMovies.get(0).getTitle());
        assertEquals(movies[1].getTitle(),dramaMovies.get(1).getTitle());
        assertTrue(dramaMovies.get(0).getReleaseYear() > dramaMovies.get(1).getReleaseYear());
    }

    @Test
    public void getMovieRecommendationsWithGenreTechnicalRestTemplateException() {
        //PREPARATION
        Mockito.when(restTemplate.getForEntity(moviesEndpointURL, Movie[].class)).thenThrow(new RestClientException("Error"));
        Mockito.when(apiMessageSource.getMessage(UNABLE_TO_FETCH_MOVIES)).thenReturn(UNABLE_TO_FETCH_MOVIES_MESSAGE);

        //ACTION
        //ASSERTION
        BusinessException businessException = assertThrows(BusinessException.class, () -> recommendationService.getMovieRecommendationsByGenre("Drama"));
        assertEquals(UNABLE_TO_FETCH_MOVIES_MESSAGE, businessException.getMessage());

    }

    @Test
    public void getMovieRecommendationsWithGenreNullBodyFromRestTemplate() {
        //PREPARATION
        Mockito.when(restTemplate.getForEntity(moviesEndpointURL, Movie[].class)).thenReturn(new ResponseEntity<>(null,HttpStatus.OK));
        Mockito.when(apiMessageSource.getMessage(UNABLE_TO_FETCH_MOVIES)).thenReturn(UNABLE_TO_FETCH_MOVIES_MESSAGE);

        //ACTION
        //ASSERTION
        BusinessException businessException = assertThrows(BusinessException.class, () -> recommendationService.getMovieRecommendationsByGenre("Drama"));
        assertEquals(UNABLE_TO_FETCH_MOVIES_MESSAGE, businessException.getMessage());


    }
}
