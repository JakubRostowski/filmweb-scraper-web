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

    @Query("select m from Movie m left join fetch m.archivedMovies")
    List<Movie> getAllMovies();

    @Query(value = "select m from Movie m where m.position > 0 order by m.position")
    Page<Movie> getActiveMovies(Pageable pageable);

    @Query(value = "select m from Movie m where m.position = -1 order by m.title")
    Page<Movie> getInactiveMovies(Pageable pageable);

    @Query(value = "select m from Movie m where m.countryOfOrigin like '%Polska%' order by nullif(m.position, -1), m.position")
    Page<Movie> getPolishMovies(Pageable pageable);

    @Query("select m from Movie m left join fetch m.archivedMovies where m.movieId = ?1")
    Optional<Movie> findById(Long movieId);
}
