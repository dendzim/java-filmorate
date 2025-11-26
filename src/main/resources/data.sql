DELETE FROM PUBLIC."Rating";
DELETE FROM PUBLIC."Genre";

ALTER TABLE PUBLIC."Genre" ALTER COLUMN GENRE_ID RESTART WITH 1;
ALTER TABLE PUBLIC."Rating" ALTER COLUMN RATING_ID RESTART WITH 1;

INSERT INTO PUBLIC."Genre" (NAME) VALUES
('Ужасы'),
('Триллер'),
('Комедия'),
('Драма'),
('Фантастика'),
('Боевик');

INSERT INTO PUBLIC."Rating" (NAME) VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');