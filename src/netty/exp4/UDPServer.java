package netty.exp4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UDPServer {

    public static void main(String[] args) {
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap server = new Bootstrap();
            server.group(worker)
            .channel(NioDatagramChannel.class)
            .option(ChannelOption.SO_BROADCAST, true)
            .handler(new ServerHandler());
            
            server.bind(12345).sync().channel().closeFuture().await();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            worker.shutdownGracefully();
        }

    }

}
