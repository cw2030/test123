package com.wzp.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.tools.json.JSONWriter;

public class RabbitProducer {

	public static void main(String[] args) throws Exception{
	    
	    System.out.println(String.format("%s,%s", "11","22"));
		
		for(int i = 0; i < 1; i++){
			new Send().start();
		}
	}
	
	static class Send extends Thread{
		public Send(){
			System.out.println(this.toString());
			
		}

		@Override
		public void run(){
			try{
				ExecutorService eService = Executors.newFixedThreadPool(10);
				ConnectionFactory factory = new ConnectionFactory();
				Connection connection = null;
				//连接创建三种方式：
				/**
				 * 
				 */
//				String username,password;
//				username=password="guest";
//				factory.setUsername(username);
//				factory.setPassword(password);
				//Address可以是一组地址
				connection = factory.newConnection(null,new Address[]{new Address("localhost")});
				//默认情况下newConnection()
//				factory.newConnection(eService, new Address[]{new Address("10.0.30.61",5672)});
//				String uri = "amqp://guest:guest@10.0.30.61:5672";
//				factory.setUri(uri);
//				factory.setHost("10.0.30.61");
				//thread execution service for consumers on the connection
//				connection = factory.newConnection(eService);

				
				Channel channel = connection.createChannel();
				
				String exchange = "";
				String routingKey = "wzpQueue2";
				
				
				String myQueue = "wzpQueue1";
//				channel.exchangeDeclare(exchange, "direct",false);
//				channel.queueDeclare(myQueue, true, false, false, null);
//				channel.queueBind(myQueue, exchange, routingKey);
				
				String message = "My message to myFirstQueue";
				int i = 500000;
				//basicPublish(java.lang.String exchange, java.lang.String routingKey, AMQP.BasicProperties props, byte[] body)
//				channel.basicPublish(exchange, routingKey, mandatory, immediate, props, body);
//				channel.basicPublish(exchange, routingKey, props, body);
//				MessageProperties.PERSISTENT_TEXT_PLAIN
//				MessageProperties.TEXT_PLAIN
				//对于持久化类型的消息，除了发送时使用MessageProperties.PERSISTENT_TEXT_PLAIN以外，还需要将queueDeclare时为durable=true
				JSONWriter jsonwriter = new JSONWriter();
				String body = jsonwriter.write("JAVA Bean or List<JAVA bean>");
				
				System.out.println(body);
				while(i> 0){
					channel.basicPublish(exchange,myQueue, MessageProperties.TEXT_PLAIN, message.getBytes());
					try{
//					    Thread.sleep(100);
					}catch(Exception e){
					    
					}
//					System.out.println(200 -i);
					i--;
				}
				
				
				channel.close();
				eService.shutdown();
				
				connection.close();
			}catch(Exception e){
				
			}
			
		}
		
	}

}
