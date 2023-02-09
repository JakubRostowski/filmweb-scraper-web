package pl.jrostowski.filmwebscraper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArchivedMovie {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long archivedMovieId;

    @JsonBackReference
    @ManyToOne
    private Movie movie;

    @NonNull
    private int position;
    @NonNull
    private String title;
    @NonNull
    private double rate;
    @NonNull
    private double criticsRate;
    @NonNull
    private Timestamp timeOfCreation = new Timestamp(System.currentTimeMillis());
    private Timestamp timeOfModification = new Timestamp(System.currentTimeMillis());

}
