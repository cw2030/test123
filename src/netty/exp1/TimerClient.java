package netty.exp1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimerClient {

    public static void main(String[] args) {
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap client = new Bootstrap();
            client.channel(NioSocketChannel.class)
            .group(worker)
            .handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimerClientHandler());
                    
                }
                
            });
            ChannelFuture future = client.connect("localhost", 12345).sync();
            future.channel().closeFuture().await();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
