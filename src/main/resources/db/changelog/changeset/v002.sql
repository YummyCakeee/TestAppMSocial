CREATE TABLE User_Movie (
    user_id int REFERENCES Users(id) ON DELETE CASCADE,
    movie_id int REFERENCES Movies(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, movie_id)
);