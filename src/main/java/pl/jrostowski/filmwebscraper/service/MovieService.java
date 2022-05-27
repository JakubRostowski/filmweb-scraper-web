package pl.jrostowski.filmwebscraper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jrostowski.filmwebscraper.entity.ArchivedMovie;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.repository.ArchivedMovieRepository;
import pl.jrostowski.filmwebscraper.repository.ExcelRepository;
import pl.jrostowski.filmwebscraper.repository.FilmwebRepository;
import pl.jrostowski.filmwebscraper.repository.MovieRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
        return (int) movieRepository.count();
    }

    @Transactional
    public void populateDatabase(Map<Integer, Movie> movieMap) {
        movieRepository.saveAll(movieMap.values());
    }

    @Transactional
    public void checkDifferences(Map<Integer, Movie> movieMap) {
        List<Movie> databaseMovies = movieRepository.getAllMovies();
        List<Integer> newMoviesPositions = new ArrayList<>();

        processRecognizedMovies(movieMap, databaseMovies, newMoviesPositions);
        processUnrecognizedMovies(movieMap, newMoviesPositions);
    }

    private void processRecognizedMovies(Map<Integer, Movie> movieMap, List<Movie> databaseMovies, List<Integer> newMoviesPositions) {
        for (Movie databaseMovie : databaseMovies) {
            Optional<Movie> checkedMovie =
                    getUniqueMovieByTitleAndYear(movieMap, databaseMovie.getTitle(), databaseMovie.getYear());
            if (checkedMovie.isPresent()) {
                if (databaseMovie.hashCode() == checkedMovie.get().hashCode()) {
                    processUnchangedMovie(checkedMovie.get(), databaseMovie.getMovieId());
                } else {
                    processChangedMovie(databaseMovie, checkedMovie.get());
                }
            } else {
                processInactiveMovie(newMoviesPositions, databaseMovie);
            }
        }
    }

    private void processUnchangedMovie(Movie checkedMovie, Long id) {
        checkedMovie.setMovieId(id);
        checkedMovie.setTimeOfModification(new Timestamp(System.currentTimeMillis()));
        movieRepository.save(checkedMovie);
    }

    private void processChangedMovie(Movie databaseMovie, Movie checkedMovie) {
        System.out.println(checkedMovie.getPosition() + ". " +
                checkedMovie.getTitle() + " changed.");
        databaseMovie.getArchivedMovies().add(checkedMovie.toArchivedMovie());
        addArchivedMovie(databaseMovie);
        updateChangedMovie(databaseMovie, checkedMovie);
    }

    private void addArchivedMovie(Movie movieData) {
        ArchivedMovie archivedMovie = movieData.toArchivedMovie();
        archivedMovie.setMovie(movieData);
        archivedMovieRepository.save(archivedMovie);
    }

    private void updateChangedMovie(Movie oldMovie, Movie newMovie) {
        oldMovie.setPosition(newMovie.getPosition());
        oldMovie.setRate(newMovie.getRate());
        oldMovie.setCriticsRate(newMovie.getCriticsRate());
        oldMovie.setTimeOfModification(new Timestamp(System.currentTimeMillis()));
        movieRepository.save(oldMovie);
    }

    private void processInactiveMovie(List<Integer> newMoviesPositions, Movie databaseMovie) {
        if (databaseMovie.getPosition() != -1) {
            newMoviesPositions.add(databaseMovie.getPosition());
        }
        databaseMovie.setPosition(-1);
        databaseMovie.setTimeOfModification(new Timestamp(System.currentTimeMillis()));
        movieRepository.save(databaseMovie);
    }

    private void processUnrecognizedMovies(Map<Integer, Movie> movieMap, List<Integer> newMoviesPositions) {
        for (Integer positionToBeReplaced : newMoviesPositions) {
            Optional<Map.Entry<Integer, Movie>> foundMovie = movieMap.entrySet()
                    .stream()
                    .filter(movie -> movie.getValue().getPosition() == positionToBeReplaced).findFirst();
            foundMovie.ifPresent(integerMovieEntry -> movieRepository.save(integerMovieEntry.getValue()));
        }
    }

    public List<Movie> getMovieContent() {
        return movieRepository.getAllMovies();
    }

    public Page<Movie> getToplistMovies(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return movieRepository.getActiveMovies(pageable);
    }

    public Page<Movie> getInactiveMovies(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return movieRepository.getInactiveMovies(pageable);
    }

    public Page<Movie> getPolishMovies(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return movieRepository.getPolishMovies(pageable);
    }

    Optional<Movie> getUniqueMovieByTitleAndYear(Map<Integer, Movie> movieMap, String title, int year) {
        Optional<Map.Entry<Integer, Movie>> searchedMovie = movieMap.entrySet().stream()
                .filter(movie -> movie.getValue().getTitle().equals(title))
                .filter(movie -> movie.getValue().getYear() == year).findFirst();

        if (searchedMovie.isPresent()) {
            return Optional.of(searchedMovie.get().getValue());
        } else {
            return Optional.empty();
        }
    }

    public void exportFile(Map<Integer, Movie> movieMap, boolean newExcelFormat) throws IOException {
        System.out.println("Exporting the data to excel format...");
        excelRepository.exportToExcel(movieMap, newExcelFormat);
    }

    public Optional<Movie> findById(Long movieId) {
        return movieRepository.findById(movieId);
    }
}