package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.jrostowski.filmwebscraper.entity.Post;
import pl.jrostowski.filmwebscraper.entity.User;
import pl.jrostowski.filmwebscraper.forms.PostForm;
import pl.jrostowski.filmwebscraper.service.PostService;
import pl.jrostowski.filmwebscraper.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/posts/page/{pageNumber}")
    public String showPosts(@PathVariable int pageNumber, Model model, Authentication auth) {
        Page<Post> page = postService.getPosts(pageNumber, 10);
        model.addAttribute("posts", page.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/posts/page/");
        model.addAttribute("userName", (auth == null) ? null : auth.getName());

        PostForm postForm = new PostForm();
        model.addAttribute("postForm", postForm);

        return "posts";
    }

    @GetMapping("/posts")
    public String redirectPosts() {
        return "redirect:/posts/page/1";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/posts/save")
    public String savePost(@Valid @ModelAttribute("post") PostForm postForm, Errors errors, Model model, Authentication auth) {
        if (errors.hasErrors()) {
            model.addAttribute("nullFields", "Fields cannot be empty.");
            return "/posts";
        }

        User user = userService.findByUsername(auth.getName());
        Post post = new Post(postForm.getTitle(), postForm.getContent(), user);
        postService.save(post);
        return "redirect:/posts";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/posts/toggle-like/{postId}")
    public String toggleLike(@PathVariable Long postId, Authentication auth) {

        User user = userService.findByUsername(auth.getName());
        postService.toggleLike(user, postId);

        return "redirect:/posts";
    }
}
