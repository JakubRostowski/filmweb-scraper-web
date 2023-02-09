package pl.jrostowski.filmwebscraper.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import pl.jrostowski.filmwebscraper.BaseDatabaseTest;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.repository.MovieRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class MovieControllerIT extends BaseDatabaseTest {

    @Autowired
    MovieRepository movieRepository;

    @Test
    void shouldReturnSingleMovie() throws Exception {
        Movie movie = new Movie(1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Test country", "Test poster");
        movieRepository.save(movie);

        perform(get("/movies/" + movie.getMovieId()))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-info"));

        movieRepository.deleteAll();
    }

    @Test
    void shouldNotReturnSingleMovieAndRedirect() throws Exception {
        perform(get("/movies/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldReturnActiveMovies() throws Exception {
        Movie movie = new Movie(1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Test country", "Test poster");
        movieRepository.save(movie);

        perform(get("/top500/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies-list"));

        movieRepository.deleteAll();
    }

    @Test
    void shouldNotReturnActiveMovies() throws Exception {
        Movie movie = new Movie(-1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Test country", "Test poster");
        movieRepository.save(movie);

        MvcResult result = perform(get("/top500/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies-list"))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("No data at the moment."));

        movieRepository.delete(movie);
    }

    @Test
    void shouldRedirectTop500() throws Exception {
        perform(get("/top500"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldReturnInactiveMovies() throws Exception {
        Movie movie = new Movie(-1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Test country", "Test poster");
        movieRepository.save(movie);

        perform(get("/inactive-movies/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies-list"));

        movieRepository.delete(movie);
    }

    @Test
    void shouldNotReturnInactiveMovies() throws Exception {
        Movie movie = new Movie(1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Test country", "Test poster");
        movieRepository.save(movie);

        MvcResult result = perform(get("/inactive-movies/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies-list"))
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("No data at the moment."));

        movieRepository.delete(movie);
    }

    @Test
    void shouldRedirectInactiveMovies() throws Exception {
        perform(get("/inactive-movies"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldReturnPolishMovies() throws Exception {
        Movie movie = new Movie(1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Polska", "Test poster");
        movieRepository.save(movie);

        perform(get("/polish-movies/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies-list"));

        movieRepository.delete(movie);
    }

    @Test
    void shouldNotReturnPolishMovies() throws Exception {
        Movie movie = new Movie(1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Test country", "Test poster");
        movieRepository.save(movie);

        MvcResult result = perform(get("/polish-movies/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies-list"))
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("No data at the moment."));

        movieRepository.delete(movie);
    }

    @Test
    void shouldRedirectPolishMovies() throws Exception {
        perform(get("/polish-movies"))
                .andExpect(status().is3xxRedirection());
    }
}