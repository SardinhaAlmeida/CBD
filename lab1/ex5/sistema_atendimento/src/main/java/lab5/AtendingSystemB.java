package lab5;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import redis.clients.jedis.Jedis;

public class AtendingSystemB {

    public static String KEY_USERS = "users";
    public static String KEY_USER_PRODUCTS = "products";
    private static final int LIMIT = 10;
    private static final int TIMESLOT = 5;

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

    public static void Products(String username, String product, Integer quantity) throws InterruptedException {

        long current_time = System.currentTimeMillis() / 1000;

        int quantity_total = 0;

        jedis.zremrangeByScore(username, 0, current_time - TIMESLOT);

        long number_products = jedis.zcard(username);

        Map<String, Integer> product_quant = new HashMap<>();
        for (String product_a : jedis.zrange(username, 0, number_products)) {
            int quantity_a = Integer.parseInt(jedis.hget("product_quant", product_a));
            product_quant.put(product_a, quantity_a);
            quantity_total += quantity_a;
        }

        System.out.println("Your current products and quantities: ");
        for (String produto : product_quant.keySet()) {
            System.out.println(produto + " - " + product_quant.get(produto));
        }

        if (quantity_total + quantity >= LIMIT) {
            System.out.println("You already ordered 10 products! You can't order more for a little longer.");
            Thread.sleep(5000); 
        } else {
            jedis.zadd(username, current_time, product);
            jedis.hset("product_quant", product, String.valueOf(quantity));
        }
    }

}