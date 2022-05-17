package pl.jrostowski.filmwebscraper.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long postId;

    @NonNull private String title;
    @NonNull private String content;

    @OneToOne
    @NonNull private User author;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> likes;

    private Timestamp timeOfCreation = new Timestamp(System.currentTimeMillis());
    private Timestamp timeOfModification = new Timestamp(System.currentTimeMillis());
}
