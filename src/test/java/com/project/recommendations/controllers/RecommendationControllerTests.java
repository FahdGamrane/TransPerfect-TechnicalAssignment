package com.project.recommendations.controllers;

import com.project.recommendations.holders.Movie;
import com.project.recommendations.services.RecommendationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.project.recommendations.holders.ApiPaths.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RecommendationController.class)
public class RecommendationControllerTests {
    private final List<Movie> movies = new ArrayList<>();
    @MockBean
    private RecommendationService recommendationService;
    @Autowired
    private MockMvc mockMvc;

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

        movies.add(movie1);
        movies.add(movie2);
    }

    @After
    public void teardown(){
        movies.clear();
    }

    @Test
    public void getRecommendations() throws Exception {
        //PREPARATION
        String testGenre = "Drama";
        Mockito.when(recommendationService.getMovieRecommendationsByGenre(testGenre)).thenReturn(movies);

        //ACTION & ASSERTION

        mockMvc.perform(get(VERSION_1 + MOVIES + RECOMMENDATIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("genre",testGenre)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("[0].title").value(movies.get(0).getTitle()))
                .andExpect(jsonPath("[1].title").value(movies.get(1).getTitle()));

    }

    @Test
    public void getRecommendationsWithNoResult() throws Exception {
        //PREPARATION
        String testGenre = "Drama";
        Mockito.when(recommendationService.getMovieRecommendationsByGenre(testGenre)).thenReturn(new ArrayList<>());

        //ACTION & ASSERTION

        mockMvc.perform(get(VERSION_1 + MOVIES + RECOMMENDATIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("genre",testGenre)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));

    }

    @Test
    public void getRecommendationsWithNoGenre() throws Exception {
        //PREPARATION
        String testGenre = "Drama";
        Mockito.when(recommendationService.getMovieRecommendationsByGenre(testGenre)).thenReturn(movies);

        //ACTION & ASSERTION

        mockMvc.perform(get(VERSION_1 + MOVIES + RECOMMENDATIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());

    }
}
