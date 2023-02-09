package pl.jrostowski.filmwebscraper.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static pl.jrostowski.filmwebscraper.entity.BugReport.Status.NEW;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class BugReport {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long bugReportId;

    @NonNull
    private String description;

    @Enumerated(STRING)
    private Status status = NEW;

    private Timestamp timeOfCreation = new Timestamp(System.currentTimeMillis());
    private Timestamp timeOfModification = new Timestamp(System.currentTimeMillis());

    public enum Status {
        NEW, IN_PROGRESS, COMPLETED, REJECTED
    }

    public String getSimpleTimeOfCreation() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.timeOfCreation);
    }

    public String getSimpleTimeOfModification() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.timeOfModification);
    }

}
