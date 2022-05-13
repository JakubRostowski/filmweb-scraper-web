package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jrostowski.filmwebscraper.entity.Movie;
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

    @GetMapping("/download")
    public Map<Integer, Movie> displayFilmwebData() throws IOException {
        return movieService.downloadData();
    }

    @GetMapping("/update")
    public void forceUpdate() throws IOException {
        if (movieService.countMovies() == 0) {
            movieService.populateDatabase(movieService.downloadData());
        } else {
            movieService.checkDifferences(movieService.downloadData());
        }
        System.out.println("Done!");
    }

    @GetMapping("/movies")
    public List<Movie> displayMovieContent(){
        return movieService.getMovieContent();
    }

    @GetMapping("/movies/{movieId}")
    public Movie getMovieById(@PathVariable Long movieId) {
        Optional<Movie> movie = movieService.findById(movieId);
        if (movie.isPresent()) {
            return movie.get();
        } else {
            throw new RuntimeException("Movie id (" + movieId + ") not found.");
        }
    }

}
