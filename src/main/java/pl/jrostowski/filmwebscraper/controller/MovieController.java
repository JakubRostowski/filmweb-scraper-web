package pl.jrostowski.filmwebscraper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.service.MovieService;

import java.io.IOException;
import java.util.Map;

@RestController
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping("/display")
    public Map<Integer, Movie> displayFilmwebData() throws IOException {
        return service.downloadData();
    }
}
