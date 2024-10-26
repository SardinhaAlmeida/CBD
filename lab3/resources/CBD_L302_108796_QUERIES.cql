// 7. Permitir a pesquisa de todos os videos de determinado autor;
SELECT * FROM video_autor WHERE autor_partilha = 'aoki';

// 8. Permitir a pesquisa de comentarios por utilizador, ordenado inversamente pela data;
SELECT * FROM coments_user WHERE autor = 'tiago';

// 9. Permitir a pesquisa de comentarios por videos, ordenado inversamente pela data;
SELECT * FROM coments_video WHERE video_id = 9;

// 10. Permitir a pesquisa do rating medio de um video e quantas vezes foi votado;
SELECT avg(rating) as average_rating, count(rating) as count_ratings FROM rating WHERE video_id = 17;

// 1. Os ultimos 3 comentarios introduzidos para um video;
SELECT * FROM coments_video WHERE video_id = 9 LIMIT 3;

// 2. Lista das tags de determinado video;
SELECT tags FROM video WHERE video_id = 3;

// 3. Todos os videos com a tag Aveiro;
#Esta query só é possível com a criação de um índice na tabela video
CREATE INDEX IF NOT EXISTS ON video(tags);
SELECT * FROM video WHERE tags CONTAINS 'Aveiro';
#De outra forma, só seria possível com a criação de uma tabela auxiliar como a apresentada na alínea 11.

// 4. Os ultimos 5 eventos de determinado video realizados por um utilizador;
SELECT * FROM event WHERE video_id = 7 and autor = 'sardinha' LIMIT 5;

// 5. Videos partilhados por determinado utilizador (maria1987, por exemplo) num determinado periodo de tempo (Agosto de 2017, por exemplo);
SELECT * FROM video_autor WHERE autor_partilha = 'tiago' and selo_upload >= '2023-11-01' AND selo_upload <= '2023-11-30';

// 6. Os ultimos 10 videos, ordenado inversamente pela data da partilhada;
#Não é possível em Cassandra, pois a primary key da tabela videos é o video_id e 
#esta query não define nenhuma restrição para a partition key, não há uma clásusula 'WHERE'.
#No fundo, não é possível porque o Cassandra não permite global querying.

// 7. Todos os seguidores (followers) de determinado video;
SELECT * FROM followers WHERE video_id = 17;

// 8. Todos os comentarios (dos videos) que determinado utilizador esta a seguir (following);
#Para realizar esta alínea seria necessário fazer join entre as tabelas coments_video e following, o que não é possível em Cassandra.
#Poderia também criar-se um tabela auxiliar que guardasse os comentários dos videos que os utilizadores estão a seguir,por exemplo;
#Outra abordagem seria fazer-se uma query sobre outra query.

// 9. Os 5 videos com maior rating;
#Semelhante à alínea 6, não é possível em Cassandra porque esta não permite global querying.

// 10. Uma query que retorne todos os videos e que mostre claramente a forma pela qual estao ordenados;
#Semelhante à alínea 6, não é possível em Cassandra porque esta não permite global querying.

// 11. Lista com as Tags existentes e o numero de videos catalogados com cada uma delas;
#Não é possível em Cassandra, pois esta não permite consultas de agregação complexas, ou seja, a funcionalidade GROUP BY só aceita a partition key.
#Seria possível com a criação de uma tabela auxiliar que guardasse as tags e os vídeos catalogados com cada uma delas, cmo por exemplo:
-- CREATE TABLE video_tag (
--     video_id int,
--     tag text,
--     PRIMARY KEY (tag, video_id)  
-- );
#Desta forma, seria possível fazer a seguinte query:
#SELECT tag, count(video_id) as num_videos FROM video_tag GROUP BY tag;

// 12. Lista de todos os ratings para um determinado video;
SELECT * FROM rating WHERE video_id = 2;

// 13. Lista de todos os eventos 'stop' de um determinado video feitos por determinado utilizador;
SELECT * FROM event WHERE video_id = 7 and autor = 'sardinha' and event_type = 'stop';
