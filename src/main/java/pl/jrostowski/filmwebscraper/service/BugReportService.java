package pl.jrostowski.filmwebscraper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jrostowski.filmwebscraper.entity.BugReport;
import pl.jrostowski.filmwebscraper.entity.BugReport.Status;
import pl.jrostowski.filmwebscraper.exception.BugReportNotFoundException;
import pl.jrostowski.filmwebscraper.repository.BugReportRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BugReportService {

    private final BugReportRepository bugReportRepository;

    public Page<BugReport> getBugReports(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return bugReportRepository.findAllByOrderByBugReportIdDesc(pageable);
    }

    public void save(BugReport bugReport) {
        bugReportRepository.save(bugReport);
    }

    public void changeStatus(Long id, Status status) {
        Optional<BugReport> bugReport = bugReportRepository.findById(id);

        if (bugReport.isPresent()) {
            bugReport.get().setStatus(status);
            save(bugReport.get());
        } else {
            throw new BugReportNotFoundException(id);
        }
    }
}
