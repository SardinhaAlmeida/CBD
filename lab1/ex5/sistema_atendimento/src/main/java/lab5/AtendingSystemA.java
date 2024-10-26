package lab5;

// import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import redis.clients.jedis.Jedis;

public class AtendingSystemA {

    public static String KEY_USERS = "users";
    public static String KEY_USER_PRODUCTS = "products";
    private static final int LIMIT = 10;
    private static final Double TIMESLOT = 5.0;

    public static Jedis jedis;

    public static Scanner user_input;

    public static void main(String[] args) throws InterruptedException{

        jedis = new Jedis();
        user_input = new Scanner(System.in);

        jedis.flushAll();

        start();
            
        user_input.close();
    }

    public static void start() throws InterruptedException {

        long current_time = System.currentTimeMillis() / 1000;

        String[] products_1 = {"banana", "café", "pão", "leite", "arroz", "feijão", "carne", "peixe", "batata", "tomate", "alface", "cenoura", "maçã", "laranja", "uva", "morango", "pêssego", "pêra", "abacate", "abacaxi"};
        String[] products_2 = {"camisola", "calças", "casaco", "ténis", "sapatilhas", "meias", "boné", "chapéu", "camisa", "gravata", "cinto", "calções", "fato", "vestido", "saia", "botas", "sandálias", "pulseira", "colar", "brincos"};
        
        System.out.println(products_1.length/2);
        System.out.println(products_2.length);
        for (int i = 0; i < products_1.length/2; i++) {
            Products("sara", products_1[i]);
            Products("aoki", products_2[i]);
        }
        Thread.sleep(5000);

        for (int i = products_1.length/2; i < products_1.length; i++) {
            Products("sara", products_1[i]);
            Products("aoki", products_2[i]);
        }

        long now_time = System.currentTimeMillis() / 1000;
        long total_time = now_time - current_time;
        System.out.printf("Total time: %d seconds\n", total_time);

        
    } 

    public static void Products(String username, String product) throws InterruptedException{
        List<String> orderedProducts;

        long current_time = System.currentTimeMillis() / 1000;

        jedis.zremrangeByScore(username, 0, current_time - TIMESLOT);

        long number_products = jedis.zcard(username);
        if (number_products >= LIMIT) {
            System.out.printf("%s, you can't ordered more than 10 products! You have to wait a little longer.\n", username);
            Thread.sleep(5000);
        }
        orderedProducts = jedis.zrange(username, 0, number_products);

        System.out.printf("%s current products: \n", username);
        System.out.println(orderedProducts);

        // System.out.println("Choose your product: ");
        // String product = user_input.nextLine();
        jedis.zadd(username, current_time, product);
    }
}