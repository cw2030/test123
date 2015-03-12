package rmqexample.rpc;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.tools.json.JSONReader;
import com.rabbitmq.tools.json.JSONWriter;

public class RpcResponderConsumer extends DefaultConsumer {
	BookStore bookstore;
	JSONReader jsonReader;
	JSONWriter jsonWriter;
	Random random = new Random();
	ExecutorService es = Executors.newFixedThreadPool(20);
	Connection conn;
	Channel channel;
	LinkedBlockingQueue<Long> queue = new LinkedBlockingQueue<Long>();

	public RpcResponderConsumer(Channel channel,Connection conn) {
		super(channel);
		jsonReader = new JSONReader();
		jsonWriter = new JSONWriter();
		bookstore = new BookStore();
		this.conn = conn;
		this.channel = channel;
		es.execute(new Ack(conn));
	}

	public void handleDelivery(String consumerTag, 
			Envelope envelope,
			AMQP.BasicProperties properties, 
			byte[] body) throws java.io.IOException {
		es.execute(new GoRun(new String(body), getChannel(), envelope.getDeliveryTag()));
//		getChannel().basicAck(envelope.getDeliveryTag(), true);
	}
	
	class GoRun implements Runnable{
	    private String msg;
	    private Channel ch;
	    private long deliverTag;
	    
	    public GoRun(String msg,Channel ch,long deliverTag){
	        this.msg = msg;
	        this.ch = ch;
	        this.deliverTag = deliverTag;
	    }
	    
        @Override
        public void run() {
            String reply = null;
            try {
                Integer idToFind =(Integer)jsonReader.read(msg);
                System.out.println("Searching for: " + idToFind); 
                Book book = bookstore.GetBook(idToFind);
                
                reply = book != null ? jsonWriter.write(book) : "ERROR: BOOK not found";
                System.out.println(Thread.currentThread().getId() + " -->JSON response: ");
                int i = 0;
                while((i = random.nextInt(10000)) > 2000) {
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                reply ="ERROR: internal server error";
                System.err.println(e);
            } finally {
              try {
                  queue.add(deliverTag);
//                  channel.basicAck(deliverTag, true);
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }
            
        }
	}
	
	class Ack implements Runnable{
	    private Channel _ch ;
	    public Ack(Connection conn){
	        try {
                _ch = conn.createChannel();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	    }
        @Override
        public void run() {
            long tag = 0;
            try{
                while(true){
                    try{
                        tag = queue.take();
                        System.out.println("ACK: " + tag);
                        if(_ch.isOpen()){
                            _ch.basicAck(tag, false);
                        }else{
                            _ch = conn.createChannel();
                            _ch.basicAck(tag, false);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        try{
                            _ch = conn.createChannel();
                            _ch.basicAck(tag, false);
                        }catch(Exception ee){
                            ee.printStackTrace();
                        }
                    }
                        
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
	    
	}
}
