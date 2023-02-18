package ru.nikita.test.dto;

import jakarta.validation.constraints.NotEmpty;
import ru.nikita.test.models.Movie;
import java.util.Objects;

public class MovieDTO {
    private int id;

    @NotEmpty(message = "Title should not be empty")
    private String title;

    private String posterPath;

    public MovieDTO(String title, String posterPath) {
        this.title = title;
        this.posterPath = posterPath;
    }

    public MovieDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDTO movieDTO = (MovieDTO) o;
        return id == movieDTO.id && Objects.equals(title, movieDTO.title) && Objects.equals(posterPath, movieDTO.posterPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, posterPath);
    }
}
