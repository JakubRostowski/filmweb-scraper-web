package pl.jrostowski.filmwebscraper.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.jrostowski.filmwebscraper.service.MovieService;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UpdatePerformer {

    @Value("${scraper.update-after-startup}")
    private boolean updateAfterStartup;

    private final MovieService movieService;

    @EventListener(ApplicationReadyEvent.class)
    public void downloadMoviesAfterStartup() throws IOException {
        if (updateAfterStartup) {
            updateDatabase();
        }
    }

    @Scheduled(cron = "${cron.update-value}")
    public void updateDatabase() throws IOException {
        System.out.println("Automatic database update at " + new Date());
        if (movieService.countMovies() == 0) {
            System.out.println("Database is empty.");
            movieService.populateDatabase(movieService.downloadData());
        } else {
            System.out.println("Database is NOT empty.");
            movieService.checkDifferences(movieService.downloadData());
        }
        System.out.println("Updated performed!");
    }
}
