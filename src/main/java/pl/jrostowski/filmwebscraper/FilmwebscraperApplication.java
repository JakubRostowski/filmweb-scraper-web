package pl.jrostowski.filmwebscraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.jrostowski.filmwebscraper.scheduler.UpdatePerformer;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class FilmwebscraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmwebscraperApplication.class, args);
    }

}
