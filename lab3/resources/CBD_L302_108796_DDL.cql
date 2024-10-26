DROP KEYSPACE IF EXISTS videos_ex2;
CREATE KEYSPACE videos_ex2 WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};
USE videos_ex2;

DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS video;
DROP TABLE IF EXISTS followers;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS rating;
DROP TABLE IF EXISTS video_autor;
DROP TABLE IF EXISTS coments_user;
DROP TABLE IF EXISTS coments_video;

CREATE TABLE user (
    username text PRIMARY KEY,
    nome text,
    email text,
    selo_registo timestamp
);

CREATE TABLE video (
    video_id int PRIMARY KEY,
    autor_partilha text,
    nome text,
    descricao text,
    tags SET<text>,
    selo_upload timestamp
);

CREATE TABLE followers (
    video_id int PRIMARY KEY,
    follower SET<text>
);

CREATE TABLE event(
    video_id int,
    autor text,
    event_type text,
    event_timestamp timestamp,
    moment timestamp,
    PRIMARY KEY ((video_id, autor), event_type, event_timestamp)
);

CREATE TABLE rating(
    rating_id int,
    video_id int,
    rating int,
    PRIMARY KEY (video_id, rating_id)
);

CREATE TABLE video_autor (
    video_id int,
    autor_partilha text,
    nome text,
    descricao text,
    tags SET<text>,
    selo_upload timestamp,
    PRIMARY KEY (autor_partilha, selo_upload, video_id)
);

CREATE TABLE coments_user (
    coment_id int,
    autor text,
    video_id int,
    coment text,
    momento_temporal timestamp,
    PRIMARY KEY (autor, momento_temporal)
) WITH CLUSTERING ORDER BY (momento_temporal DESC);


CREATE TABLE coments_video (
    coment_id int,
    autor text,
    video_id int,
    coment text,
    momento_temporal timestamp,
    PRIMARY KEY (video_id, momento_temporal, autor)
) WITH CLUSTERING ORDER BY (momento_temporal DESC);
