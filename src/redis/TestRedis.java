package redis;

import java.security.Permission;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestRedis {
    private static String REDIS_HOST = "10.0.30.66";

    @Before
    public void setUp() throws Exception {}

    @Test
    public void test() {
        JedisPoolConfig jedisConf = new JedisPoolConfig();
        //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisConf.setBlockWhenExhausted(true);
        //当空闲时，是否检查连接有效性
        jedisConf.setTestWhileIdle(true);
        //最大连接空闲
        jedisConf.setMaxIdle(50);
        //最大连接数
        jedisConf.setMaxTotal(1000);
        //最小空闲数
        jedisConf.setMinIdle(10);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常
        jedisConf.setMaxWaitMillis(20* 1000);
        //回收连接的最小空闲时间--5分钟回收
        jedisConf.setMinEvictableIdleTimeMillis(10 * 1000);
        //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisConf.setNumTestsPerEvictionRun(5);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 
        jedisConf.setSoftMinEvictableIdleTimeMillis(6*1000);
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisConf.setTimeBetweenEvictionRunsMillis(30 * 1000);
        //在获取连接的时候检查有效性, 默认false
        jedisConf.setTestOnBorrow(false);
        jedisConf.setLifo(true);
        
        System.out.println(jedisConf.getTestOnBorrow());
        System.out.println(jedisConf.getTestOnCreate());
        System.out.println(jedisConf.getTestOnReturn());
        
        JedisPool pool = new JedisPool(jedisConf, REDIS_HOST, 6379, 20*1000);
        Jedis jedis = pool.getResource();
        String k1 = "key1";
        String r = jedis.set(k1, "kkkk1");
        System.out.println(jedis.get(k1));
        System.out.println(jedis.setnx(k1, "replace"));
        System.out.println(jedis.get(k1));
        int i = 10000000;
        String tbit = "tbit";
        
        String sortKey = "sortKey";
        
        jedis.zadd(sortKey, 10, "10a");
        jedis.zadd(sortKey, 9, "9a");
        jedis.zadd(sortKey, 3, "3a");
        jedis.zadd(sortKey, 6, "6a");
        jedis.zadd(sortKey, 2, "2a");
        jedis.zadd(sortKey, 5, "5a");
        jedis.zadd(sortKey, 1, "1a");
        
//        jedis.hset("rp/1", "count", "1");/
        jedis.hincrBy("rp/1", "count", 1);
        jedis.hincrBy("rp/1", "total", 100);
        
        
        
        System.out.println(jedis.zrange(sortKey, 0, 3));
        System.out.println(jedis.zrange(sortKey, 1, 2));
        System.out.println(jedis.zrevrange(sortKey, 1, 2));
        System.out.println(jedis.zrevrange(sortKey, 0, 2));
        System.out.println(jedis.zrangeByScore(sortKey, 7, 10));
        System.out.println(jedis.zrevrangeByScore(sortKey, 10, 7));
        System.out.println(jedis.zrange(sortKey, 0, -1));
        System.out.println(jedis.zrevrange(sortKey, 0, -1));
        
        SecurityManager security = System.getSecurityManager();
        
        
       /* Random random = new Random();
        while(i > 0){
            new TThread(pool,random.nextInt(8000)).start();
            try {
                Thread.sleep(random.nextInt(10));
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            i--;
        }
        try {
            while(true){
                System.out.println("-------------------------->" + pool.getNumActive());
                System.out.println("-------------------------->" + pool.getNumIdle());
                System.out.println("-------------------------->" + pool.getNumWaiters());
                Thread.sleep(5000);
            }
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
    
    static class TThread extends Thread{
        String tbit = "tbit";
        JedisPool pool;
        int evictTime;
        public TThread(JedisPool pool, int evictTime){
            this.pool = pool;
            this.evictTime = evictTime;
        }
        @Override
        public void run() {
            Jedis jedis = pool.getResource();
            try {
                jedis.setbit(tbit, 1, true);
//                System.out.println(pool.getNumActive());
//                System.out.println(pool.getNumIdle());
//                System.out.println(pool.getNumWaiters());
                
                System.out.println("-------"+Thread.currentThread().getId()+"--------" + evictTime);
                Thread.sleep(evictTime);
                pool.returnResourceObject(jedis);
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{
//                pool.returnResourceObject(jedis);
            }
            
        }
        
    }

}
