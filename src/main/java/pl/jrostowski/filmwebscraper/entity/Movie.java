package pl.jrostowski.filmwebscraper.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long movieId;

    @NonNull private int position;
    @NonNull private String title;
    @NonNull private int year;
    @NonNull private String originalTitle;
    @NonNull private double rate;
    @NonNull private double criticsRate;
    @NonNull private String length;
    @NonNull private String director;
    @NonNull private String screenwriter;
    @NonNull private String genre;
    @NonNull private String countryOfOrigin;
    private Timestamp timeOfCreation = new Timestamp(System.currentTimeMillis());
    private Timestamp timeOfModification = new Timestamp(System.currentTimeMillis());
    @OneToMany(fetch = FetchType.EAGER, mappedBy="movieId")
    private List<ArchivedMovie> archivedMovies = new ArrayList<>();


    public ArchivedMovie getArchivedMovieObject() {
        return new ArchivedMovie(getPosition(), getTitle(), getRate(), getCriticsRate(), getTimeOfCreation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return getPosition() == movie.getPosition() && Double.compare(movie.getRate(), getRate()) == 0 && Double.compare(movie.getCriticsRate(), getCriticsRate()) == 0 && getTitle().equals(movie.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), getTitle(), getRate(), getCriticsRate());
    }

}
