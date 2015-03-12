package zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

public class DistributedAtomicLongTest {
    private CuratorFramework zkClient = null;//CuratorFrameworkFactory.newClient(quorum, 60*1000, 60*1000, policy);
    public void init(){
        String quorum = "echqlm2:2181,echqlm0:2181,echqlm1:2181";
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 10);
        Builder builder = CuratorFrameworkFactory.builder().connectString(quorum).connectionTimeoutMs(60*1000).sessionTimeoutMs(60*1000);
        builder.namespace("zktest100").retryPolicy(policy);
        zkClient = builder.build();
        zkClient.start();
    }

    @Test
    public void test()throws Exception{
        init();
        DistributedAtomicLong  count = new DistributedAtomicLong(zkClient, "/ccLong2", new ExponentialBackoffRetry(1000, 10));
        System.out.println("val:" + count.get().postValue());
        count.increment();
        
        count.increment();
        count.increment();
        count.increment();
        
        System.out.println(count.get().postValue());
    }
}
