package rbm342;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.primitives.Longs;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Confirm.SelectOk;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class ClientRpcManualReply {

    public static String queueName = "testRPC";
    public static String exchangeName = "testExchange";
    
    private static LinkedBlockingQueue<String> chs = new LinkedBlockingQueue<String>(200);
    private static final AtomicLong count = new AtomicLong();
    final static AtomicInteger lock = new AtomicInteger(0);

    public static void main(String[] args) {
        String rabbitmqHost = "10.0.30.60";
        if (args.length > 0)
            rabbitmqHost = args[0];

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin123");
        factory.setHost(rabbitmqHost);
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(10);
        factory.setRequestedHeartbeat(5);
        factory.setConnectionTimeout(10*1000);
       
        try {
            Connection connection = factory.newConnection();
            for(int i = 0; i < 200; i++){
                chs.add("1");
            }
            int requests = 1;
            ExecutorService es = Executors.newFixedThreadPool(200);
            while(requests > 0){
                while(lock.get() > 200){
                    Thread.sleep(10);
                }
                es.submit(new Task(connection));
                /*if(requests%1000 == 0)
                    Thread.sleep(100);*/
                requests++;
                lock.incrementAndGet();
            }
            System.out.println("任务提交完毕");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    static class Task extends Thread{
        Connection connection;
        public Task(Connection connection){
            this.connection = connection;
        }
        @Override
        public void run() {
            try{
                final String flag = chs.poll(5, TimeUnit.SECONDS);
                if(flag == null){
                    System.out.println("获取channel超时");
                    return;
                }
                final CountDownLatch cdl = new CountDownLatch(1);
                if(!connection.isOpen()){
                    System.out.println("++++++连接已经关闭+++++++");
                    return;
                }
                Channel channel = connection.createChannel();
                String replyQueue = channel.queueDeclare().getQueue();
                channel.basicConsume(replyQueue, true, new DefaultConsumer(channel) {

                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               BasicProperties properties,
                                               byte[] body) throws IOException {
                        
                        System.out.println(count.incrementAndGet() + "-++-" + (System.currentTimeMillis() - Longs.fromByteArray(body)));
                        getChannel().close();
                        
                        chs.add(flag);
                        cdl.countDown();
                    }

                });
                // channel.exchangeDeclare(exchangeName, "direct");
                DeclareOk declareOk = channel.queueDeclare(queueName, true, false, false, null);
                if(declareOk.getConsumerCount() == 0){
                    System.out.println("没有消费者");
                    channel.close();
                    return;
                }
                
                BasicProperties props = new BasicProperties.Builder().replyTo(replyQueue).expiration("30000").build();
                channel.basicPublish(exchangeName, queueName, props, Longs.toByteArray(System.currentTimeMillis()));
                cdl.await(5, TimeUnit.SECONDS);
                lock.decrementAndGet();
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
    }
    
}
