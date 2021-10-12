package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.service.MovieService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService service;

    @GetMapping("/movies")
    public String showDatabaseMovies(Model model) {
        List<Movie> movies = service.getMovieContent();
        model.addAttribute("movies", movies);
        return "moviesList";
    }
}
