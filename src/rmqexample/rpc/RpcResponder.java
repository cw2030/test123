package rmqexample.rpc;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class RpcResponder {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="5";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Using RPC with messaging (RpcCallBack Server Java) **");
		String rabbitmqHost = "localhost";
		if (args.length > 0)
			rabbitmqHost = args[0];

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rabbitmqHost);

		try {
			Connection connection = factory.newConnection(Executors.newFixedThreadPool(20));
			System.out.println("Conneted:" + rabbitmqHost);
			Channel channel = connection.createChannel();
			String requestQueue = "MyRpcQueue_"+RECIPE_NR;
			channel.queueDeclare(requestQueue, false, false, false, null);
//			channel.basicQos(prefetchCount);
//			channel.basicQos(10);
//			channel.basicQos(10);
			RpcResponderConsumer consumer = new RpcResponderConsumer(channel,connection);
			String consumerTag = channel.basicConsume(requestQueue, false, consumer);
			channel.addShutdownListener(new ShutdownListener() {
                
                @Override
                public void shutdownCompleted(ShutdownSignalException cause) {
                    System.out.println("ShutdownSignalException");
                    cause.printStackTrace();
                }
            });
			System.out.println("Consuming messages from queue: " + requestQueue + " - press any key to exit");
			Thread.sleep(1000000);
			System.in.read();
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
			System.out.println("Execution completed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
