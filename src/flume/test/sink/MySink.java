package flume.test.sink;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;

/**
 * 自定义sink
 * @author user
 *
 */
public class MySink extends AbstractSink implements Configurable {
    private String conf1;
    @Override
    public Status process() throws EventDeliveryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void configure(Context context) {
        // TODO Auto-generated method stub
        /**
         * 读取外部配置，如配置定义如下：
         * agent.sinks=k1
         * 
         * agent.sinks.k1.type=flume.test.sink.MySink
         * agent.sinks.k1.conf1=myconfig
         */
        
        conf1 = context.getString("conf1");
        System.out.println(conf1);
    }

    @Override
    public synchronized void start() {
        // TODO Auto-generated method stub
        super.start();
    }

    @Override
    public synchronized void stop() {
        // TODO Auto-generated method stub
        super.stop();
    }

}
