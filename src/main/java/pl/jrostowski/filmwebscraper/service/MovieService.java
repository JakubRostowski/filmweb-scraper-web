package pl.jrostowski.filmwebscraper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.repository.ArchivedMovieRepository;
import pl.jrostowski.filmwebscraper.repository.MovieRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    @PersistenceContext
    private EntityManager em;

    private final MovieRepository movieRepository;
    private final ArchivedMovieRepository archivedMovieRepository;

    public Map<Integer, Movie> downloadData() throws IOException {
        System.out.println("Downloading the data from Filmweb.pl...");
        return movieRepository.getTopList();
    }

    public boolean isDatabaseEmpty() {
        return movieRepository.checkIfEmpty();
    }

    @Transactional
    public void populateDatabase(Map<Integer, Movie> movieMap) {
        movieRepository.createDatabase(movieMap);
    }

    @Transactional
    public void checkDifferences(Map<Integer, Movie> movieMap) {
        System.out.println("Looking for differences...");

        List<Movie> databaseMovies = movieRepository.getMoviesFromDatabase();

        for (Map.Entry<Integer, Movie> movie : movieMap.entrySet()) {
            Movie checkedMovie = getUniqueMovieByPosition(databaseMovies, movie.getValue().getPosition()).get();
            if (movie.getValue().hashCode() == checkedMovie.hashCode()) {
                movieRepository.updateTimeOfModification(checkedMovie);
            } else {
                System.out.println(checkedMovie.getPosition() + ". " + checkedMovie.getTitle() + " changed.");
                archivedMovieRepository.addArchivedMovie(checkedMovie);
                if (movie.getValue().getTitle().equals(checkedMovie.getTitle())) {
                    movieRepository.updateChangedMovie(checkedMovie, movie.getValue());
                    checkedMovie.getArchivedMovies().add(movie.getValue().getArchivedMovieObject());
                } else {
                    movieRepository.addMovie(movie.getValue());
                    movieRepository.updatePositionToUnused(checkedMovie);
                }
            }
        }
    }

    public List<Movie> getMovieContent() {
        return movieRepository.getMoviesFromDatabase();
    }

    Optional<Movie> getUniqueMovieByPosition(List<Movie> list, int position) {
        return list.stream()
                .filter(movie -> movie.getPosition() == position)
                .findFirst();
    }

    public void ExportFile(Map<Integer, Movie> movieMap, boolean newExcelFormat) throws IOException {
        System.out.println("Exporting the data to excel format...");
        movieRepository.exportToExcel(movieMap, newExcelFormat);
    }
}
