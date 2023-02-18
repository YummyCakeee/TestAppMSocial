package ru.nikita.test.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nikita.test.dto.MovieDTO;
import ru.nikita.test.dto.UserDTO;
import ru.nikita.test.models.Movie;
import ru.nikita.test.models.User;
import ru.nikita.test.services.UsersService;
import ru.nikita.test.utils.BadRequestException;
import ru.nikita.test.utils.BindingResultErrorsConverter;
import ru.nikita.test.utils.ErrorResponse;
import ru.nikita.test.utils.enums.LoaderType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger =  LoggerFactory.getLogger(UserController.class);
    private final UsersService usersService;
    private  final ModelMapper modelMapper;

    @Autowired
    public UserController(UsersService usersService, ModelMapper modelMapper) {
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/getCurrentUserInfo")
    public ResponseEntity<UserDTO> getUserInfo(@RequestHeader("User-Id") int userId) {
        logger.info("User got his info");
        Optional<User> user = usersService.getUserInfoById(userId);
        if (user.isPresent()) {
            UserDTO userDTO = modelMapper.map(user.get(), UserDTO.class);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        throw new BadRequestException("There is no such user");
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(BindingResultErrorsConverter.convertToString(bindingResult));
        }
        User user = modelMapper.map(userDTO, User.class);
        usersService.addUser(user);
        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> changeUserInfo(@RequestBody @Valid UserDTO userDTO,
                                                     BindingResult bindingResult, @RequestHeader("User-Id") int userId) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(BindingResultErrorsConverter.convertToString(bindingResult));
        }
        User user = modelMapper.map(userDTO, User.class);
        usersService.updateUser(user, userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable int id) {
        usersService.deleteUserById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/favourites/{movieId}")
    private ResponseEntity<HttpStatus> addMovieToFavourites(@RequestHeader("User-Id") int userId,
                                                            @PathVariable int movieId) {
        usersService.addMovieToUsersFavourites(userId, movieId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/favourites/{movieId}")
    private ResponseEntity<HttpStatus> removeMovieFromFavourites(@RequestHeader("User-Id") int userId,
                                                            @PathVariable int movieId) {
        usersService.removeMovieFromFavourites(userId, movieId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/notInFavourites")
    private ResponseEntity<List<MovieDTO>> getNotInFavouritesMovies(@RequestHeader("User-Id") int userId,
                                                                    @RequestParam(name = "loaderType") String query) {
        LoaderType loaderType = Objects.equals(query, "sql") ?
                LoaderType.SQL : LoaderType.IN_MEMORY;
        List<MovieDTO> movieDTOs = usersService.getNotInFavouritesMovies(userId, loaderType)
                .stream().map(el -> modelMapper.map(el, MovieDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(movieDTOs, HttpStatus.OK);
    }

    @ExceptionHandler
    public ErrorResponse exceptionHandler(BadRequestException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
