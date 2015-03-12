package zookeeper;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.shared.SharedCountListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.hbase.async.Bytes;
import org.joda.time.LocalDateTime;
import org.junit.Test;

public class ZKTest {

    @Test
    public void test()throws Exception{
        String quorum = "echqlm2:2181,echqlm0:2181,echqlm1:2181";
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 10);
        CuratorFramework zkClient = null;//CuratorFrameworkFactory.newClient(quorum, 60*1000, 60*1000, policy);
        
        Builder builder = CuratorFrameworkFactory.builder().connectString(quorum).connectionTimeoutMs(60*1000).sessionTimeoutMs(60*1000);
        builder.namespace("zktest100").retryPolicy(policy);
        zkClient = builder.build();
        zkClient.start();
        
        List<String> dirs = zkClient.getChildren().forPath("/");
        for (String string : dirs) {
            System.out.println(string);
        }
        Stat state = zkClient.checkExists().forPath("/zkTest");
        if(state != null){
            System.out.println(new LocalDateTime(state.getCtime()).toString("yyyy-MM-dd HH:mm:ss:SSS"));
        }else{
            zkClient.create().forPath("/zkTest");
        }
//        zkClient.checkExists().usingWatcher(new CuratorWatcher() {
//            
//            @Override
//            public void process(WatchedEvent evt) throws Exception {
//                System.out.println(evt.toString());
//                
//            }
//        }).forPath("/zkTest");
        zkClient.getChildren().usingWatcher(new CuratorWatcher() {
            
            @Override
            public void process(WatchedEvent evt) throws Exception {
                System.out.println(evt.toString());
                
            }
        }).forPath("/zkTest");
        SharedCountListener sl = null;
        
        zkClient.create().withMode(CreateMode.EPHEMERAL).forPath("/zkTest/tmp1",Bytes.fromLong(13456));
        System.out.println(Bytes.getLong(zkClient.getData().forPath("/zkTest/tmp1")));
        
        zkClient.delete().forPath("/zkTest/tmp1");
        
        zkClient.close();
        
    }
}
