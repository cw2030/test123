package rbm342;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.primitives.Longs;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 不需要创建临时队列接收rpc-server的reply
 * 
 * @author user
 * 
 */
public class ClientRpcAutoReply {

    public static String queueName = "testRPC";
    public static String exchangeName = "testExchange";
    private static LinkedBlockingQueue<String> chs = new LinkedBlockingQueue<String>(200);
    final static AtomicInteger lock = new AtomicInteger(0);
    final static AtomicInteger count = new AtomicInteger(0);
    
    public static void main(String[] args) {
        String rabbitmqHost = "10.0.30.60";
        if (args.length > 0)
            rabbitmqHost = args[0];

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin123");
        factory.setHost(rabbitmqHost);
        Connection connection = null;
        ExecutorService es = Executors.newFixedThreadPool(200);
        try {
            for(int i = 0; i < 200; i++){
                chs.add("1");
            }
            connection = factory.newConnection();
            
            while (true) {
                while(lock.get() > 200){
                    Thread.sleep(10);
                }
                es.submit(new Sender(connection, lock.incrementAndGet()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class Sender extends Thread {
        Connection connSub;
        int flag;

        public Sender(Connection conn, int flag) {
            this.connSub = conn;
            this.flag = flag;
        }

        @Override
        public void run() {
            try {
                Channel channel = connSub.createChannel();
                final CountDownLatch down = new CountDownLatch(1);
                // 必须要是：amq.rabbitmq.reply-to
                channel.basicConsume("amq.rabbitmq.reply-to", true, new DefaultConsumer(channel) {

                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               BasicProperties properties,
                                               byte[] body) throws IOException {
                        String msg = new String(body, "utf-8");
                        System.out.println(count.incrementAndGet() + "-++-" + (System.currentTimeMillis() - Longs.fromByteArray(body)));
//                        if(!msg.contains(flag+"")){
//                            System.out.println(flag + "-->" + msg);
//                            System.out.println(envelope);
//                        }
                        down.countDown();
                    }

                });

                // replyTo必须是：amq.rabbitmq.reply-to
                BasicProperties props = new BasicProperties.Builder().replyTo("amq.rabbitmq.reply-to").expiration("30000").build();
                channel.basicPublish(exchangeName, queueName, props, Longs.toByteArray(System.currentTimeMillis()));
                //("测试RPC通信ch" + flag).getBytes("utf-8")
                down.await();
                lock.decrementAndGet();
                channel.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
