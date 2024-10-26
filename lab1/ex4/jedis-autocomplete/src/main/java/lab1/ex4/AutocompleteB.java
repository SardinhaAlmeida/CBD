package lab1.ex4;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

import redis.clients.jedis.Jedis;

public class AutocompleteB {
    public static String KEY_HASH = "hash";

    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        Scanner user_scanner = new Scanner(System.in);
        Scanner file_scanner = null;
        File names_file = new File("/home/saraalmeida/UA/ano3/CBD/lab1/ex4/jedis-autocomplete/src/main/java/lab1/ex4/nomes-pt-2021.csv");

        HashMap <String, String > names_pops = new HashMap<String, String>();

        try {

            file_scanner = new Scanner(names_file);

            while(file_scanner.hasNextLine()){
                String name = file_scanner.nextLine();
                String[] name_pop = name.split(";");
                names_pops.put(name_pop[0], name_pop[1]);
                jedis.hset(KEY_HASH, name_pop[0], name_pop[1]);
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
            
            List<String> name_list =  new ArrayList<String>();
            for (String name : names_pops.keySet()) {
                name_list.add(name);
            }
            HashMap <Integer, String > completes = new HashMap<Integer, String>();
            for (String name : name_list) {
                if (name.toLowerCase().startsWith(init)) {
                    completes.put(Integer.valueOf(names_pops.get(name)), name);
                }
            }
            List<Integer> sortedCompletes = new ArrayList<>(completes.keySet());
            Collections.sort(sortedCompletes);
            Collections.reverse(sortedCompletes);
            for (Integer sorted : sortedCompletes) {
                //System.out.printf("%-12s (%1d)\n",completes.get(sorted), sorted);
                System.out.println(completes.get(sorted));
            }
        }
        user_scanner.close();
        jedis.close();
    }
}