package pl.jrostowski.filmwebscraper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jrostowski.filmwebscraper.entity.Post;
import pl.jrostowski.filmwebscraper.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Page<Post> getPosts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return postRepository.findAllByOrderByPostIdDesc(pageable);
    }

    public void save(Post post) {
        postRepository.save(post);
    }
}
