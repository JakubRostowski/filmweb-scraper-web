package pl.jrostowski.filmwebscraper.exception;

public class PostNotFoundException extends NotFoundException {

    public PostNotFoundException(Long id) {
        super("Post", id);
    }
}
