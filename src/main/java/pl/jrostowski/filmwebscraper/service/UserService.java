package pl.jrostowski.filmwebscraper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jrostowski.filmwebscraper.entity.User;
import pl.jrostowski.filmwebscraper.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Page<User> findAllByOrderByIdAsc(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return userRepository.findAllByOrderByUserIdAsc(pageable);
    }

    public User findByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void toggleRole(Long id) {
        User user = userRepository.findByUserId(id);
        if (user.getRole().equals("ROLE_ADMIN")) {
            user.setRole("ROLE_USER");
        } else {
            user.setRole("ROLE_ADMIN");
        }
        userRepository.save(user);
    }
}
