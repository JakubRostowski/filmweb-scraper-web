package pl.jrostowski.filmwebscraper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jrostowski.filmwebscraper.entity.Post;
import pl.jrostowski.filmwebscraper.entity.User;
import pl.jrostowski.filmwebscraper.forms.PostForm;
import pl.jrostowski.filmwebscraper.repository.PostRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Page<Post> getPosts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return postRepository.findAllByOrderByPostIdDesc(pageable);
    }

    public void save(PostForm postForm, User user) {
        Post post = new Post(postForm.getTitle(), postForm.getContent(), user);
        postRepository.save(post);
    }

    @Transactional
    public void toggleLike(User user, Long postId) {
        Post post = postRepository.findById(postId).get();
        List<User> likes = post.getLikes()
                .stream()
                .filter(like -> Objects.equals(like.getUserId(), user.getUserId())).collect(Collectors.toList());
        if (likes.isEmpty()) {
            post.getLikes().add(user);
        } else {
            for (User like : likes) {
                post.getLikes().remove(like);
            }
        }
    }
}
