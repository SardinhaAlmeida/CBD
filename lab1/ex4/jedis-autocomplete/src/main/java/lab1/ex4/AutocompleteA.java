package lab1.ex4;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;

import redis.clients.jedis.Jedis;

public class AutocompleteA {
    public static String KEY_NAMES = "names";

    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        Scanner user_scanner = new Scanner(System.in);
        Scanner file_scanner = null;
        File names_file = new File("/home/saraalmeida/UA/ano3/CBD/lab1/ex4/jedis-autocomplete/src/main/java/lab1/ex4/names.txt");

        try {

            file_scanner = new Scanner(names_file);

            while(file_scanner.hasNextLine()){
                String name = file_scanner.nextLine();
                jedis.sadd(KEY_NAMES,name);
            }

            file_scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }

        while(true){
            System.out.print("Search for ('Enter' to quit): ");
            
            String init = user_scanner.nextLine();
            if (init.equals("")){
                break;
            } 
            
            Set<String> name_list = jedis.smembers(KEY_NAMES);
            List <String> completes = new ArrayList<String>();
            for (String name : name_list) {
                if (name.startsWith(init)) {
                    completes.add(name);
                }
            }
            Collections.sort(completes);
            for (String name : completes) {
                System.out.println(name);
            }
        }
        user_scanner.close();
        jedis.close();
    }
}