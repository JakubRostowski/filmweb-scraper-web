package pl.jrostowski.filmwebscraper.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.jrostowski.filmwebscraper.service.MovieService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
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
        log.info("--- Update started. ---");
        if (movieService.countMovies() == 0) {
            log.info("Database is empty.");
            movieService.populateDatabase(movieService.downloadData());
        } else {
            log.info("Database is NOT empty.");
            movieService.checkDifferences(movieService.downloadData());
        }
        log.info("--- Update performed! ---");
    }
}
