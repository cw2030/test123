package rbm342;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.primitives.Longs;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ReturnListener;

public class Server {

    public static String queueName = "testRPC";
    public static String exchangeName = "testExchange";
    private static LinkedBlockingQueue<Channel> chs = new LinkedBlockingQueue<Channel>(200);
    private static final ExecutorService ess = Executors.newFixedThreadPool(200);
    private static Connection connection = null;
    public static void main(String[] args){
        String rabbitmqHost = "10.0.30.60";
        if (args.length > 0)
            rabbitmqHost = args[0];

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqHost);
        factory.setUsername("admin");
        factory.setPassword("admin123");
        try{
            factory.setAutomaticRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(10);
            factory.setRequestedHeartbeat(5);
            factory.setConnectionTimeout(10*1000);
            connection = factory.newConnection(Executors.newFixedThreadPool(10));
            for(int i = 0; i < 200; i++){
//                chs.add(connection.createChannel());
            }
            try{
//                for(int i = 0; i < 100; i++){
                    Channel channel = connection.createChannel();
                    AutoReplyConsumer consumer = new AutoReplyConsumer(channel);
                    channel.queueDeclare(queueName, true, false, false, null);
                    channel.exchangeDeclare(exchangeName, "direct");
                    channel.queueBind("testRPC", "testExchange", "testRPC");
                    channel.basicQos(10);
                    channel.basicConsume(queueName, false, consumer);
//                }
            }catch(Exception e){
                
            }
            
            System.out.println("server is starting...");
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    static class AutoReplyConsumer extends DefaultConsumer{

        public AutoReplyConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag,
                                   Envelope envelope,
                                   BasicProperties properties,
                                   byte[] body) throws IOException {
//            System.out.println(Longs.fromByteArray(body));
//            getChannel().basicPublish("", properties.getReplyTo(), null, body);
            getChannel().basicAck(envelope.getDeliveryTag(), true);
            ess.submit(new Task(body,properties.getReplyTo()));
        }
    }
    
    static class Task extends Thread{
        byte[] body = null;
        String reply;
        public Task(byte[] body,String reply){
            this.body = body;
            this.reply = reply;
        }

        @Override
        public void run() {
            try{
                /*Channel ch = chs.poll(5, TimeUnit.SECONDS);
                boolean isNeedClose = false;
                if(ch == null){
                    System.out.println("获取channel超时");
                    isNeedClose = true;*/
                   Channel ch = connection.createChannel();
                /*}*/
//                System.out.println(reply);
                ch.basicPublish("", reply, null, body);
//                if(isNeedClose){
                    ch.close();
                /*}else{
                    chs.add(ch);
                }*/
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
    }
    
    static class CustomConfirmListener implements ConfirmListener{

        @Override
        public void handleAck(long deliveryTag, boolean multiple)
                throws IOException {
            System.out.println(deliveryTag + "::" + multiple);
            
        }

        @Override
        public void handleNack(long deliveryTag, boolean multiple)
                throws IOException {
            System.out.println(deliveryTag + "::" + multiple);
            
        }
        
    }
    
    static class CustomReturnListener implements ReturnListener{

        @Override
        public void handleReturn(int arg0,
                                 String arg1,
                                 String arg2,
                                 String arg3,
                                 BasicProperties arg4,
                                 byte[] arg5) throws IOException {
            System.out.println(arg0 + "::" + arg1 + "::" + arg2 + "::" + arg3);
            
        }
        
    }
}
