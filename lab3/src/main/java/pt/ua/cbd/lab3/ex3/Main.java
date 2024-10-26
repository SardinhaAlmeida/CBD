package pt.ua.cbd.lab3.ex3;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

public class Main {
    
    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder().build()) {                                  // (1)
            ResultSet rs = session.execute("select release_version from system.local");              // (2)
            Row row = rs.one();
            System.out.println(row.getString("release_version"));                                    // (3)

            session.execute("USE videos_ex2");                                                                   
            AlineaA(session);
            AlineaB(session);
        }
    }

    public static void AlineaA(CqlSession session) {
        System.out.println("------------------Alinea A------------------");
        insert(session);
        edits(session);
        search(session);

    }
    public static void AlineaB(CqlSession session) {
        System.out.println("\n------------------Alinea B------------------");
        query2(session);
        query3(session);
        query4(session);
        query7(session);
    }

    public static void insert(CqlSession session){
        String query1 = "INSERT INTO video (video_id, autor_partilha, nome, descricao, tags, selo_upload) VALUES (18, 'sardinha', 'Trying to use java driver for cassandra', 'cassandra', {'Aveiro', 'university'}, toTimestamp(now()))";
        String query2 = "INSERT INTO user (username, nome, email, selo_registo) VALUES ('bla','bla','blabla@gmail.com', toTimestamp(now()))";
        String query3 = "INSERT INTO coments_video (coment_id, autor, video_id, coment , momento_temporal) VALUES (11, 'tiago', 18, 'isso trabalha', toTimestamp(now()))";
        
        try{
            session.execute(query1);
            session.execute(query2);
            session.execute(query3); 

            System.out.println("Inserts successful");

        } catch (Exception e) {
            System.out.println("ERROR: Inserts failed" + e.getMessage());
            System.exit(1);
        }

    }

    public static void edits(CqlSession session){
        String query1 = "UPDATE video SET nome =  'Trying to use java driver for cassandra for cbd course' WHERE video_id = 18";
        String query2 = "UPDATE user SET email =  'sardinhafa@gmail.pt' WHERE username = 'sardinha'";
        String query3 = "UPDATE rating SET rating = 4 WHERE rating_id = 14 AND video_id = 2";
        
        try{
            session.execute(query1);
            session.execute(query2);
            session.execute(query3); 

            System.out.println("Edits successful");

        } catch (Exception e) {
            System.out.println("ERROR: Edits failed" + e.getMessage());
            System.exit(1);
        }

    }

    public static void search(CqlSession session){
            String query1 = "SELECT * FROM video";
            String query2 = "SELECT * FROM rating WHERE video_id = 17";
            String query3 = "SELECT * FROM followers WHERE video_id = 11";
            
            try{
                System.out.println("\nAll Videos");
                for (Row row : session.execute(query1)) {
                    System.out.printf("%-10d %-25.25s %-45.45s %-40.40s %-30.30s %-20s\n",
                            row.getInt("video_id"),
                            row.getString("autor_partilha"),
                            row.getString("nome"),
                            row.getString("descricao"),
                            row.getSet("tags", String.class),
                            row.getObject("selo_upload"));
                }

                System.out.println("\nAll Ratings from video 17");
                for (Row row : session.execute(query2)) {
                    System.out.printf("%-5d %-5.5s %-5.5s\n",
                            row.getInt("rating_id"),
                            row.getInt("video_id"),
                            row.getInt("rating"));
                }

                System.out.println("\nAll followers from video 11");
                for (Row row : session.execute(query3)) {
                    System.out.printf("%-5d %-90.90s\n",
                            row.getInt("video_id"),
                            row.getSet("follower", String.class));
                }


                System.out.println("Selects successful");

            } catch (Exception e) {
                System.out.println("ERROR: Selects failed" + e.getMessage());
                System.exit(1);
            }

        }

    public static void query2(CqlSession session){
        String query = "SELECT tags FROM video WHERE video_id = 3;";
        try{
            System.out.println("\nQUERY2: Lista das tags de determinado video");
                for (Row row : session.execute(query)) {
                    System.out.printf("%-90.90s \n",
                            row.getSet("tags", String.class));
                }

        } catch (Exception e) {
            System.out.println("ERROR: Query2 failed" + e.getMessage());
            System.exit(1);
        }

    }

    public static void query3(CqlSession session){
        String query = "SELECT * FROM video_tag WHERE tag = 'Aveiro';";
        try{
            System.out.println("\nQUERY3: Todos os videos com a tag Aveiro");
                for (Row row : session.execute(query)) {
                    System.out.printf("%-20.20s %-10d \n",
                            row.getString("tag"),
                            row.getInt("video_id"));
                }

        } catch (Exception e) {
            System.out.println("ERROR: Query3 failed" + e.getMessage());
            System.exit(1);
        }

    }

    public static void query4(CqlSession session) {
        String query = "SELECT * FROM event WHERE video_id = 7 and autor = 'sardinha' LIMIT 5;";
        try {
            System.out.println("\nQUERY4:  Os ultimos 5 eventos de determinado video realizados por um utilizador");
            for (Row row : session.execute(query)) {
                System.out.printf("%-5d %-10.10s %-5.5s %-30.30s %-30.30s\n",
                            row.getInt("video_id"),
                            row.getString("autor"),
                            row.getString("event_type"),
                            row.getObject("event_timestamp"),
                            row.getObject("moment"));
            }

        } catch (Exception e) {
            System.out.println("ERROR: Query4 failed" + e.getMessage());
            System.exit(1);
        }

    }

    public static void query7(CqlSession session) {
        String query = "SELECT * FROM followers WHERE video_id = 17;";
        try {
            System.out.println("\nQUERY7:  Todos os seguidores (followers) de determinado video");
            for (Row row : session.execute(query)) {
                System.out.printf("%-5d %-90.90s\n",
                        row.getInt("video_id"),
                        row.getSet("follower", String.class));
            }

        } catch (Exception e) {
            System.out.println("ERROR: Query7 failed" + e.getMessage());
            System.exit(1);
        }

    }

    
}