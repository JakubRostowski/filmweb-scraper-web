package pl.jrostowski.filmwebscraper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.ArchivedMovie;
import pl.jrostowski.filmwebscraper.entity.Movie;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ArchivedMovieRepository {

    private final EntityManager em;

    public void addArchivedMovie(Movie movieData) {
        ArchivedMovie movie = movieData.getArchivedMovieObject();
        movie.setMovieId(movieData);
        em.persist(movie);
    }
}
