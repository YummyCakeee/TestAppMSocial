package ru.nikita.test.services;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikita.test.models.Movie;
import ru.nikita.test.models.User;
import ru.nikita.test.repositories.MoviesRepository;
import ru.nikita.test.repositories.UsersRepository;
import ru.nikita.test.utils.BadRequestException;
import ru.nikita.test.utils.enums.LoaderType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final MoviesRepository moviesRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, MoviesRepository moviesRepository) {
        this.usersRepository = usersRepository;
        this.moviesRepository = moviesRepository;
    }

    @Transactional
    public void addUser(User user) {
        usersRepository.save(user);
    }

    public Optional<User> getUserInfoById(int id) {
        return usersRepository.findById(id);
    }

    @Transactional
    public void updateUser(User user, int id) {
        user.setId(id);
        usersRepository.save(user);
    }

    @Transactional
    public void deleteUserById(int id) {
        usersRepository.deleteById(id);
    }

    @Transactional
    public void addMovieToUsersFavourites(int userId, int movieId) throws BadRequestException {
        Optional<User> user = usersRepository.findById(userId);
        Optional<Movie> movie = moviesRepository.findById(movieId);
        if (user.isPresent() && movie.isPresent()) {
            User foundUser = user.get();
            foundUser.getMovies().add(movie.get());
        }
        else {
            throw new BadRequestException("Could not find user or movie by id");
        }
    }

    @Transactional
    public void removeMovieFromFavourites(int userId, int movieId) throws  BadRequestException {
        Optional<User> user = usersRepository.findById(userId);
        Optional<Movie> movie = moviesRepository.findById(movieId);
        if (user.isPresent() && movie.isPresent()) {
            User foundUser = user.get();
            foundUser.getMovies().remove(movie.get());
        }
        else {
            throw new BadRequestException("Could not find user or movie by id");
        }
    }

    public List<Movie> getNotInFavouritesMovies(int userId, LoaderType loaderType) {
        return switch (loaderType) {
            case SQL -> getNotInFavouritesWithSQL(userId);
            default -> getNotInFavouritesWithMemory(userId);
        };
    }

    public List<Movie> getNotInFavouritesWithSQL(int userId) {
        return moviesRepository.findNotInFavourites(userId);
    }

    public List<Movie> getNotInFavouritesWithMemory(int userId) {
        User user = usersRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
        Hibernate.initialize(user.getMovies());
        List<Movie> movies = moviesRepository.findAll();
        return  movies.stream().filter(movie ->
                        user.getMovies().stream().noneMatch(userMovie -> userMovie.getId() == movie.getId()))
                .collect(Collectors.toList());
    }
}
