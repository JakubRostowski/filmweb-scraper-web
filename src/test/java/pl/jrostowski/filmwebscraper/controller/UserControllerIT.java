package pl.jrostowski.filmwebscraper.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import pl.jrostowski.filmwebscraper.BaseDatabaseTest;
import pl.jrostowski.filmwebscraper.entity.User;
import pl.jrostowski.filmwebscraper.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class UserControllerIT extends BaseDatabaseTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldChangeRole() throws Exception {
        refreshWebApplicationContext();
        User user = new User("test username", "test email", "test password");
        userRepository.save(user);

        perform(post("/change-role/" + user.getUserId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel"));

        Assertions.assertNotEquals("ROLE_USER", userRepository.findByUserId(user.getUserId()).getRole());
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUsers() throws Exception {
        perform(get("/admin-panel/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-panel"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRedirect() throws Exception {
        perform(get("/admin-panel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/page/1"));
    }
}