package pl.jrostowski.filmwebscraper.exception;

public abstract class NotFoundException extends RuntimeException {

    protected NotFoundException(String name, Long id) {
        super(name + " with id " + id + " not found.");
    }
}
