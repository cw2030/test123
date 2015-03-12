package com.wzp.test;

import java.io.IOException;
import java.util.UUID;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitmqRPCClient {
    public static String exchange = "rpcExchange";
    public static String routingKey = "rpcRoutingKey";
    public static String queue = "rpcQueue";
    public static String replyQueue = "replyQueue";

    public static void main(String[] args) {

        int timeout = -1;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            for(int i = 0; i < 10000; i++){
                Channel channel = connection.createChannel();
                new Request(channel,i).start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class Request extends Thread {
        Channel channel;
        int i = 0;

        public Request(Channel channel, int i) {
            this.channel = channel;
            this.i = i;
        }

        @Override
        public void run() {
            try {
                channel.addConfirmListener(new custConfirmListener());
                channel.addShutdownListener(new CustShutdownListener());
                channel.confirmSelect();
                // 声明一个临时队列，用于接收服务提供者返回的结果
                String replyQueue = channel.queueDeclare().getQueue();
                QueueingConsumer replyConsumer = new QueueingConsumer(channel);
                channel.basicConsume(replyQueue, replyConsumer);
                BasicProperties bp = new BasicProperties().builder().replyTo(replyQueue).build();
                channel.queueDeclare(queue, true, false, false, null);
                channel.exchangeDeclare(exchange, "direct");
                channel.queueBind(queue, exchange, routingKey);
                channel.basicPublish(exchange, routingKey, bp, ("Request:" + i).getBytes());

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    static class custConfirmListener implements ConfirmListener{

        @Override
        public void handleAck(long deliveryTag, boolean multiple)
                throws IOException {
            System.out.println("handleAck1===" + deliveryTag);
            
        }

        @Override
        public void handleNack(long deliveryTag, boolean multiple)
                throws IOException {
            System.out.println("handleNack2===" + deliveryTag);
            
        }
        
    }

    static class CustShutdownListener implements ShutdownListener {

        @Override
        public void shutdownCompleted(ShutdownSignalException cause) {

            System.out.println("shutdownCompleted===" + cause.getMessage());

        }

    }

}
