package netty.exp3;

import com.google.common.base.Charsets;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class DelimiterBaseFrameServer {
    public static final ByteBuf DELIMITER = Unpooled.copiedBuffer("\1".getBytes());

    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker)
            .channel(NioServerSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO))
            .option(ChannelOption.SO_BACKLOG, 100)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline cp = ch.pipeline();
//                    cp.addLast(new FixedLengthFrameDecoder(20));
                    cp.addLast(new DelimiterBasedFrameDecoder(1024, DELIMITER));
                    cp.addLast(new StringDecoder(Charsets.UTF_8));
                    cp.addLast(new ServerHandler());
                    
                }
                
            });
            server.bind(12345).sync().channel().closeFuture().await();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
