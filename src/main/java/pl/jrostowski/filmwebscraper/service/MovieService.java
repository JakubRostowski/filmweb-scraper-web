package pl.jrostowski.filmwebscraper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.repository.ArchivedMovieRepository;
import pl.jrostowski.filmwebscraper.repository.ExcelRepository;
import pl.jrostowski.filmwebscraper.repository.FilmwebRepository;
import pl.jrostowski.filmwebscraper.repository.MovieRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ArchivedMovieRepository archivedMovieRepository;
    private final FilmwebRepository filmwebRepository;
    private final ExcelRepository excelRepository;

    public Map<Integer, Movie> downloadData() throws IOException {
        System.out.println("Downloading the data from Filmweb.pl...");
        return filmwebRepository.getTopList();
    }

    public int countMovies() {
        return movieRepository.getMovieCount();
    }

    @Transactional
    public void populateDatabase(Map<Integer, Movie> movieMap) {
        movieRepository.createDatabase(movieMap);
    }

    @Transactional
    public void checkDifferences(Map<Integer, Movie> movieMap) {
        System.out.println("Looking for differences...");
        List<Movie> databaseMovies = movieRepository.getAllMovies();

        for (Movie databaseMovie : databaseMovies) {
            Optional<Movie> checkedMovie = getUniqueMovieByTitle(movieMap, databaseMovie.getTitle());
            if (checkedMovie.isPresent()) {
                if (databaseMovie.hashCode() == checkedMovie.get().hashCode()) {
                    movieRepository.updateTimeOfModification(checkedMovie.get());
                } else {
                    System.out.println(checkedMovie.get().getPosition() + ". " + checkedMovie.get().getTitle() + " changed.");
                    databaseMovie.getArchivedMovies().add(checkedMovie.get().toArchivedMovie());
                    archivedMovieRepository.addArchivedMovie(databaseMovie);
                    movieRepository.updateChangedMovie(databaseMovie, checkedMovie.get());
                }
            } else {
                movieRepository.addMovie(checkedMovie.get()); // nieosiągalne
                movieRepository.updatePositionToUnused(databaseMovie);
            }
        }
    }

    public List<Movie> getMovieContent() {
        return movieRepository.getAllMovies();
    }

    public List<Movie> getToplistMovies() {
        return movieRepository.getActiveMovies();
    }

    Optional<Movie> getUniqueMovieByTitle(Map<Integer, Movie> movieMap, String title) {
        return Optional.of(movieMap.entrySet().stream()
                .filter(movie -> movie.getValue().getTitle().equals(title))
                .findFirst()
                .get()
                .getValue());
    }

    public void ExportFile(Map<Integer, Movie> movieMap, boolean newExcelFormat) throws IOException {
        System.out.println("Exporting the data to excel format...");
        excelRepository.exportToExcel(movieMap, newExcelFormat);
    }

    public Movie findById(int movieId) {
        return movieRepository.findById(movieId);
    }
}
