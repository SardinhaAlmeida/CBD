package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class SimplePost {
    private Jedis jedis;
    public static String USERS = "users"; // Key set for users' name
    public static String USERS_KEY_LIST = "list"; // Key set for users' name
    public static String USERS_KEY_HASH = "hash"; // Key set for users' name
	
	public SimplePost() {
		this.jedis = new Jedis();
	}
 
	public void saveUser(String username) {
		jedis.sadd(USERS, username);
	}

	public Set<String> getUser() {
		return jedis.smembers(USERS);
	}
	
	public Set<String> getAllKeys() {
		return jedis.keys("*");
	}

    public void saveinList(String value){
        jedis.rpush(USERS_KEY_LIST, value);
    }

    public List<String> getFromList(){
        return jedis.lrange(USERS_KEY_LIST, 0, jedis.llen(USERS_KEY_LIST));
    }

    public void saveinHash(String field, HashMap<String, String> users_hashmap){
        jedis.hset(USERS_KEY_HASH, field, users_hashmap.get(field).toString());
    }

    public List<String> getFromHash(String field){
        return jedis.hmget(USERS_KEY_HASH,field);
    }

    public void delete(String key){
        jedis.del(key);
    }
 
	public static void main(String[] args) {
		SimplePost board = new SimplePost();
		// set some users
		String[] users = { "Ana", "Pedro", "Maria", "Luis" };
		for (String user: users) 
			board.saveUser(user);
		board.getAllKeys().stream().forEach(System.out::println);
        System.out.println("------------Set---------------");
		board.getUser().stream().forEach(System.out::println);
        board.delete(USERS);

        //list  
        String[] users_list = { "Carolina", "Aoki", "Sara", "Jacke" };
        for (String user: users_list)
            board.saveinList(user);
        System.out.println("------------List--------------");
        board.getFromList().stream().forEach(System.out::println);
        board.delete(USERS_KEY_LIST); 

        //hashmap
        HashMap <String, String > users_hashmap = new HashMap<String, String>();
        String[] users_hash = { "Tiago", "Berto", "Rafinha", "Marta"};
        String[] ages = { "20", "21", "22", "23" };
        int i = 0;
        for (String user: users_hash){
            users_hashmap.put(user, ages[i++]);
            board.saveinHash(user, users_hashmap);
        }
        System.out.println("------------HashMap-----------");
        for (String user: users_hash){
            System.out.println(user + " - "+ board.getFromHash(user));
        }
        board.delete(USERS_KEY_HASH); 
    }
}