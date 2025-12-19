INSERT INTO genres (name) VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');

INSERT INTO mpa (name) VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');

INSERT INTO users (email, login, name, birthday) VALUES
('почта1', 'логин1', 'имя1', '1999-01-01'),
('почта2', 'логин2', 'имя2', '2000-01-02'),
('почта3', 'логин3', 'имя3', '2002-02-01'),
('почта4', 'логин4', 'имя4', NULL);

INSERT INTO user_friends (user_id, friend_id) VALUES
(1, 2),
(2, 1),
(4, 2),
(3, 1);

INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES
('имя1', 'описание1', '2002-01-10', 120, 1),
('имя2', 'описание2', '2005-05-01', 100, 2),
('имя3', 'описание3', '2006-01-01', 140, 3),
('имя4', 'описание4', '2010-06-01', 90, 4),
('имя5', 'описание5', '2000-08-01', 180, 5);

INSERT INTO films_genres (film_id, genre_id) VALUES
(4,1),
(5,2),
(5,3),
(5,1),
(3,3);

INSERT INTO likes (film_id, user_id) VALUES
(1,1),
(1,2),
(2,1),
(2,2),
(2,3),
(3,1),
(3,2),
(3,3),
(3,4);