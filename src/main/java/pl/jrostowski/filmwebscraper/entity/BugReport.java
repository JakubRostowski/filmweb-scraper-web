package pl.jrostowski.filmwebscraper.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class BugReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long bugReportId;

    @NonNull private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    private Timestamp timeOfCreation = new Timestamp(System.currentTimeMillis());
    private Timestamp timeOfModification = new Timestamp(System.currentTimeMillis());

    public enum Status {
        NEW, IN_PROGRESS, COMPLETED, REJECTED
    }


}
