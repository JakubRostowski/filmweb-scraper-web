package pl.jrostowski.filmwebscraper.forms;

import lombok.Getter;
import lombok.Setter;
import pl.jrostowski.filmwebscraper.validation.PasswordMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@PasswordMatch.List({
        @PasswordMatch(password = "password", confirmPassword = "confirmPassword", message = "Passwords do not match")
})
@Getter
@Setter
public class UserForm {

    @NotBlank(message = "is required")
    private String username;

    @NotBlank(message = "is required")
    private String password;

    @NotBlank(message = "is required")
    private String confirmPassword;

    @Email
    @NotBlank(message = "is required")
    private String email;

    public UserForm() {

    }
}

