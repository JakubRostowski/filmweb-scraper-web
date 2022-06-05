package pl.jrostowski.filmwebscraper.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.jrostowski.filmwebscraper.entity.BugReport;
import pl.jrostowski.filmwebscraper.repository.BugReportRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class BugReportControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    BugReportRepository bugReportRepository;
    @Autowired
    WebApplicationContext wac;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBugReports() throws Exception {
        BugReport bugReport = new BugReport("test description");
        bugReportRepository.save(bugReport);

        MvcResult result = this.mockMvc.perform(get("/bug-reports/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bug-reports"))
                .andReturn();

        Assertions.assertTrue(result.getResponse().getContentAsString().contains("test description"));

        bugReportRepository.deleteAll();
    }

    @Test
    void shouldRedirect() throws Exception {
        mockMvc.perform(get("/bug-reports"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bug-reports/page/1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldSaveBugReport() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        BugReport sample = new BugReport("sample report");
        bugReportRepository.save(sample);

        mockMvc.perform(post("/bug-reports/save")
                        .param("description", "test description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bug-reports/page/1"))
                .andReturn();

        BugReport bugReport = bugReportRepository.findById(sample.getBugReportId() + 1).get();
        Assertions.assertEquals("test description", bugReport.getDescription());

        bugReportRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldChangeStatus() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        BugReport bugReport = new BugReport("test description");
        bugReportRepository.save(bugReport);

        mockMvc.perform(post("/bug-reports/change-status/" + bugReport.getBugReportId())
                        .param("status", String.valueOf(BugReport.Status.COMPLETED)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bug-reports"));

        Assertions.assertEquals(BugReport.Status.COMPLETED, bugReportRepository.findById(bugReport.getBugReportId()).get().getStatus());
        bugReportRepository.deleteAll();
    }
}