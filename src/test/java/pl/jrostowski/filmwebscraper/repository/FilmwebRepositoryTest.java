package pl.jrostowski.filmwebscraper.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.jrostowski.filmwebscraper.entity.Movie;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {FilmwebRepository.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmwebRepositoryTest {

    @Autowired
    FilmwebRepository filmwebRepository;
    Map<Integer, Movie> movies;
    Movie movieSample;

    @BeforeAll
    void downloadSampleData() throws IOException {
        movies = filmwebRepository.getTopList();
        movieSample = movies.get(1);
    }

    @Test
    void shouldGetMovieData() {
        assertNotNull(movieSample, "Movie object is null.");
    }

    @Test
    void shouldGet500Movies() {
        assertEquals(500, movies.size());
    }

    @Test
    void shouldNotHaveEmptyValues() {
        assertNotNull(movieSample.getTitle(), "Wrong selector for field: title.");
        assertNotNull(movieSample.getOriginalTitle(), "Wrong selector for field: originalTitle.");
        assertNotNull(movieSample.getLength(), "Wrong selector for field: length.");
        assertNotNull(movieSample.getDirector(), "Wrong selector for field: director.");
        assertNotNull(movieSample.getScreenwriter(), "Wrong selector for field: screenwriter.");
        assertNotNull(movieSample.getGenre(), "Wrong selector for field: genre.");
        assertNotNull(movieSample.getCountryOfOrigin(), "Wrong selector for field: countryOfOrigin.");
        assertNotEquals(0, movieSample.getYear(), "Wrong selector for field: year.");
        assertNotEquals(0, movieSample.getRate(), "Wrong selector for field: rate.");
        assertNotEquals(0, movieSample.getCriticsRate(), "Wrong selector for field: criticsRate.");
    }
}