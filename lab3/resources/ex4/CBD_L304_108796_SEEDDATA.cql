USE puzzle_shop;

INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (1, 'The Starry Night', 1000, 70, {'Van Gogh', 'art', 'painting'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (2, 'New York', 250, 20, {'city', 'USA'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (3, 'Harry Potter', 1000, 40, {'Harry Potter', 'movies', 'fantasy'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (4, 'Mountain view', 500, 30, {'mountains', 'nature', 'view'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (5, 'The Hudson River', 350, 20, {'river', 'nature', 'view'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (6, 'Office', 1500, 60, {'office', 'work', 'people'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (7, 'Star Wars', 1000, 40, {'Star Wars', 'movies', 'fantasy'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (8, 'Christmas', 500, 30, {'Christmas', 'holidays', 'people'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (9, 'Beach',1000, 30, {'beach', 'nature', 'view'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (10, 'Harvard University', 750, 30, {'university', 'USA', 'people'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (11, 'Portugal', 1000, 35, {'Portugal', 'culture', 'countries'});
INSERT INTO puzzle(id, subject, piece_number, price, tag) VALUES (12, 'Aveiro', 750, 25, {'Aveiro', 'culture', 'city', 'Portugal'});

INSERT INTO users (username, name, email) VALUES ('sardinha','sara','sardinha@gmail.com');
INSERT INTO users (username, name, email) VALUES ('julia','luana','lualm@gmail.com');
INSERT INTO users (username, name, email) VALUES ('roberto.piloto','roberto','roberto@gmail.com');
INSERT INTO users (username, name, email) VALUES ('tiago','tiago','tiago@gmail.com');
INSERT INTO users (username, name, email) VALUES ('aoki','aoki','aaoki@gmail.com');
INSERT INTO users (username, name, email) VALUES ('jacke','joaquim','joaquim@gmail.com');
INSERT INTO users (username, name, email) VALUES ('rafilsons','rafaela','rafaela@gmail.com');
INSERT INTO users (username, name, email) VALUES ('martita','marta','marta@gmail.com');
INSERT INTO users (username, name, email) VALUES ('lola','matilde','matilde@gmail.com');
INSERT INTO users (username, name, email) VALUES ('papi','renato','renato@gmail.com');
INSERT INTO users (username, name, email) VALUES ('numeiro','hugo','hugo@gmail.com');
INSERT INTO users (username, name, email) VALUES ('andre','andre','andre@gmail.com');

INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('sardinha', [3, 6, 8, 12], {3: '2020-01-01', 6: '2021-05-02', 8: '2022-12-03', 12: '2023-01-04'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('julia', [3, 6, 8], {3: '2020-01-01', 6: '2021-05-02', 8: '2022-12-03'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('aoki', [3, 7, 10], {3: '2021-01-01', 7: '2021-06-30', 10: '2023-08-24', 12: '2023-01-04'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('roberto.piloto', [4, 8, 9], {4: '2020-11-09', 8: '2023-01-02', 9: '2022-09-03'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('tiago', [8, 11], {8: '2023-12-24', 11: '2018-06-07'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('jacke', [4], {4: '2010-08-03'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('rafilsons', [1, 8], {1: '2023-09-13', 8: '2022-12-03'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('martita', [10], {10: '2020-01-01'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('lola', [6, 10], {6: '2022-07-02', 10: '2023-02-14'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('papi', [2, 5], {2: '2011-11-11', 5: '2021-04-12'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('numeiro', [12], {12: '2014-12-21'});
INSERT INTO solved_puzzles(username, puzzle_id, date_solved) VALUES ('andre', [3], {3: '2013-05-01'});

INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('sardinha', 3, 'Adorei! está até hoje na minha mesa da sala', 5, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('sardinha', 8, 'Muito fofoooo, mas gosto de puzzles com mais peças', 4, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('julia', 6, 'muito aesthetic e com muitas peças amei', 5, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('roberto.piloto', 9, 'praia linda mas muito difícil', 4, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('tiago', 11, 'muito giro mas não tenho paciência para isto', 3, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('aoki', 7, 'adoro star wars e este puzzle é muito giro', 5, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('jacke', 4, 'muito giro e fácil', 5, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('rafilsons', 1, 'muito lindooooo', 5, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('martita', 10, 'muito giro', 4, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('lola', 6, 'muito giro mas muito trabalhoso', 4, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('papi', 5, 'não é muito giro ao vivo', 3, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('numeiro', 12, 'muito giro e fácil como eu gosto', 5, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('andre', 3, 'bué fácil fiz em 1 dia', 5, toTimestamp(now()));
INSERT INTO coments_ratings(username, puzzle_id, coment, rating, time_of) VALUES ('sardinha', 6, 'gostei muito', 4, toTimestamp(now()));

INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('sardinha', [3, 6, 8, 12]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('julia', [3, 6, 8]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('aoki', [7]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('roberto.piloto', [4, 8]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('tiago', [8]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('jacke', [4]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('rafilsons', [1, 8]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('martita', [10]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('lola', [10]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('papi', [2]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('numeiro', [12]);
INSERT INTO liked_puzzles(username, puzzle_id) VALUES ('andre', [3]);   

