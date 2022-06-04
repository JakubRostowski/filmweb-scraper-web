package pl.jrostowski.filmwebscraper.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.jrostowski.filmwebscraper.entity.User;
import pl.jrostowski.filmwebscraper.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldChangeRole() throws Exception {
        User user = new User("test username", "test email", "test password");
        userRepository.save(user);

        mockMvc.perform(post("/change-role/" + user.getUserId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel"));

        Assertions.assertNotEquals("ROLE_USER", userRepository.findByUserId(user.getUserId()).getRole());
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUsers() throws Exception {
        this.mockMvc.perform(get("/admin-panel/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-panel"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRedirect() throws Exception {
        mockMvc.perform(get("/admin-panel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel"));
    }
}