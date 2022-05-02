package pl.jrostowski.filmwebscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FilmwebscraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmwebscraperApplication.class, args);
    }

}
