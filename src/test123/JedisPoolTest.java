package test123;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolTest {
	private static JedisPool pool = null;

	public static void main(String[] args) {
		if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
//            System.out.println(config.testOnBorrow);
            
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
//            config.setMaxActive(10);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(5);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
//            config.setMaxWait(1000 * 10);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, "10.0.30.40", 6379);
        }
		
        Jedis jedis = null;
        try {
        	int count = 100;
        	long ts1 = 0;
        	long ts2 = 0;
        	while(count > 0){
        		try{
        			jedis = pool.getResource();
                    String value = jedis.get("pool.test");
                    System.out.println(count + "-->" + value);
                    count --;
                    System.out.println(jedis);
                    Thread.sleep(2000);
        		}catch(Exception e){
        			System.out.println("eex::" + (System.nanoTime() - ts1));
        			System.out.println(ts2 + "eex::" + System.currentTimeMillis());
        			e.printStackTrace();
        			System.out.println("ERROR-->" + count);
        			count --;
        			Thread.sleep(2000);
        		}finally {
                    //返还到连接池
                    pool.returnResource(jedis);
                }
        	}
            
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } 

	}

}
