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

    private final MovieService movieService;

    @GetMapping("/movies")
    public String showDatabaseMovies(Model model) {
        List<Movie> movies = movieService.getMovieContent();
        model.addAttribute("movies", movies);
        return "movies-list";
    }

    @GetMapping("/toplist")
    public String showCurrentToplist(Model model) {
        List<Movie> movies = movieService.getToplistMovies();
        model.addAttribute("movies", movies);
        return "movies-list";
    }
}
