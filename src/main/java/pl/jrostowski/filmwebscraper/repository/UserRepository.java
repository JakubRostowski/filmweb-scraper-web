package pl.jrostowski.filmwebscraper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAllByOrderByUserIdAsc();
    User findFirstByUsername(String username);
    User findFirstByEmail(String email);
    User findByUserId(Long id);
}
