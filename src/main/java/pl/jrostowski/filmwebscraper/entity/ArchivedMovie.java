package pl.jrostowski.filmwebscraper.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class ArchivedMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long archivedMovieId;

    @ManyToOne
    @JoinColumn(name="movieId")
    private Movie movieId;

    @NonNull private int position;
    @NonNull private String title;
    @NonNull private double rate;
    @NonNull private double criticsRate;
    @NonNull private Timestamp timeOfCreation;
    private Timestamp timeOfModification = new Timestamp(System.currentTimeMillis());

}
