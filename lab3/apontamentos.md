* comandos para introduzir as tabelas e os dados na shell do cassandra:
# cat resources/CBD_L302_108796_DDL.cql | cqlsh
# cat resources/CBD_L302_108796_SEEDDATA.cql | cqlsh

- acrescentei o autor na primary key da tabela coments_video, porque se fosse só o video_id e o momento temporal,*não podia haver mais que um comentário no mesmo instante*

# cat resources/ex4/CBD_L304_108796_DDL.cql | cqlsh
# cat resources/ex4/CBD_L304_108796_SEEDDATA.cql | cqlsh