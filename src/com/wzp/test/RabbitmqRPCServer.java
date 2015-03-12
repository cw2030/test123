package com.wzp.test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitmqRPCServer {

    public static void main(String[] args) {
        final String exchange = "rpcExchange";
        final String routingKey = "rpcRoutingKey";
        final String queue = "rpcQueue";
        final ExecutorService es = Executors.newFixedThreadPool(50);

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            // 设置由于网络原因，断开自动恢复
            factory.setAutomaticRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(10000);
            /*
             * 对于网络故障，可以设置rabbitmq集群，其中一个连接断开，可以继续连接另外一个rabbitmq Address[]
             * addresses = {new Address("192.168.1.4"), new
             * Address("192.168.1.5")}; factory.newConnection(addresses);
             */

            Connection connection = factory.newConnection(Executors.newFixedThreadPool(5));
            final Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, "direct", false);
            channel.queueDeclare(queue, true, false, false, null);
            channel.queueBind(queue, exchange, routingKey);

            channel.basicConsume(queue, true, new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           BasicProperties properties,
                                           byte[] body) throws IOException {
                    /*String msg = new String(body);
                    System.out.println(msg);
                    msg = "reply ::　" + msg;

                    String correlationId = properties.getCorrelationId();
                    String replyTo = properties.getReplyTo();
                    AMQP.BasicProperties replyProperties = new AMQP.BasicProperties.Builder().correlationId(correlationId).build();
                    byte[] replyBody = msg.getBytes();
                    channel.basicPublish(exchange, routingKey, replyProperties, replyBody);*/
                    // BasicProperties replyProp = new
                    // BasicProperties().builder().correlationId(properties.getCorrelationId()).build();
                    // channel.basicPublish(exchange, routingKey, replyProp,
                    // msg.getBytes());

                    es.submit(new ProcessRequest(getChannel(), body, properties.getReplyTo()));
                }

            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class ProcessRequest extends Thread {

        Channel channel;
        byte[] body;
        String replyQueue;

        public ProcessRequest(Channel ch, byte[] body, String replyQueue) {
            this.channel = ch;
            this.body = body;
            this.replyQueue = replyQueue;
        }

        @Override
        public void run() {
            Random random = new Random();
            try {
                System.out.println("收到请求：" + new String(body));
                Thread.sleep(random.nextInt(50));
                channel.addShutdownListener(new ShutdownListener() {
                    @Override
                    public void shutdownCompleted(ShutdownSignalException cause) {
                        cause.printStackTrace();
                        
                    }
                });
                channel.basicPublish("", replyQueue, null, body);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
