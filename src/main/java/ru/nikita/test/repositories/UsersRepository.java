package ru.nikita.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nikita.test.models.Movie;
import ru.nikita.test.models.User;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

}
