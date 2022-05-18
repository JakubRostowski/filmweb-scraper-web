package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.jrostowski.filmwebscraper.entity.User;
import pl.jrostowski.filmwebscraper.forms.UserForm;
import pl.jrostowski.filmwebscraper.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegisterForm(Model model, Authentication authentication) {
        UserForm userForm = new UserForm();
        model.addAttribute("user", userForm);

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "register";
        } else {
            return "index";
        }
    }

    @PostMapping("register/save")
    public String saveUser(@Valid @ModelAttribute("user") UserForm userForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "register";
        }
        if (userService.findByUsername(userForm.getUsername()) != null) {
            model.addAttribute("registrationError", "Username already exists.");
            return "register";
        }
        if (userService.findByEmail(userForm.getEmail()) != null) {
            model.addAttribute("registrationError", "Email already exists.");
            return "register";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userForm.getPassword());
        userForm.setPassword(encodedPassword);
        User user = new User(userForm.getUsername(), userForm.getEmail(), userForm.getPassword());
        userService.save(user);
        return "login";
    }
}
