package cbd.java_driver.Ex3;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;

import static com.mongodb.client.model.Projections.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ex3 {

    private static MongoCollection<Document> collection;

    public static void main(String[] args) {

        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

        // Replace the placeholder with your MongoDB deployment's connection string
        String uri = "mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.0.1";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("cbd");
            collection = database.getCollection("restaurants");

            // *Alínea a*//
            System.out.println("\n____________________Alínea a____________________\n");
            alinea_a();

            // *Alínea b*//
            System.out.println("\n____________________Alínea b____________________\n");
            alinea_b();

            // *Alínea c*//
            System.out.println("\n____________________Alínea c____________________\n");
            alinea_c();

            // *Alínea d*//
            System.out.println("\n____________________Alínea d____________________\n");
            alinea_d();

            mongoClient.close();
        }
    }

    public static void alinea_a() {
        // Documento para inserir e depois editar
        Document doc_insert = new Document("address", new Document("building", "182")
                .append("coord", Arrays.asList(40.5747340, -8.4436850))
                .append("rua", "Av. 10 de Junho ")
                .append("zipcode", "3660-261"))
                .append("localidade", "Santa Cruz da Trapa")
                .append("gastronomia", "Portuguesa")
                .append("grades", new Document("date", new Document("$date", 1409616000000L))
                        .append("grade", "A")
                        .append("score", 12))
                .append("nome", "Manjar das Letras")
                .append("restaurant_id", "5000123");

        System.out.println("------------Inserção e pesquisa------------");
        // Inserção
        collection.insertOne(doc_insert);
        // Pesquisa
        Document doc1 = collection.find(Filters.eq("nome", "Manjar das Letras")).first();

        // Print
        if (doc1 != null) {
            System.out.println(doc1.toJson() + "\n");
        } else {
            System.out.println("No matching documents found.");
        }

        System.out.println("------------Edição e pesquisa------------");
        // Edição
        collection.updateOne(Filters.eq("restaurant_id", "5000123"), Updates.set("grades.score", 17));// need to

        // Pesquisa
        Document doc2 = collection.find(Filters.eq("nome", "Manjar das Letras")).first();

        // Print
        if (doc2 != null) {
            System.out.println(doc2.toJson() + "\n");
        } else {
            System.out.println("No matching documents found.");
        }

        collection.deleteOne(Filters.eq("restaurant_id", "5000123")); // just to delete the one I inserted
    }

    public static void alinea_b() {
        long start_time, end_time; // para testar o desempenho

        // LOCALIDADE
        start_time = System.currentTimeMillis();
        collection.find(Filters.eq("localidade", "Manhattan")); // Manhattan é o mais comum
        end_time = System.currentTimeMillis();
        System.out.println("Tempo de pesquisa sem index - localidade: " + (end_time - start_time) + "ms");

        // Criação do index
        collection.createIndex(Indexes.descending("localidade"));
        start_time = System.currentTimeMillis();
        collection.find(Filters.eq("localidade", "Manhattan"));
        end_time = System.currentTimeMillis();
        System.out.println("Tempo de pesquisa com index - localidade: " + (end_time - start_time) + "ms");

        // GASTRONOMIA
        start_time = System.currentTimeMillis();
        collection.find(Filters.eq("gastronomia", "American")); // American é o mais comum
        end_time = System.currentTimeMillis();
        System.out.println("Tempo de pesquisa sem index - gastronomia: " + (end_time - start_time) + "ms");

        // Criação do index
        collection.createIndex(Indexes.ascending("gastronomia"));
        start_time = System.currentTimeMillis();
        collection.find(Filters.eq("gastronomia", "American"));
        end_time = System.currentTimeMillis();
        System.out.println("Tempo de pesquisa com index - gastronomia: " + (end_time - start_time) + "ms");

        // NOME
        start_time = System.currentTimeMillis();
        collection.find(Filters.eq("nome", "Dunkin Donuts"));
        end_time = System.currentTimeMillis();
        System.out.println("Tempo de pesquisa sem index - nome: " + (end_time - start_time) + "ms");

        // Criação do index
        collection.createIndex(Indexes.text("nome"));
        start_time = System.currentTimeMillis();
        collection.find(Filters.eq("nome", "Dunkin Donuts"));
        end_time = System.currentTimeMillis();
        System.out.println("Tempo de pesquisa com index - nome: " + (end_time - start_time) + "ms");

        // collection.listIndexes().forEach(doc -> System.out.println(doc.toJson()));
        collection.dropIndexes();
    }

    public static void alinea_c() {
        // 6. Liste todos os restaurantes que tenham pelo menos um score superior a 85.
        System.out.println("------------6. Restaurantes com pelo menos um score superior a 85------------\n");
        FindIterable<Document> result_6 = collection.find(Filters.gt("grades.score", 85));

        for (Document doc : result_6) {
            System.out.println(doc.toJson());
        }

        // 11. Liste o nome, a localidade e a gastronomia dos restaurantes que pertencem
        // ao Bronx e cuja gastronomia é do tipo "American" ou "Chinese".
        System.out.println(
                "\n------------11. Restaurantes do Bronx com gastronomia American ou Chinese------------\n");
        FindIterable<Document> result_11 = collection.find(Filters.and(Filters.eq("localidade", "Bronx"),
                Filters.or(Filters.eq("gastronomia", "American"), Filters.eq("gastronomia", "Chinese"))))
                .projection((fields(
                        include("nome", "localidade", "gastronomia"),
                        exclude("_id"))));

        for (Document doc : result_11) {
            System.out.println(doc.toJson());
        }

        // 13. Liste o nome, a localidade, o score e gastronomia dos restaurantes que
        // alcançaram sempre pontuações inferiores ou igual a 3.
        System.out.println("\n------------13. Restaurantes com score inferior ou igual a 3------------\n");
        FindIterable<Document> result_13 = collection
                .find(Filters.not(Filters.elemMatch("grades", Filters.gt("score", 3))))
                .projection((fields(
                        include("nome", "localidade", "gastronomia", "grades.score"),
                        exclude("_id"))));

        for (Document doc : result_13) {
            System.out.println(doc.toJson());
        }

        // 20. Apresente o nome e número de avaliações (numGrades) dos 3 restaurante com
        // mais avaliações.
        System.out.println("\n------------20. 3 restaurantes com mais avaliações------------\n");
        AggregateIterable<Document> result_20 = collection.aggregate(Arrays.asList(
                Aggregates.unwind("$grades"),
                Aggregates.group("$nome", Accumulators.sum("numGrades", 1)),
                Aggregates.sort(Indexes.descending("numGrades")),
                Aggregates.limit(3),
                Aggregates.project(fields(include("nome", "numGrades")))));

        for (Document doc : result_20) {
            System.out.println(doc.toJson());
        }

        // 24. Apresente o número de gastronomias diferentes na rua "Fifth Avenue"
        System.out.println("\n------------24. Número de gastronomias diferentes na rua Fifth Avenue------------\n");
        AggregateIterable<Document> result_24 = collection.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("address.rua", "Fifth Avenue")),
                Aggregates.group("$gastronomia"),
                Aggregates.group(null, Accumulators.sum("count", 1))));

        for (Document doc : result_24) {
            System.out.println(doc.toJson());
        }
    }

    public static void alinea_d() {
        System.out.println("Número de localidades distintas:" + countLocalidades());

        System.out.println("\nNumero de restaurantes por localidade:");
        Map<String, Integer> rest_local = countRestByLocalidade();
        for (String key : rest_local.keySet()) {
            System.out.println(" -> " + key + " - " + rest_local.get(key));
        }

        System.out.println("\nNome de restaurantes contendo 'Park' no nome:");
        List<String> rest_park = getRestWithNameCloserTo("Park");
        for (String rest : rest_park) {
            System.out.println(" -> " + rest);
        }

    }

    public static int countLocalidades() { 

        List<String> result = collection.distinct("localidade", String.class).into(new ArrayList<>());

        return result.size();    }

    public static Map<String, Integer> countRestByLocalidade() {

        Map<String, Integer> rest_local = new HashMap<>();

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                Aggregates.group("$localidade", Accumulators.sum("totalRes", 1))));

        for (Document document : result) {
            String localidade = document.getString("_id");
            int totalRestaurants = document.getInteger("totalRes");
            rest_local.put(localidade, totalRestaurants);
        }

        return rest_local;
    }

    public static List<String> getRestWithNameCloserTo(String name) {
        List<String> rest = new ArrayList<>();

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                Aggregates.match(Filters.regex("nome", name)),
                Aggregates.project(fields(include("nome"), exclude("_id")))));

        for (Document document : result) {
            rest.add(document.getString("nome"));
        }

        return rest;
    }

}