package pl.jrostowski.filmwebscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.jrostowski.filmwebscraper.entity.BugReport;
import pl.jrostowski.filmwebscraper.forms.BugReportForm;
import pl.jrostowski.filmwebscraper.service.BugReportService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class BugReportController {

    private final BugReportService bugReportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bug-reports/page/{pageNumber}")
    public String showBugReports(@PathVariable int pageNumber, Model model) {
        Page<BugReport> page = bugReportService.getBugReports(pageNumber, 5);
        model.addAttribute("reports", page.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/bug-reports/page/");
        return "bug-reports";
    }

    @GetMapping("/bug-reports")
    public String redirectBugReports() {
        return "redirect:/bug-reports/page/1";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/bug-reports/change-status/{reportId}")
    public String changeStatus(@PathVariable Long reportId, @RequestParam("status") BugReport.Status status) {
        bugReportService.changeStatus(reportId, status);
        return "redirect:/bug-reports";
    }

    @PreAuthorize("!hasRole('ADMIN')")
    @GetMapping("/bug-reports-form")
    public String showBugReportForm(Model model) {
        model.addAttribute("bugReportForm", new BugReportForm());
        return "bug-reports-form";
    }

    @PreAuthorize("!hasRole('ADMIN')")
    @PostMapping("/bug-reports/save")
    public String saveBugReport(@Valid @ModelAttribute("bug-report") BugReportForm bugReportForm, RedirectAttributes redirectAttributes) {
        BugReport bugReport = new BugReport(bugReportForm.getDescription());
        bugReportService.save(bugReport);
        redirectAttributes.addFlashAttribute("success", "success");
        return "redirect:/bug-reports-form";
    }

}
