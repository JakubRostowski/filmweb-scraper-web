package pl.jrostowski.filmwebscraper.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.ArchivedMovie;
import pl.jrostowski.filmwebscraper.entity.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Repository
public class ArchivedMovieRepository {

    private final EntityManager em;

    @Autowired
    public ArchivedMovieRepository(EntityManager em) {
        this.em = em;
    }

    public void addArchivedMovie(Movie movieData) {
        ArchivedMovie movie = movieData.getArchivedMovieObject();
        movie.setMovieId(movieData);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(movie);
        transaction.commit();
    }
}
