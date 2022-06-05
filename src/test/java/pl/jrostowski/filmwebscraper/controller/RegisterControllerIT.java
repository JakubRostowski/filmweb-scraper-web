package pl.jrostowski.filmwebscraper.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
class RegisterControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WebApplicationContext wac;

    @Test
    @WithAnonymousUser
    void shouldAccessRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"));
    }

    @Test
    @WithMockUser
    void shouldNotAccessRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/index"));
    }

    @Test
    void shouldDetectNullFields() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        MvcResult result = mockMvc.perform(post("/register/save"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"))
                .andReturn();

        Assertions.assertTrue(result.getResponse().getContentAsString().contains("This field is required"));
    }

    @Test
    void shouldDetectExistingUsername() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        User user = new User("test", "test@test.com", "test");
        userRepository.save(user);

        MvcResult result = mockMvc.perform(post("/register/save")
                        .param("username", "test")
                        .param("email", "test@test.com")
                        .param("password", "123")
                        .param("confirmPassword", "123"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"))
                .andReturn();

        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Username already exists."));
        userRepository.deleteAll();
    }

    @Test
    void shouldDetectExistingEmail() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        User user = new User("test username", "test@test.com", "test");
        userRepository.save(user);

        MvcResult result = mockMvc.perform(post("/register/save")
                        .param("username", "test")
                        .param("email", "test@test.com")
                        .param("password", "123")
                        .param("confirmPassword", "123"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"))
                .andReturn();

        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Email already exists."));
        userRepository.deleteAll();
    }

    @Test
    void shouldDetectNotMatchingPasswords() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        User user = new User("test username", "test@test.com", "test");
        userRepository.save(user);

        MvcResult result = mockMvc.perform(post("/register/save")
                        .param("username", "test")
                        .param("email", "newtest@test.com")
                        .param("password", "123")
                        .param("confirmPassword", "1234"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"))
                .andReturn();

        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Passwords do not match"));
        userRepository.deleteAll();
    }

    @Test
    void shouldAddNewUserAndRedirect() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        User user = new User("test username", "test@test.com", "test");
        userRepository.save(user);

        mockMvc.perform(post("/register/save")
                        .param("username", "test")
                        .param("email", "newtest@test.com")
                        .param("password", "123")
                        .param("confirmPassword", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));

        User newUser = userRepository.findByUserId(user.getUserId() + 1);
        Assertions.assertEquals("test", newUser.getUsername());
        Assertions.assertEquals("newtest@test.com", newUser.getEmail());
        userRepository.deleteAll();
    }
}