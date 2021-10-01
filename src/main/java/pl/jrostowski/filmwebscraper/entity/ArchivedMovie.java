package pl.jrostowski.filmwebscraper.entity;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
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

    @Override
    public String toString() {
        return "ArchivedMovie{" +
                "archivedMovieId=" + archivedMovieId +
                ", movieId=" + movieId +
                ", position=" + position +
                ", title='" + title + '\'' +
                ", rate=" + rate +
                ", criticsRate=" + criticsRate +
                ", timeOfCreation=" + timeOfCreation +
                ", timeOfModification=" + timeOfModification +
                '}';
    }
}
