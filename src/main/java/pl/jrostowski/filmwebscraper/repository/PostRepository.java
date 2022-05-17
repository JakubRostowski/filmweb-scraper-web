package pl.jrostowski.filmwebscraper.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.Post;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
