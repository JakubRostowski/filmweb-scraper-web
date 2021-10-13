package pl.jrostowski.filmwebscraper.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long userId;

    @NonNull private String name;
    @NonNull private String email;
    @NonNull private String password;
    private Timestamp timeOfCreation = new Timestamp(System.currentTimeMillis());
    private Timestamp timeOfModification = new Timestamp(System.currentTimeMillis());

}
