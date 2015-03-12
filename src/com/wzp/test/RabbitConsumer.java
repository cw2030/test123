package com.wzp.test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitConsumer {
    
    private static AtomicLong count = new AtomicLong();

	public static void main(String[] args) throws Exception{
		ExecutorService eService = Executors.newFixedThreadPool(20);
		ConnectionFactory factory = new ConnectionFactory();
		ExecutorService es = Executors.newFixedThreadPool(10);
		Connection connection = factory.newConnection(es,new Address[]{new Address("localhost")});

//		for(int i = 0; i < 1; i++){
			Channel channel = connection.createChannel();
			
			String exchange = "wzpExchange";
//			String routingKey = "wzpRoutingkey";
			
			
			String myQueue = "wzpQueue1";
//			String myQueue2 = "wzpQueue2";
			channel.exchangeDeclare(exchange, "direct",false);
			channel.queueDeclare(myQueue, true, false, false, null);
//			channel.queueDeclare(myQueue2, true, false, false, null);
			channel.queueBind(myQueue, exchange, myQueue);
			channel.basicQos(1000);
			
			String consumerTag = channel.basicConsume(myQueue, false, new Consumer(channel));
			
			
//			String consumerTag2 = channel.basicConsume(myQueue2, false, new Consumer(channel));
			//如果消费者要是退出的话，除了需要关闭连接、channel还要执行下面这步channel.basicCancel(consumerTag)
//			channel.basicCancel(consumerTag);
//		}
		
		
		
		
		Thread.sleep(10000000);
		eService.shutdown();
		channel.close();
		connection.close();
	}
	
	static class Consumer extends DefaultConsumer{
		Channel channel;
		public Consumer(Channel channel) {
			super(channel);
			this.channel = channel;
		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope,
				BasicProperties properties, byte[] body) throws IOException {
//			System.out.println(consumerTag);
//			System.out.println(new String(body,"utf-8"));
			
			System.out.println(Thread.currentThread().getId() + "::count -->" + count.incrementAndGet());
			channel.basicAck(envelope.getDeliveryTag(), false);
			/*try {
				Thread.sleep(500);
				channel.basicAck(envelope.getDeliveryTag(), false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		
		
	}

}
