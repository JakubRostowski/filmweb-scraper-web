package pl.jrostowski.filmwebscraper.exception;

public class MovieNotFoundException extends NotFoundException {

    public MovieNotFoundException(Long id) {
        super("Movie", id);
    }
}
