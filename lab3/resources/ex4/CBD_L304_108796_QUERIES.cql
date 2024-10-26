#________________________________updates_______________________________
// Atualizar os puzzles resolvidos por determinado utilizador (adicionar um puzzle)
UPDATE solved_puzzles SET puzzle_id = puzzle_id + [12] WHERE username = 'jacke';

// Atualizar a data em que um puzzle foi resolvido por determinado utilizador (adicionar a data)
UPDATE solved_puzzles SET date_solved = date_solved + {12 : '2023-11-29'} WHERE username = 'jacke';

// Atualizar a data em que um puzzle foi resolvido por determinado utilizador (mudar a data)
UPDATE solved_puzzles SET date_solved[12] = '2023-11-30' WHERE username = 'jacke';

//Atulizar puzzles resolvidos por determinado utilizador (mudar a lista)
UPDATE solved_puzzles SET puzzle_id = [8,11,5] WHERE username = 'tiago';

//Atualizar tags de um puzzle (remover uma tag) 
UPDATE puzzle SET tag = tag + {'people'} WHERE id = 2;


#________________________________deletes_______________________________
#alguns estão feitos de acordo com os updates acima
// Apagar a data em que um puzzle foi resolvido por determinado utilizador
DELETE date_solved[1] FROM solved_puzzles WHERE username = 'rafilsons';

// Apagar um puzzle resolvido por determinado utilizador(puzzle adicionado acima)
DELETE puzzle_id[2] FROM solved_puzzles WHERE username = 'tiago';

// Apagar uma tag de um puzzle (tag adicionada acima)
DELETE tag['people'] FROM puzzle WHERE id = 2;

// Apagar um puzzle dos favoritos
DELETE puzzle_id[3] FROM liked_puzzles WHERE username = 'sardinha';

// Apagar uma tag de um puzzzle
DELETE tag['people'] FROM puzzle WHERE id = 10;


#_______________________________queries__________________________________
// 1. Lista de todos os subjects de puzzles
SELECT subject from puzzle;

// 2. Lista de todos os puzzles com a tag city
SELECT id from puzzle WHERE tag CONTAINS 'city';

// 3. Data em que determinado puzzle foi resolvido por determinado utilizador
SELECT date_solved[7] from solved_puzzles WHERE username = 'aoki';

// 4. Lista de todos os puzzles com 1000 peças
SELECT * from puzzle WHERE piece_number=1000;

// 5. Lista de todos os puzzles com rating 4
SELECT puzzle_id from coments_ratings WHERE rating=4;

// 6. Lista dos últimos 2 coments num determinado puzzle
SELECT coment from coments_ratings WHERE puzzle_id=6 ORDER BY time_of DESC LIMIT 2;

// 7. Lista dos puzzles que determinado user gostou
SELECT * from liked_puzzles WHERE username = 'sardinha';

// 8. Lista das avaliações de determinado puzzle depois de determinada data
SELECT * from coments_ratings WHERE puzzle_id = 6 and time_of > '2023-11-28 00:48:45.087000+0000' ;

// 9. Lista do preço e número de peças de todos os puzzles
SELECT id, price, piece_number FROM puzzle;

// 10. Lista de todos os puzzles resolvidos por determinado utilizador e a data em que foram resolvidos
SELECT date_solved from solved_puzzles WHERE username = 'aoki';
