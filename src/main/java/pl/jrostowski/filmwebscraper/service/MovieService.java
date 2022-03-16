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
        List<Integer> newMoviesPositions = new ArrayList<>() {
        };

        processRecognizedMovies(movieMap, databaseMovies, newMoviesPositions);
        processUnrecognizedMovies(movieMap, newMoviesPositions);
    }

    private void processRecognizedMovies(Map<Integer, Movie> movieMap, List<Movie> databaseMovies, List<Integer> newMoviesPositions) {
        for (Movie databaseMovie : databaseMovies) {
            Optional<Movie> checkedMovie =
                    getUniqueMovieByTitleAndYear(movieMap, databaseMovie.getTitle(), databaseMovie.getYear());
            if (checkedMovie.isPresent()) {
                if (databaseMovie.hashCode() == checkedMovie.get().hashCode()) {
                    processUnchangedMovie(checkedMovie);
                } else {
                    processChangedMovie(databaseMovie, checkedMovie);
                }
            } else {
                processUnactiveMovie(newMoviesPositions, databaseMovie);
            }
        }
    }

    private void processUnchangedMovie(Optional<Movie> checkedMovie) {
        movieRepository.updateTimeOfModification(checkedMovie.get());
    }

    private void processChangedMovie(Movie databaseMovie, Optional<Movie> checkedMovie) {
        System.out.println(checkedMovie.get().getPosition() + ". " +
                checkedMovie.get().getTitle() + " changed.");
        databaseMovie.getArchivedMovies().add(checkedMovie.get().toArchivedMovie());
        archivedMovieRepository.addArchivedMovie(databaseMovie);
        movieRepository.updateChangedMovie(databaseMovie, checkedMovie.get());
    }

    private void processUnactiveMovie(List<Integer> newMoviesPositions, Movie databaseMovie) {
        if(databaseMovie.getPosition() != -1) {
            newMoviesPositions.add(databaseMovie.getPosition());
        }
        movieRepository.updatePositionToUnused(databaseMovie);
        movieRepository.updateTimeOfModification(databaseMovie);
        System.out.println(databaseMovie.getTitle() + " set to inactive.");
    }

    private void processUnrecognizedMovies(Map<Integer, Movie> movieMap, List<Integer> newMoviesPositions) {
        for (Integer positionToBeReplaced : newMoviesPositions) {
            Optional<Map.Entry<Integer, Movie>> foundMovie = movieMap.entrySet()
                    .stream()
                    .filter(movie -> movie.getValue().getPosition() == positionToBeReplaced).findFirst();
            if (foundMovie.isPresent()) {
                movieRepository.addMovie(foundMovie.get().getValue());
                System.out.println(positionToBeReplaced + " added successfully.");
            } else {
                System.out.println("Movie position" + positionToBeReplaced + "has not been found.");
            }
        }
    }

    public List<Movie> getMovieContent() {
        return movieRepository.getAllMovies();
    }

    public List<Movie> getToplistMovies() {
        return movieRepository.getActiveMovies();
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

    Optional<Movie> getUniqueMovieByPosition(List<Movie> movieList, int position) {
        return Optional.of(movieList.stream()
                .filter(movie -> movie.getPosition() == position)
                .findFirst()
                .get());
    }

    public void ExportFile(Map<Integer, Movie> movieMap, boolean newExcelFormat) throws IOException {
        System.out.println("Exporting the data to excel format...");
        excelRepository.exportToExcel(movieMap, newExcelFormat);
    }

    public Movie findById(int movieId) {
        return movieRepository.findById(movieId);
    }
}