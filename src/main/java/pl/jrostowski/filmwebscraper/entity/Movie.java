package pl.jrostowski.filmwebscraper.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long movieId;

    private int position;
    private String title;
    private int year;
    private String originalTitle;
    private double rate;
    private double criticsRate;
    private String length;
    private String director;
    private String screenwriter;
    private String genre;
    private String countryOfOrigin;
    private Timestamp timeOfCreation = new Timestamp(System.currentTimeMillis());
    private Timestamp timeOfModification = new Timestamp(System.currentTimeMillis());
    @OneToMany(mappedBy="movieId")
    private List<ArchivedMovie> archivedMovies = new ArrayList<>();


//    public ArchivedMovie getArchivedMovieObject() {
//        return new ArchivedMovie(getPosition(), getTitle(), getRate(), getCriticsRate(), getTimeOfCreation());
//    }

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

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", position=" + position +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", originalTitle='" + originalTitle + '\'' +
                ", rate=" + rate +
                ", criticsRate=" + criticsRate +
                ", length='" + length + '\'' +
                ", director='" + director + '\'' +
                ", screenwriter='" + screenwriter + '\'' +
                ", genre='" + genre + '\'' +
                ", countryOfOrigin='" + countryOfOrigin + '\'' +
                ", timeOfCreation=" + timeOfCreation +
                ", timeOfModification=" + timeOfModification +
                ", archivedMovies=" + archivedMovies +
                '}';
    }
}
