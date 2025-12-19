INSERT INTO mpa (name) VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');

INSERT INTO genres (name) VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');

INSERT INTO users (email, login, name, birthday) VALUES
('email1', 'login1', 'name1', '2005-01-02'),
('email2', 'login2', 'name2', '2006-01-03'),
('email3', 'login3', 'name3', '2007-01-04'),
('email4', 'login4', 'name4', '2008-01-05'),
('email5', 'login5', 'name5', NULL);

INSERT INTO user_friends(user_id, friend_id) VALUES
(1, 2),
(2, 1),
(3, 1),
(4, 2),
(3, 4);

INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES
('name1', 'description1', '2002-02-02', 100, 1),
('name2', 'description2', '2003-03-03', 90, 2),
('name3', 'description3', '2004-04-04', 150, 3),
('name4', 'description4', '2005-05-05', 140, 4),
('name5', 'description5', '2006-06-06', 120, 5),
('name6', 'description6', '2007-07-07', 120, 1);

INSERT INTO films_genres (film_id, genre_id) VALUES
(4,1),
(3,2),
(3,3),
(5,1),
(5,2),
(5,3);

INSERT INTO likes (film_id, user_id) VALUES
(1,1),
(1,5),
(2,1),
(2,3),
(2,4),
(3,2),
(3,3),
(3,1),
(3,4),
(3,5),
(6,4);