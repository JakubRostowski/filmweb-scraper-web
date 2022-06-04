package pl.jrostowski.filmwebscraper.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BugReportForm {

    @NotBlank(message = "This field is required")
    private String description;

    public BugReportForm() {

    }
}

