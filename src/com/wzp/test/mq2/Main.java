package com.wzp.test.mq2;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

public class Main {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //设置由于网络原因，断开自动恢复
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(10000);
//        ExecutorService threadExecutor = Executors.newFixedThreadPool(5);
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(200000));
        Connection con = new ConnectionFactory().newConnection(tpe);

        Channel throwAwayChannel = con.createChannel();
        String queue = "queueABC";
        
        throwAwayChannel.queueDeclare(queue, false, false, false, null);

        

        int prefetchCount = 10;

        Worker fast = new Worker(prefetchCount, tpe, 1,
                                 con.createChannel(), queue);
        Worker fast2 = new Worker(prefetchCount, tpe, 1,
                                 con.createChannel(), queue);
        Worker fast3 = new Worker(prefetchCount, tpe, 1,
                                 con.createChannel(), queue);
        Worker fast4 = new Worker(prefetchCount, tpe, 1,
                                 con.createChannel(), queue);
        Worker fast5 = new Worker(prefetchCount, tpe, 1,
                                 con.createChannel(), queue);
        /*Worker slow = new Worker(prefetchCount, threadExecutor, 100,
                                 con.createChannel(), queue);*/

        Producer producer = new Producer(con.createChannel(), queue);
        tpe.execute(producer);

        Thread.sleep(300000);
        
        tpe.shutdownNow();
        System.out.println("end.........");
        con.close();

        System.err.println("Fast worker processed : " + fast.processed);
        /*System.err.println("Slow worker processed : " + slow.processed);*/
    }
    
    static class Producer implements Runnable {

        Channel channel;
        String routingKey;

        Producer(Channel c, String r) {
            channel = c;
            routingKey = r;
        }

        public void run() {
            int count = 0;
            while (true) {
                try {
                    channel.basicPublish("", routingKey,
                                         MessageProperties.BASIC, ("" + count).getBytes());
                    count++;
                    if(count > 500000){
                        break;
                    }
//                    Thread.sleep(10);
                } catch (Exception e) {
                    break;
                }
            }
        }
    }
    
    static class Worker extends DefaultConsumer {

        String name;
        long sleep;
        Channel channel;
        String queue;
        int processed;
        ExecutorService executorService;

        public Worker(int prefetch, ExecutorService threadExecutor,
                      long s, Channel c, String q) throws Exception {
            super(c);
            sleep = s;
            channel = c;
            queue = q;
            channel.basicQos(prefetch);
            channel.basicConsume(queue, true, this);
            executorService = threadExecutor;
        }

        @Override
        public void handleDelivery(String consumerTag,
                                   Envelope envelope,
                                   AMQP.BasicProperties properties,
                                   byte[] body) throws IOException {
            for(int i = 0; i < 100000; i++){
                String c = new String("abc") + "123";
            }
            System.out.println(Thread.currentThread().getId() + ":" + new String(body));
           /* Runnable task = new VariableLengthTask(this,
                                                   envelope.getDeliveryTag(),
                                                   channel, sleep,new String(body));
            executorService.submit(task);*/
        }
    }
    
    static class VariableLengthTask implements Runnable {

        long tag;
        long sleep;
        Channel chan;
        Worker worker;
        String msg;

        VariableLengthTask(Worker w, long t, Channel c, long s,String msg) {
            worker = w;
            tag = t;
            chan = c;
            sleep = s;
            this.msg = msg;
        }

        public void run() {
           /* try {
                Thread.sleep(sleep * 10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            for(int i = 0; i < 100000; i++){
                String c = new String("abc") + "123";
            }
            System.out.println(Thread.currentThread().getId() + ":" + msg);
            /*if (chan.isOpen()) {
                try {
                    chan.basicAck(tag, false);
                    worker.processed++;
                } catch (IOException e) {}
            }*/
        }
    }

}
