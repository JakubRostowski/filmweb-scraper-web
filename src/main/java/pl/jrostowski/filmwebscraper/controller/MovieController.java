package pl.jrostowski.filmwebscraper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.service.MovieService;

import java.io.IOException;
import java.util.List;
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

    @GetMapping("/updateDatabase")
    public void updateDatabase() throws IOException {
        if (service.isDatabaseEmpty()) {
            System.out.println("Database is empty.");
            service.populateDatabase(service.downloadData());
        } else {
            System.out.println("Database is NOT empty.");
            service.checkDifferences(service.downloadData());
        }
        System.out.println("Done!");
    }

    @GetMapping("/database")
    public List<Movie> displayDatabaseContent(){
        return service.getDatabaseContent();
    }
}
