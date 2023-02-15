package pl.jrostowski.filmwebscraper.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(code = NOT_FOUND)
public abstract class NotFoundException extends RuntimeException {

    protected NotFoundException(String name, Long id) {
        super(name + " with id " + id + " not found.");
    }
}
