package pl.jrostowski.filmwebscraper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Page<User> findAllByOrderByUserIdAsc(Pageable pageable);

    User findFirstByUsername(String username);

    User findFirstByEmail(String email);

    User findByUserId(Long id);
}
