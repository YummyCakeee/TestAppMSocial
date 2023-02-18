package ru.nikita.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nikita.test.models.Movie;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, Integer> {
    Optional<Movie> findMovieByTitle(String title);

    @Query(value = "SELECT * FROM movies m WHERE m.id NOT IN (SELECT movie_id FROM user_movie WHERE user_id = ?1)",
            nativeQuery = true)
    List<Movie> findNotInFavourites(Integer userId);
}
