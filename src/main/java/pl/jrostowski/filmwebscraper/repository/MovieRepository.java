package pl.jrostowski.filmwebscraper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.Movie;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {

    @Query("select distinct m from Movie m left join fetch m.archivedMovies")
    List<Movie> getAllMovies();

    @Query("select distinct m from Movie m left join fetch m.archivedMovies where m.position > 0 order by m.position")
    List<Movie> getActiveMovies();

    @Query(value = "select distinct m from Movie m where m.position > 0 order by m.position")
    Page<Movie> getActiveMovies(Pageable pageable);

    @Query("select m from Movie m where m.movieId = ?1")
    Optional<Movie> findById(Long movieId);
}
