package pl.jrostowski.filmwebscraper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.BugReport;

@Repository
public interface BugReportRepository extends PagingAndSortingRepository<BugReport, Long> {

    Page<BugReport> findAllByOrderByBugReportIdDesc(Pageable pageable);
}
