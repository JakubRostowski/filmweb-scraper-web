package pl.jrostowski.filmwebscraper.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(Authentication authentication) {
        boolean isNotLoggedIn = authentication == null || authentication instanceof AnonymousAuthenticationToken;

        return isNotLoggedIn ? "login" : "redirect:/";
    }
}
