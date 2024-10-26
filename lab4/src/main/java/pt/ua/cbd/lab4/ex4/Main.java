package pt.ua.cbd.lab4.ex4;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Query;
import org.neo4j.driver.Values;

import org.apache.commons.csv.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main implements AutoCloseable {

    private final Driver driver;

    public static void main(String... args) {
        try (var greeter = new Main("bolt://localhost:7687", "neo4j", "password")) {

            //My dataset represents a very simple social network, where users can publish posts and comment on others or their own posts.

            //change the path to the csv files
            greeter.insert_users("/home/saraalmeida/UA/ano3/CBD/lab4/src/main/resources/users.csv");
            greeter.insert_posts("/home/saraalmeida/UA/ano3/CBD/lab4/src/main/resources/posts.csv");
            greeter.insert_comments("/home/saraalmeida/UA/ano3/CBD/lab4/src/main/resources/comments.csv");
            greeter.relationships();

            try {
                //change the path to the output file
                FileWriter writer = new FileWriter("/home/saraalmeida/UA/ano3/CBD/lab4/src/main/resources/CBD_L44c_output.txt");
                greeter.queries(writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Main(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        driver.session().run("MATCH (n) DETACH DELETE n");
    }

    @Override
    public void close() throws RuntimeException {
        driver.close();
    }

    public void insert_users(String filePath) {
        try (var session = driver.session()){
            
            CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord record : parser) {
                int user_id = Integer.parseInt(record.get("user_id"));
                String name = record.get("name");
                int age = Integer.parseInt(record.get("age"));
                String location = record.get("location");

                session.run("CREATE (:User {user_id: $user_id, name: $name, age: $age, location: $location})",
                        Values.parameters("user_id", user_id, "name", name, "age", age, "location", location));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void insert_posts(String filePath) {
        try (var session = driver.session()){
            
            CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord record : parser) {
                int post_id = Integer.parseInt(record.get("post_id"));
                int user_id = Integer.parseInt(record.get("user_id"));
                String post = record.get("post");
                String created_at = record.get("created_at");
                
                session.run("CREATE (:Post {post_id: $post_id, user_id: $user_id, post: $post, created_at: $created_at})",
                        Values.parameters("post_id", post_id, "user_id", user_id, "post", post, "created_at", created_at));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insert_comments(String filePath) {
        try (var session = driver.session()) {
            
            CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord record : parser) {
                int comment_id = Integer.parseInt(record.get("comment_id"));
                int user_id = Integer.parseInt(record.get("user_id"));
                int post_id = Integer.parseInt(record.get("post_id"));
                String comment = record.get("comment");
                String created_at = record.get("created_at");
                
                session.run("CREATE (:Comment {comment_id: $comment_id, user_id: $user_id, post_id: $post_id, comment: $comment, created_at: $created_at})",
                        Values.parameters("comment_id", comment_id, "user_id", user_id, "post_id", post_id, "comment", comment, "created_at", created_at));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    public void relationships() {
        try (var session = driver.session()) {
            // Relationship: User - Published -> Post
            session.run("MATCH (u:User), (p:Post) WHERE u.user_id = p.user_id " +
                        "CREATE (u)-[:PUBLISHED]->(p)");

            // Relationship: User - Commented -> Comment
            session.run("MATCH (u:User), (c:Comment) WHERE u.user_id = c.user_id " +
                        "CREATE (u)-[:COMMENTED]->(c)");

            // Relationship: Comment - Commented_Post -> Post
            session.run("MATCH (c:Comment), (p:Post) WHERE c.post_id = p.post_id " +
                        "CREATE (c)-[:COMMENTED_POST]->(p)");
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public void queries(FileWriter writer) {

        try (var session = driver.session()){

            //query 1
            writer.write("#1: Lista de post e seus comentários");
            Query query1 = new Query("MATCH (c:Comment)-[:COMMENTED_POST]->(p:Post) WITH p, COLLECT(c.comment) as comments RETURN p.post_id, comments");
            var result1 = session.run(query1);
            while (result1.hasNext()) {
                var record = result1.next();
                writer.write("\n" + "p.post_id:" + record.get("p.post_id").asInt() + " " +
                                ", comments:" + record.get("comments").asList());       
            }
            writer.write("\n\n");  

            //query 2
            writer.write("#2: Lista dos users que comentaram o post do user com user_id=134");
            Query query2 = new Query("MATCH (u:User)-[:COMMENTED]->(c:Comment)-[:COMMENTED_POST]->(p:Post)<-[:PUBLISHED]-(u2:User{user_id:134}) RETURN p.post_id, c.comment, u.name");
            var result2 = session.run(query2);
            while (result2.hasNext()) {
                var record = result2.next();
                writer.write("\n" + "p.post_id:" + record.get("p.post_id").asInt() + " " +
                                ", c.comment:" + record.get("c.comment").asString() + " " +
                                ", u.name:" + record.get("u.name").asString());         
            }
            writer.write("\n\n");

            //query 3
            writer.write("#3: Lista de posts com mais de 2 comentários");
            Query query3 = new Query("MATCH (c:Comment)-[:COMMENTED_POST]->(p:Post) WITH p, COUNT(c) as num_comments WHERE num_comments>2 RETURN p.post_id, num_comments");
            var result3 = session.run(query3);
            while (result3.hasNext()) {
                var record = result3.next();
                writer.write("\n" + "p.post_id:" + record.get("p.post_id").asInt() + " " +
                                ", num_comments:" + record.get("num_comments").asInt());         
            }
            writer.write("\n\n");

            //query 4
            writer.write("#4: Lista de posts feitos depois de Novembro de 2023");
            Query query4 = new Query("MATCH (p:Post) WHERE date(p.created_at) > date('2023-11-01') RETURN p.post_id, p.created_at ORDER BY p.created_at ASC");
            var result4 = session.run(query4);
            while (result4.hasNext()) {
                var record = result4.next();
                writer.write("\n" + "p.post_id:" + record.get("p.post_id").asInt() + " " +
                                ", p.created_at:" + record.get("p.created_at").asString());         
            }
            writer.write("\n\n");

            //query 5
            writer.write("#5: Lista dos 20 comentários mais recentes");
            Query query5 = new Query("MATCH (c:Comment) RETURN c.comment_id, c.created_at ORDER BY c.created_at DESC LIMIT 20");
            var result5 = session.run(query5);
            while (result5.hasNext()) {
                var record = result5.next();
                writer.write("\n" + "c.comment_id:" + record.get("c.comment_id").asInt() + " " +
                                ", c.created_at:" + record.get("c.created_at").asString());         
            }
            writer.write("\n\n");

            //query 6
            writer.write("#6: Lista de users com a mesma localização que comentaram o mesmo post");
            Query query6 = new Query("MATCH (u:User)-[:COMMENTED]->(c:Comment)-[:COMMENTED_POST]->(p:Post)<-[:COMMENTED_POST]-(c2:Comment)<-[:COMMENTED]-(u2:User) WHERE u.location = u2.location AND c.post_id = c2.post_id AND u.user_id <> u2.user_id RETURN u.name, u2.name, u.location, c.post_id");
            var result6 = session.run(query6);
            while (result6.hasNext()) {
                var record = result6.next();
                writer.write("\n" + "u.name:" + record.get("u.name").asString() + " " +
                                ", u2.name:" + record.get("u2.name").asString() + " " +
                                ", u.location:" + record.get("u.location").asString() + " " +
                                ", c.post_id:" + record.get("c.post_id").asInt());         
            }
            writer.write("\n\n");

            //query 7
            writer.write("#7: User que fez mais posts");
            Query query7 = new Query("MATCH (u:User)-[:PUBLISHED]->(p:Post) WITH u, COUNT(p) as num_posts RETURN u.name, num_posts ORDER BY num_posts DESC LIMIT 1");
            var result7 = session.run(query7);
            while (result7.hasNext()) {
                var record = result7.next();
                writer.write("\n" + "u.name:" + record.get("u.name").asString() + " " +
                                ", num_posts:" + record.get("num_posts").asInt());         
            }
            writer.write("\n\n");

            //query 8
            writer.write("#8: Lista de posts com comentários feitos por users com idade entre os 20 e 30 anos");
            Query query8 = new Query("MATCH (u:User)-[:COMMENTED]->(c:Comment)-[:COMMENTED_POST]->(p:Post) WHERE u.age >= 20 AND u.age <= 30 RETURN p.post_id, c.comment, u.name, u.age ORDER BY u.age ASC");
            var result8 = session.run(query8);
            while (result8.hasNext()) {
                var record = result8.next();
                writer.write("\n" + "p.post_id:" + record.get("p.post_id").asInt() + " " +
                                ", u.name:" + record.get("u.name").asString() + " " +
                                ", u.age:" + record.get("u.age").asInt() + " " +
                                ", c.comment:" + record.get("c.comment").asString());   
            }
            writer.write("\n\n");

            //query 9
            writer.write("#9: Lista de users que publicaram um post e comentaram esse mesmo post");
            Query query9 = new Query("MATCH (u:User)-[:PUBLISHED]->(p:Post)<-[:COMMENTED_POST]-(c:Comment)<-[:COMMENTED]-(u2:User) WHERE u.user_id = u2.user_id RETURN u.user_id, u2.user_id, p.post_id, c.comment");
            var result9 = session.run(query9);
            while (result9.hasNext()) {
                var record = result9.next();
                writer.write("\n" + "u.user_id:" + record.get("u.user_id").asInt() + " " +
                                ", u2.user_id:" + record.get("u2.user_id").asInt() + " " +
                                ", p.post_id:" + record.get("p.post_id").asInt() + " " +
                                ", c.comment:" + record.get("c.comment").asString());       
            }
            writer.write("\n\n");

            //query 10
            writer.write("#10: Número de users que não fizeram nenhum comentário");
            Query query10 = new Query("MATCH (u:User) WHERE NOT (u)-[:COMMENTED]->(:Comment) WITH COUNT(u) as num_users RETURN num_users");
            var result10 = session.run(query10);
            while (result10.hasNext()) {
                var record = result10.next();
                writer.write("\n" + "num_users:" + record.get("num_users").asInt());      
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }

    }
}
