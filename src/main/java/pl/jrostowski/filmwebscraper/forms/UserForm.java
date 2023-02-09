package pl.jrostowski.filmwebscraper.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.jrostowski.filmwebscraper.validation.PasswordMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@PasswordMatch.List({
        @PasswordMatch(password = "password", confirmPassword = "confirmPassword", message = "Passwords do not match")
})
@Getter
@Setter
@NoArgsConstructor
public class UserForm {

    @NotBlank(message = "This field is required")
    private String username;

    @NotBlank(message = "This field is required")
    private String password;

    @NotBlank(message = "This field is required")
    private String confirmPassword;

    @Email(message = "Wrong e-mail format")
    @NotBlank(message = "This field is required")
    private String email;
}

