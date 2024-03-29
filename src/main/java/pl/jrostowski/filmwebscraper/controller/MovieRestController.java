package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.exception.MovieNotFoundException;
import pl.jrostowski.filmwebscraper.scheduler.UpdatePerformer;
import pl.jrostowski.filmwebscraper.service.MovieService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MovieRestController {

    private final MovieService movieService;
    private final UpdatePerformer updatePerformer;

    @GetMapping("/download")
    public Map<Integer, Movie> displayFilmwebData() throws IOException {
        return movieService.downloadData();
    }

    @GetMapping("/update")
    public void forceUpdate() throws IOException {
        updatePerformer.updateDatabase();
    }

    @GetMapping("/movies")
    public List<Movie> displayMovieContent() {
        return movieService.getMovieContent();
    }

    @GetMapping("/movies/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.findById(id);
        if (movie.isPresent()) {
            return movie.get();
        } else {
            throw new MovieNotFoundException(id);
        }
    }

}
