package mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.google.common.collect.Maps;

public class MySqlMemoryLeak {

    public static void main(String[] args) throws Exception{
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://10.0.30.59:3306/pldb?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true");
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername("platform_app");
        p.setPassword("yV2x60A3");
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        final DataSource pool = new DataSource();
        pool.setPoolProperties(p);
        
        /*pool.setUsername("platform_app");
        pool.setPassword("yV2x60A3");
        pool.setUrl("jdbc:mysql://10.0.30.59:3306/pldb?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true");
        pool.setDriverClassName("com.mysql.jdbc.Driver");
        pool.setMaxActive(10);
        pool.setMaxIdle(10);
        pool.setMinIdle(2);
        pool.setInitialSize(2);
        pool.setLogAbandoned(true);*/
        
        CountDownLatch latch = new CountDownLatch(2);
        int count = 10;
        while(count > 0){
            new Child(pool).start();
            count--;
        }
        latch.await();
    }
    
    static class Child extends Thread{
        DataSource ds;
        Child(DataSource ds){
            this.ds = ds;
        }
        @Override
        public void run() {
            try{
                while(true){
                Connection conn = ds.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("select * from mgtf_logs");
                ResultSet rs = pstmt.executeQuery();
                Map<String,Object> map = Maps.newHashMap();
                while(rs.next()){
                    map.put("logId", rs.getObject("log_id"));
                    map.put("log_name", rs.getObject("log_name"));
                    map.put("log_user", rs.getObject("log_user"));
                    map.put("log_user_role", rs.getObject("log_user_role"));
                }
                System.out.println(map);
//                rs.close();
                pstmt.close();
                conn.close();
//                Thread.sleep(10);
            }
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
    }

}
