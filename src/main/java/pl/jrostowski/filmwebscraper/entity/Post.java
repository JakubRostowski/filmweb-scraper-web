package pl.jrostowski.filmwebscraper.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long postId;

    @NonNull
    private String title;
    @NonNull
    private String content;

    @OneToOne
    @NonNull
    private User author;

    @JsonManagedReference
    @ManyToMany(fetch = EAGER)
    private List<User> likes;

    private Timestamp timeOfCreation = new Timestamp(System.currentTimeMillis());
    private Timestamp timeOfModification = new Timestamp(System.currentTimeMillis());

    public String getSimpleTimeOfCreation() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.timeOfCreation);
    }

    public boolean isPostLikedByUser(String searchedName) {
        for (User user : this.likes) {
            if (user.getUsername().equals(searchedName)) {
                return true;
            }
        }
        return false;
    }
}
