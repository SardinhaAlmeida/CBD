package cbd.java_driver.Ex4;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import static com.mongodb.client.model.Projections.*;

public class AttendingSystemB {

    private static final int LIMIT = 10;
    private static final Double TIMESLOT = 5.0;

    public static Scanner user_input;

    private static MongoCollection<Document> collection;

    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

        // Replace the placeholder with your MongoDB deployment's connection string
        String uri = "mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.0.1";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("cbd");

            collection = database.getCollection("users_a");

            collection.drop();
            user_input = new Scanner(System.in);

            start();

            user_input.close();
        }
    }

    public static void start() throws InterruptedException {

        Document user_1 = new Document("username", "sara").append("products",
                Arrays.asList(new Document("especification", "").append("time", 0).append("quantity", 0)));
        collection.insertOne(user_1);

        Document user_2 = new Document("username", "aoki").append("products",
                Arrays.asList(new Document("especification", "").append("time", 0).append("quantity", 0)));
        collection.insertOne(user_2);

        long current_time = System.currentTimeMillis() / 1000;

        String[] products_1 = {"banana", "café", "pão", "leite", "arroz", "feijão"};
        String[] products_2 = {"camisola", "calças", "casaco", "ténis", "sapatilhas", "meias"};

        Integer[] quantities_1 = {3, 4, 2, 1, 7, 5};
        Integer[] quantities_2 = {5, 5, 3, 2, 5, 1};
        
        for (int i = 0; i < products_1.length/2; i++) {
            Products("sara", products_1[i], quantities_1[i]);
            Products("aoki", products_2[i], quantities_2[i]);
        }
        Thread.sleep(5000);

        for (int i = products_1.length/2; i < products_1.length; i++) {
            Products("sara", products_1[i], quantities_1[i]);
            Products("aoki", products_2[i], quantities_2[i]);
        }

        long now_time = System.currentTimeMillis() / 1000;
        long total_time = now_time - current_time;
        System.out.printf("Total time: %d seconds\n", total_time);
    }

    public static void Products(String username, String product, Integer quantity) throws InterruptedException{

        long current_time = System.currentTimeMillis() / 1000;

        Bson updateOperation = Updates.push("products",
                        new Document("especification", product).append("time", current_time).append("quantity", quantity));

        collection.updateOne(Filters.eq("username", username), updateOperation);

        current_time = System.currentTimeMillis() / 1000;

        Bson filter = Filters.and(
            Filters.eq("username", username),
            Filters.elemMatch("products", Filters.lt("time", current_time - TIMESLOT))
        );

        Bson update = Updates.pull("products", Filters.lt("time", current_time - TIMESLOT));
        collection.updateOne(filter, update);
        

        int number_products = 0;
        AggregateIterable<Document> num_products = collection.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("username", username)),
                Aggregates.unwind("$products"),
                Aggregates.group("$username", Accumulators.sum("numProducts", "$products.quantity"))));

        for (Document doc : num_products) {
            number_products = doc.getInteger("numProducts");
        }

        if (number_products > LIMIT) {
            System.out.printf("%s, you can't ordered more than 10 products! You have to wait a little longer.\n", username);
            Thread.sleep(5000);
        }
        
        AggregateIterable<Document> orderedProducts = collection.aggregate(Arrays.asList(
            Aggregates.unwind("$products"),
            Aggregates.group("$username", Accumulators.push("products", "$products.especification"), Accumulators.push("quantity", "$products.quantity")),
            Aggregates.project(fields(include("products", "quantity")))));

        System.out.println("Your current products: ");
        for (Document doc : orderedProducts) {
            System.out.println(doc);
        }

    }
}