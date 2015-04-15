package netty.exp3;

import com.google.common.base.Charsets;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class DelimiterBaseFrameClient {
    public static void main(String[] args) {
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap client = new Bootstrap();
            client.group(worker)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline cp = ch.pipeline();
//                    cp.addLast(new FixedLengthFrameDecoder(20));
                    cp.addLast(new DelimiterBasedFrameDecoder(1024, DelimiterBaseFrameServer.DELIMITER));
                    cp.addLast(new StringDecoder(Charsets.UTF_8));
                    cp.addLast(new ClientHandler());
                    
                }
                
            });
            client.connect("localhost", 12345).channel().closeFuture().await();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            worker.shutdownGracefully();
        }

    }

}
