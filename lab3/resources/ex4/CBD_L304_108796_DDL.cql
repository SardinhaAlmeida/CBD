DROP KEYSPACE IF EXISTS puzzle_shop;
CREATE KEYSPACE puzzle_shop WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};
USE puzzle_shop;

DROP TABLE IF EXISTS puzzle;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS solved_puzzles;
DROP TABLE IF EXISTS coments_ratings;
DROP TABLE IF EXISTS liked_puzzles;

CREATE TABLE puzzle (
    id int,
    subject text,
    piece_number int,
    price float,
    tag set<text>,
    PRIMARY KEY (id)
);

CREATE TABLE users(
    username text PRIMARY KEY,
    name text,
    email text
);      

CREATE TABLE solved_puzzles (
    username text PRIMARY KEY,
    puzzle_id list<int>,
    date_solved map<int, date>,
);

CREATE TABLE coments_ratings (
    username text,
    puzzle_id int,
    coment text,
    rating int,
    time_of timestamp,
    PRIMARY KEY (puzzle_id, time_of, username)
);

CREATE TABLE liked_puzzles (
    username text PRIMARY KEY,
    puzzle_id list<int>
);

DROP INDEX IF EXISTS pn;
DROP INDEX IF EXISTS pt;
DROP INDEX IF EXISTS r;

CREATE INDEX pn ON puzzle(piece_number);
CREATE INDEX pt ON puzzle(tag);
CREATE INDEX r ON coments_ratings(rating);
