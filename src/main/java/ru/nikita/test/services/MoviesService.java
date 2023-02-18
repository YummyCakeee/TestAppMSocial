package ru.nikita.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikita.test.models.Movie;
import ru.nikita.test.repositories.MoviesRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MoviesService {

    private final MoviesRepository moviesRepository;

    @Autowired
    public MoviesService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @Transactional
    public void saveAllMovies(List<Movie> movies) {
        moviesRepository.saveAll(movies);
    }

    public List<Movie> findAll(int page, int itemsPerPage) {
        return moviesRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }
    public Optional<Movie> findMovieByTitle(String title) {
        return moviesRepository.findMovieByTitle(title);
    }
}
