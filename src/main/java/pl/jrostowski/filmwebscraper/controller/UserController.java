package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.findAllByOrderByIdAsc();
        model.addAttribute("users", users);
        return "users-list";
    }

}
