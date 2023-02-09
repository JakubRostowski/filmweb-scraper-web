package pl.jrostowski.filmwebscraper.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import pl.jrostowski.filmwebscraper.BaseDatabaseTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class LoginControllerIT extends BaseDatabaseTest {

    @Test
    @WithMockUser
    void shouldRedirectLoggedInUser() throws Exception {
        perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @WithAnonymousUser
    void shouldAllowAnonymousUser() throws Exception {
        perform(get("/login"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"));
    }
}