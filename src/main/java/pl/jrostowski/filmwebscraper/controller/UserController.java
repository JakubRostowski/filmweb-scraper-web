package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jrostowski.filmwebscraper.entity.User;
import pl.jrostowski.filmwebscraper.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public String showDatabaseMovies(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "usersList";
    }

}
