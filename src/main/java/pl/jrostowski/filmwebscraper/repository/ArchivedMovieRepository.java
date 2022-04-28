package pl.jrostowski.filmwebscraper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.ArchivedMovie;

@Repository
public interface ArchivedMovieRepository extends CrudRepository<ArchivedMovie, Long> {

}
