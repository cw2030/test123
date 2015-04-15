package netty.exp1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TimerServer {

    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker);
            server.channel(NioServerSocketChannel.class);
            server.option(ChannelOption.SO_BACKLOG, 100);
            server.handler(new LoggingHandler(LogLevel.INFO));
            server.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimerServerHandler());
                }
                
            });
            ChannelFuture cf = server.bind(12345).sync();
            System.out.println("绑定12345端口");
            cf.channel().closeFuture().await();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
