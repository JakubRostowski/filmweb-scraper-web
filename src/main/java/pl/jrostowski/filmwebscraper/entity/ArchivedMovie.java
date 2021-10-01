package pl.jrostowski.filmwebscraper.entity;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class ArchivedMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long archivedMovieId;

    @ManyToOne
    @JoinColumn(name="movieId")
    private Movie movieId;

    private int position;
    private String title;
    private double rate;
    private double criticsRate;
    private Timestamp timeOfCreation;
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
