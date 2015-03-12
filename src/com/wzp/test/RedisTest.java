package com.wzp.test;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisTest {

	public static void main(String[] args) {
	    JedisPool pool = new JedisPool("10.0.30.66");
	    Jedis jedis = pool.getResource();
	    
	    jedis.set("mk1", "12345abcdefwwfw");
	    System.out.println(jedis.substr("mk1", 0, 5));
	    
	    jedis.hset("t/map", "k1", "value1");
	    jedis.hset("t/map", "k2", "value2");
	    jedis.hset("t/map", "k3", "value3");
	    jedis.hset("t/map", "k4", "value4");
	    jedis.hsetnx("t/map", "k4", "v4");
	    
	    jedis.mset("mk1","mv1","mk2","mv2","mk3","mv3");
	    List<String> valList = jedis.mget("mk1","mk2","mk3");
	    for(String v : valList){
	        System.out.println(v);
	    }
	    System.out.println(jedis.randomKey());
	    
	    Set<String> keys = jedis.hkeys("t/map");
	    for(String k : keys){
	        System.out.println(k);
	    }
	    
	    List<String> vList = jedis.hvals("t/map");
        for(String k : vList){
            System.out.println(k);
        }
        
	    pool.returnResource(jedis);
	    pool.destroy();
	}

}
