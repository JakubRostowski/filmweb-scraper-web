package pl.jrostowski.filmwebscraper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
