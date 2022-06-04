package pl.jrostowski.filmwebscraper.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.repository.MovieRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void shouldReturnSingleMovie() throws Exception {
        Movie movie = new Movie(1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Test country", "Test poster");
        movieRepository.save(movie);

        this.mockMvc.perform(get("/movies/" + movie.getMovieId()))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-info"));

        movieRepository.deleteAll();
    }

    @Test
    void shouldNotReturnSingleMovieAndRedirect() throws Exception {
        this.mockMvc.perform(get("/movies/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldReturnActiveMovies() throws Exception {
        Movie movie = new Movie(1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Test country", "Test poster");
        movieRepository.save(movie);

        this.mockMvc.perform(get("/top500/page/1"))
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

        MvcResult result = this.mockMvc.perform(get("/top500/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies-list"))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("No data at the moment."));

        movieRepository.delete(movie);
    }

    @Test
    void shouldRedirectTop500() throws Exception {
        this.mockMvc.perform(get("/top500"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldReturnInactiveMovies() throws Exception {
        Movie movie = new Movie(-1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Test country", "Test poster");
        movieRepository.save(movie);

        this.mockMvc.perform(get("/inactive-movies/page/1"))
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

        MvcResult result = this.mockMvc.perform(get("/inactive-movies/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies-list"))
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("No data at the moment."));

        movieRepository.delete(movie);
    }

    @Test
    void shouldRedirectInactiveMovies() throws Exception {
        this.mockMvc.perform(get("/inactive-movies"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldReturnPolishMovies() throws Exception {
        Movie movie = new Movie(1, "Test title", 2000, "Test original title",
                6.00, 8.00, "Test length", "Test director", "Test screenwriter",
                "Test genre", "Polska", "Test poster");
        movieRepository.save(movie);

        this.mockMvc.perform(get("/polish-movies/page/1"))
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

        MvcResult result = this.mockMvc.perform(get("/polish-movies/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies-list"))
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("No data at the moment."));

        movieRepository.delete(movie);
    }

    @Test
    void shouldRedirectPolishMovies() throws Exception {
        this.mockMvc.perform(get("/polish-movies"))
                .andExpect(status().is3xxRedirection());
    }
}