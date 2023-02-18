package ru.nikita.test.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import ru.nikita.test.models.Movie;

import java.util.List;

public class UserDTO {

    private int id;

    @NotEmpty(message = "Username should not be empty")
    @Pattern(regexp = "[A-z]+", message = "Only latin characters are allowed in the username")
    private String username;

    private String name;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Incorrect email address")
    private String email;

    public UserDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
