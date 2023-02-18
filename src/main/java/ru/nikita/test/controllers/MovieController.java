package ru.nikita.test.controllers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nikita.test.dto.MovieDTO;
import ru.nikita.test.services.MoviesService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MoviesService moviesService;
    private final ModelMapper modelMapper;

    @Autowired
    public MovieController(MoviesService moviesService, ModelMapper modelMapper) {
        this.moviesService = moviesService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getMovies(@RequestParam(required = false, defaultValue = "0") int page,
                                                    @RequestParam(required = false, defaultValue = "15") int itemsPerPage) {
        List<MovieDTO> movieDTOs = moviesService.findAll(page, itemsPerPage)
                .stream().map(el -> modelMapper.map(el, MovieDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(movieDTOs, HttpStatus.OK);
    }
}
