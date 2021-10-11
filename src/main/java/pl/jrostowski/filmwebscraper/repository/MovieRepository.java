package pl.jrostowski.filmwebscraper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.Movie;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.intValue;

@Repository
@RequiredArgsConstructor
public class MovieRepository {

    private final EntityManager em;

    public List getMoviesFromDatabase() {
        return em.createQuery("from Movie").getResultList();
    }

    public void createDatabase(Map<Integer, Movie> movies) {
        for (Map.Entry<Integer, Movie> movie : movies.entrySet()) {
            addMovie(movie.getValue());
        }
    }

    public void addMovie(Movie movie) {
        em.persist(movie);
    }

    public int getMovieCount() {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM movie");
        return intValue(query.getResultList().get(0));
    }

    public void deleteMovie(Movie movie) {
        em.remove(movie);
    }

    public void updateChangedMovie(Movie oldMovie, Movie newMovie) {
        oldMovie.setPosition(newMovie.getPosition());
        oldMovie.setRate(newMovie.getRate());
        oldMovie.setCriticsRate(newMovie.getCriticsRate());
        oldMovie.setTimeOfModification(new Timestamp(System.currentTimeMillis()));
    }

    public void updatePositionToUnused(Movie movie) {
        movie.setPosition(-1);
    }

    public void updateTimeOfModification(Movie checkedMovie) {
        checkedMovie.setTimeOfModification(new Timestamp(System.currentTimeMillis()));
    }
}
