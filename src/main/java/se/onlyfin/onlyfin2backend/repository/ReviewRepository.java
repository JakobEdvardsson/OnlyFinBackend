package se.onlyfin.onlyfin2backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.onlyfin.onlyfin2backend.model.Review;
import se.onlyfin.onlyfin2backend.model.User;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    void deleteAllByAuthor(User author);

    void deleteAllByAuthorAndTarget(User author, User target);

    List<Review> findAllByTarget(User target);
}
