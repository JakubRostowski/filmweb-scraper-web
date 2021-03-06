package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.jrostowski.filmwebscraper.entity.User;
import pl.jrostowski.filmwebscraper.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/change-role/{id}")
    public String changeRole(@PathVariable Long id) {
        userService.toggleRole(id);
        return "redirect:/admin-panel";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-panel/page/{pageNumber}")
    public String getUserView(@PathVariable int pageNumber, Model model) {
        Page<User> page = userService.findAllByOrderByIdAsc(pageNumber, 15);
        model.addAttribute("users", page.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/admin-panel/page/");
        return "admin-panel";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-panel")
    public String redirect() {
        return "redirect:/admin-panel/page/1";
    }

}
