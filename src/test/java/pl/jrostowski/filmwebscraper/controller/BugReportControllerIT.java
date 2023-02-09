package pl.jrostowski.filmwebscraper.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import pl.jrostowski.filmwebscraper.BaseDatabaseTest;
import pl.jrostowski.filmwebscraper.entity.BugReport;
import pl.jrostowski.filmwebscraper.repository.BugReportRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static pl.jrostowski.filmwebscraper.entity.BugReport.Status.COMPLETED;

class BugReportControllerIT extends BaseDatabaseTest {

    @Autowired
    BugReportRepository bugReportRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBugReports() throws Exception {
        bugReportRepository.save(new BugReport("test description"));

        MvcResult result = perform(get("/bug-reports/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bug-reports"))
                .andReturn();

        Assertions.assertTrue(result.getResponse().getContentAsString().contains("test description"));

        bugReportRepository.deleteAll();
    }

    @Test
    void shouldRedirect() throws Exception {
        perform(get("/bug-reports"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bug-reports/page/1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldSaveBugReport() throws Exception {
        refreshWebApplicationContext();
        BugReport sample = new BugReport("sample report");
        bugReportRepository.save(sample);

        perform(post("/bug-reports/save")
                .param("description", "test description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bug-reports-form"))
                .andReturn();

        BugReport bugReport = bugReportRepository.findById(sample.getBugReportId() + 1).get();
        Assertions.assertEquals("test description", bugReport.getDescription());

        bugReportRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldChangeStatus() throws Exception {
        refreshWebApplicationContext();
        BugReport bugReport = new BugReport("test description");
        bugReportRepository.save(bugReport);

        perform(post("/bug-reports/change-status/" + bugReport.getBugReportId())
                .param("status", String.valueOf(COMPLETED)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bug-reports"));

        Assertions.assertEquals(COMPLETED, bugReportRepository.findById(bugReport.getBugReportId()).get().getStatus());
        bugReportRepository.deleteAll();
    }
}