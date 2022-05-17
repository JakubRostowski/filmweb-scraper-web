package pl.jrostowski.filmwebscraper.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PostForm {

    @NotBlank(message = "is required")
    private String title;

    @NotBlank(message = "is required")
    private String content;

    public PostForm() {

    }
}

