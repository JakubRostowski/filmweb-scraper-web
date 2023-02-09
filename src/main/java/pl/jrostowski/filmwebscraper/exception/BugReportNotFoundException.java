package pl.jrostowski.filmwebscraper.exception;

public class BugReportNotFoundException extends NotFoundException {

    public BugReportNotFoundException(Long id) {
        super("Bug report", id);
    }
}
