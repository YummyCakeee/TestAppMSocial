package ru.nikita.test.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nikita.test.models.Movie;
import ru.nikita.test.repositories.MoviesRepository;
import ru.nikita.test.services.MoviesService;
import ru.nikita.test.utils.imdbResponse.ImdbResponse;
import ru.nikita.test.utils.imdbResponse.Results;

import java.util.*;

@Component
public class ScheduledTasks {

    private final MoviesService moviesService;
    @Value("${imdb.access-token}")
    private String imdbApiKey;

    @Autowired
    public ScheduledTasks(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @Scheduled(fixedDelay = 3 * 60 * 60 * 1000)
    public void getIMdbMovies() {
        RestTemplate restTemplate = new RestTemplate();
        int itemsPerPage = 5;
        int pageCount = 5;
        Set<Movie> movies = new HashSet<>();

        for (int i = 0; i < pageCount; i++) {
            String url = String.format(
                    "https://imdb-api.com/API/AdvancedSearch/%s?groups=now-playing-us&count=%d&page=%d",
                    imdbApiKey, itemsPerPage, i + 1);
            ImdbResponse response = restTemplate.getForObject(url, ImdbResponse.class);
            if (Objects.nonNull(response)) {
                List<Results> results = response.getResults();
                if (Objects.nonNull(results))
                    for (Results result : results) {
                        movies.add(new Movie(result.getTitle(), result.getImage()));
                    }
            }
        }

        movies.removeIf(movie -> moviesService.findMovieByTitle(movie.getTitle()).isPresent());
        if (!movies.isEmpty())
            moviesService.saveAllMovies(movies.stream().toList());
    }
}
