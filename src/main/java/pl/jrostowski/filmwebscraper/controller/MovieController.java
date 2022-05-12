package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pl.jrostowski.filmwebscraper.entity.Movie;
import pl.jrostowski.filmwebscraper.service.MovieService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movies/{id}")
    public String getMovie(@PathVariable Long id, Model model) {
        Optional<Movie> movie = movieService.findById(id);
        if (movie.isPresent()) {
            model.addAttribute("movie", movie.get());
            return "movie-info";
        }
        return "redirect:/";
    }

    @GetMapping("/top500/page/{pageNumber}")
    public String showActiveMovies(@PathVariable int pageNumber, Model model) {
        Page<Movie> page = movieService.getToplistMovies(pageNumber, 15);
        model.addAttribute("movies", page.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/top500/page/");
        model.addAttribute("webpageTitle", "Top 500");
        return "movies-list";
    }

    @GetMapping("/top500")
    public ModelAndView redirect() {
        return new ModelAndView("redirect:/top500/page/1");
    }

    //    @GetMapping("/inactive-movies")
//    public String showAllMovies(Model model) {
//        List<Movie> movies = movieService.getMovieContent();
//        model.addAttribute("movies", movies);
//        return "movies-list";
//    }

//    @GetMapping("/polish-movies")
//    public String showPolishMovies(Model model) {
//        List<Movie> movies = movieService.getToplistMovies();
//        model.addAttribute("movies", movies);
//        return "movies-list";
//    }


}
