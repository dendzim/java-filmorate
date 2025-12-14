DELETE FROM mpa;
DELETE FROM genres;

ALTER TABLE genres ALTER COLUMN genre_id RESTART WITH 1;
ALTER TABLE mpa ALTER COLUMN mpa_id RESTART WITH 1;

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