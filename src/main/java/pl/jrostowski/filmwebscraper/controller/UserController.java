package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "usersList";
    }

    @GetMapping("/users/add")
    public String getRegisterForm(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("user", userForm);
        return "register";
    }

    @PostMapping("users/save")
    public String saveUser(@Valid @ModelAttribute("user") UserForm userForm, Errors errors) {
        if (errors.hasErrors()) {
            return "register";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userForm.getPassword());
        userForm.setPassword(encodedPassword);
        User user = new User(userForm.getUsername(), userForm.getEmail(), userForm.getPassword());
        userService.save(user);
        return "redirect:/users/";
    }

}
